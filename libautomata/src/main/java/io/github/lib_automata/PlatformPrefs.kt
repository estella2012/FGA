package io.github.lib_automata

interface PlatformPrefs {
    val debugMode: Boolean

    /**
     * The default minimum similarity used for image comparisons.
     */
    val minSimilarity: Double

    val waitMultiplier: Double

    val swipeMultiplier: Double
}