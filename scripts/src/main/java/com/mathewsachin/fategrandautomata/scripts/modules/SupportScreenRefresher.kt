package com.mathewsachin.fategrandautomata.scripts.modules

import com.mathewsachin.fategrandautomata.scripts.IFgoAutomataApi
import com.mathewsachin.fategrandautomata.scripts.Images
import com.mathewsachin.fategrandautomata.scripts.ScriptNotify
import com.mathewsachin.libautomata.dagger.ScriptScope
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.TimeMark
import kotlin.time.TimeSource

@ScriptScope
class SupportScreenRefresher @Inject constructor(
    fgAutomataApi: IFgoAutomataApi,
    private val connectionRetry: ConnectionRetry
) : IFgoAutomataApi by fgAutomataApi {
    private var lastSupportRefreshTimestamp: TimeMark? = null
    private val supportRefreshThreshold = Duration.seconds(10)

    fun refreshSupportList() {
        lastSupportRefreshTimestamp?.elapsedNow()?.let { elapsed ->
            val toWait = supportRefreshThreshold - elapsed

            if (toWait.isPositive()) {
                messages.notify(ScriptNotify.SupportListUpdatingIn(toWait))

                toWait.wait()
            }
        }

        game.support.updateClick.click()
        Duration.seconds(1).wait()

        game.support.updateYesClick.click()

        waitForSupportScreenToLoad()
        updateLastSupportRefreshTimestamp()
    }

    private fun updateLastSupportRefreshTimestamp() {
        lastSupportRefreshTimestamp = TimeSource.Monotonic.markNow()
    }

    fun waitForSupportScreenToLoad() {
        while (true) {
            when {
                connectionRetry.needsToRetry() -> connectionRetry.retry()
                // wait for dialogs to close
                images[Images.SupportExtra] !in game.support.extraRegion -> Duration.seconds(1).wait()
                images[Images.SupportNotFound] in game.support.notFoundRegion -> {
                    updateLastSupportRefreshTimestamp()
                    refreshSupportList()
                    return
                }
                game.support.confirmSetupButtonRegion.exists(
                    images[Images.SupportConfirmSetupButton],
                    similarity = Support.supportRegionToolSimilarity
                ) -> return
                images[Images.Guest] in game.support.friendRegion -> return
            }

            Duration.milliseconds(100).wait()
        }
    }
}