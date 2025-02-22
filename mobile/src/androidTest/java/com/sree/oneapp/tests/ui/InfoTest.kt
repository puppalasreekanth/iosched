/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sree.oneapp.tests.ui

import androidx.test.InstrumentationRegistry
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.sree.oneapp.R
import com.sree.oneapp.tests.SetPreferencesRule
import com.sree.oneapp.tests.SyncTaskExecutorRule
import com.sree.oneapp.ui.MainActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Espresso tests for the Info screen, covering main use case.
 */
@RunWith(AndroidJUnit4::class)
class InfoTest {

    @get:Rule
    var activityRule = ActivityTestRule<MainActivity>(MainActivity::class.java)

    // Executes tasks in a synchronous [TaskScheduler]
    @get:Rule
    var syncTaskExecutorRule = SyncTaskExecutorRule()

    // Sets the preferences so no welcome screens are shown
    @get:Rule
    var preferencesRule = SetPreferencesRule()

    private val resources = InstrumentationRegistry.getTargetContext().resources

    @Before
    fun goToInfoScreen() {
        onView(withId(R.id.navigation_info)).perform(ViewActions.click())
    }

    @Test
    fun info_basicViewsDisplayed() {
        onView(withText(resources.getString(R.string.wifi_header))).check(matches(isDisplayed()))
        // Travel tab
        onView(withText(resources.getString(R.string.travel_title))).perform(click())
        onView(withText(resources.getString(R.string.travel_directions_title)))
            .check(matches(isDisplayed()))
        // About tab
        onView(withText(resources.getString(R.string.about_title))).perform(click())
        onView(withText(resources.getString(R.string.faq_title))).check(matches(isDisplayed()))
        // Setting tab
        onView(withText(resources.getString(R.string.settings_title))).perform(click())
        onView(withText(resources.getString(R.string.settings_enable_notifications)))
            .check(matches(isDisplayed()))
    }
}
