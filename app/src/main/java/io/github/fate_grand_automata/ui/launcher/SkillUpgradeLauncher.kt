package io.github.fate_grand_automata.ui.launcher

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.fate_grand_automata.R
import io.github.fate_grand_automata.scripts.prefs.IPreferences
import io.github.fate_grand_automata.ui.Stepper


@Composable
fun skillUpgradeLauncher(
    prefs: IPreferences,
    modifier: Modifier = Modifier
): ScriptLauncherResponseBuilder {

    val skillUpgrade = prefs.skillUpgrade

    var shouldUpgrade1 by remember {
        mutableStateOf(false)
    }
    val minSkill1 by remember {
        mutableStateOf(skillUpgrade.minSkill1)
    }
    var upgradeSkill1 by remember {
        mutableStateOf(0)
    }
    var shouldUpgrade2 by remember {
        mutableStateOf(false)
    }
    val minSkill2 by remember {
        mutableStateOf(skillUpgrade.minSkill2)
    }
    var upgradeSkill2 by remember {
        mutableStateOf(0)
    }
    val skill2Available by remember {
        mutableStateOf(skillUpgrade.skill2Available)
    }

    var shouldUpgrade3 by remember {
        mutableStateOf(false)
    }
    val minSkill3 by remember {
        mutableStateOf(skillUpgrade.minSkill3)
    }
    var upgradeSkill3 by remember {
        mutableStateOf(0)
    }
    val skill3Available by remember {
        mutableStateOf(skillUpgrade.skill3Available)
    }

    var shouldUpdateAll by remember {
        mutableStateOf(false)
    }
    val lowestMinSkill = listOf(minSkill1, minSkill2, minSkill3).min()
    var targetAllSkillLevel by remember {
        mutableStateOf(
            lowestMinSkill
        )
    }


    LaunchedEffect(key1 = targetAllSkillLevel, block = {
        if (minSkill1 <= targetAllSkillLevel && shouldUpgrade1) {
            upgradeSkill1 = targetAllSkillLevel - minSkill1
        }
        if (minSkill2 <= targetAllSkillLevel && shouldUpgrade2) {
            upgradeSkill2 = targetAllSkillLevel - minSkill2
        }
        if (minSkill3 <= targetAllSkillLevel && shouldUpgrade3) {
            upgradeSkill3 = targetAllSkillLevel - minSkill3
        }
    })

    LaunchedEffect(key1 = shouldUpdateAll, block = {
        shouldUpgrade1 = shouldUpdateAll == true && minSkill1 < 10
        shouldUpgrade2 = shouldUpdateAll == true && minSkill2 < 10
        shouldUpgrade3 = shouldUpdateAll == true && minSkill3 < 10
    })

    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .padding(top = 5.dp)
    ) {
        Text(
            text = stringResource(id = R.string.skill_upgrade),
            style = MaterialTheme.typography.headlineSmall
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { shouldUpdateAll = !shouldUpdateAll }
        ) {
            Text(
                stringResource(R.string.skill_enhancement_all_question),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )

            Switch(
                checked = shouldUpdateAll,
                onCheckedChange = { shouldUpdateAll = it }
            )
        }

        Box(
            modifier = Modifier.align(Alignment.End)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TextButton(
                    onClick = { targetAllSkillLevel =  lowestMinSkill},
                    enabled = shouldUpdateAll,
                    modifier = Modifier.alignByBaseline()
                ) {
                    Text(text = stringResource(id = R.string.reset).uppercase())
                }
                Stepper(
                    modifier = Modifier.alignByBaseline(),
                    value = targetAllSkillLevel,
                    onValueChange = { targetAllSkillLevel = it },
                    valueRange = lowestMinSkill..10,
                    enabled = shouldUpdateAll,
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            SkillUpgradeItem(
                name = stringResource(id = R.string.skill_1),
                shouldUpgrade = shouldUpgrade1,
                onShouldUpgradeChange = {
                    shouldUpgrade1 = it
                },
                minimumUpgrade = minSkill1,
                upgradeLevel = upgradeSkill1,
                onUpgradeLevelChange = { upgradeSkill1 = it - minSkill1 },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp),
            )

            SkillUpgradeItem(
                name = stringResource(id = R.string.skill_2),
                shouldUpgrade = shouldUpgrade2,
                onShouldUpgradeChange = {
                    shouldUpgrade2 = it
                },
                minimumUpgrade = minSkill2,
                upgradeLevel = upgradeSkill2,
                onUpgradeLevelChange = { upgradeSkill2 = it - minSkill2 },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp),
                available = skill2Available
            )

            SkillUpgradeItem(
                name = stringResource(id = R.string.skill_3),
                shouldUpgrade = shouldUpgrade3,
                onShouldUpgradeChange = {
                    shouldUpgrade3 = it
                },
                minimumUpgrade = minSkill3,
                upgradeLevel = upgradeSkill3,
                onUpgradeLevelChange = { upgradeSkill3 = it - minSkill3 },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp),
                available = skill3Available
            )
        }
    }

    return ScriptLauncherResponseBuilder(
        canBuild = { true },
        build = {
            ScriptLauncherResponse.SkillUpgrade(
                shouldUpgradeSkill1 = shouldUpgrade1,
                upgradeSkill1 = upgradeSkill1,
                shouldUpgradeSkill2 = shouldUpgrade2,
                upgradeSkill2 = upgradeSkill2,
                shouldUpgradeSkill3 = shouldUpgrade3,
                upgradeSkill3 = upgradeSkill3,
            )
        }
    )
}

@Composable
fun SkillUpgradeItem(
    modifier: Modifier = Modifier,
    name: String,
    shouldUpgrade: Boolean,
    onShouldUpgradeChange: (Boolean) -> Unit,
    minimumUpgrade: Int,
    upgradeLevel: Int,
    onUpgradeLevelChange: (Int) -> Unit,
    available: Boolean = true
) {
    LazyColumn(
        modifier = modifier
            .fillMaxHeight()
            .clickable(
                enabled = minimumUpgrade < 10 && available,
                onClick = {
                    onShouldUpgradeChange(!shouldUpgrade)
                }
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (available) {
            if (minimumUpgrade < 10) {
                item {
                    Checkbox(
                        checked = shouldUpgrade,
                        onCheckedChange = {
                            onShouldUpgradeChange(!shouldUpgrade)
                        },
                    )
                }
            }
            item {
                Text(
                    text = when (minimumUpgrade < 10) {
                        true -> name.uppercase()
                        false -> name.uppercase() + "\n" + stringResource(id = R.string.skill_max).uppercase()
                    },
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium,
                    color = when (shouldUpgrade) {
                        true -> MaterialTheme.colorScheme.onBackground
                        false -> MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
                    }
                )
            }
            if (minimumUpgrade < 10) {
                item {
                    Stepper(
                        value = (upgradeLevel + minimumUpgrade),
                        onValueChange = { onUpgradeLevelChange(it) },
                        valueRange = minimumUpgrade..10,
                        enabled = shouldUpgrade
                    )
                }
            }

        } else {
            item {
                Text(
                    text = name.uppercase(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
                )
            }
            item {
                Text(
                    text = stringResource(id = R.string.skill_not_available).uppercase(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
                )
            }
        }
    }
}