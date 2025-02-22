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

@file:Suppress("FunctionName")

package com.sree.oneapp.ui.onboarding

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sree.oneapp.androidtest.util.LiveDataTestUtil
import com.sree.oneapp.shared.data.prefs.PreferenceStorage
import com.sree.oneapp.shared.domain.prefs.OnboardingCompleteActionUseCase
import com.sree.oneapp.test.util.SyncTaskExecutorRule
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test

/**
 * Unit tests for the [OnboardingViewModel].
 */
class OnboardingViewModelTest {

    // Executes tasks in the Architecture Components in the same thread
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    // Executes tasks in a synchronous [TaskScheduler]
    @get:Rule
    var syncTaskExecutorRule = SyncTaskExecutorRule()

    @Test
    fun onGetStartedClicked_updatesPrefs() {
        // Given an onboarding view model
        val prefs = mock<PreferenceStorage>()
        val onboardingCompleteActionUseCase = OnboardingCompleteActionUseCase(prefs)
        val viewModel = OnboardingViewModel(onboardingCompleteActionUseCase)

        // When getStarted is called
        viewModel.getStartedClick()

        // Then verify that local storage was updated
        verify(prefs).onboardingCompleted = true

        // And that the navigation event was fired
        val navigateEvent = LiveDataTestUtil.getValue(viewModel.navigateToMainActivity)
        assertNotNull(navigateEvent?.getContentIfNotHandled())
    }
}
