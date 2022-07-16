package com.test.kabak.openweather

import android.Manifest
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import com.kaspersky.kaspresso.screens.KScreen
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.test.kabak.openweather.ui.addCity.AddCityActivity
import org.junit.Rule
import org.junit.Test

class SimpleTest : TestCase() {
    @get:Rule
    val runtimePermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
    )

    @get:Rule
    val activityTestRule = ActivityTestRule(AddCityActivity::class.java, true, false)

    @Test
    fun test() =
        run {
            step("Open Simple Screen") {
                activityTestRule.launchActivity(null)
                MainScreen { }
            }
        }
}

object MainScreen : KScreen<MainScreen>() {

    override val layoutId: Int = R.layout.activity_add_city
    override val viewClass: Class<*> = AddCityActivity::class.java
}