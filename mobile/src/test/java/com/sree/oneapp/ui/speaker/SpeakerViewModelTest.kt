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

package com.sree.oneapp.ui.speaker

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sree.oneapp.androidtest.util.LiveDataTestUtil
import com.sree.oneapp.model.TestDataRepository
import com.sree.oneapp.shared.analytics.AnalyticsHelper
import com.sree.oneapp.shared.data.session.DefaultSessionRepository
import com.sree.oneapp.shared.data.userevent.DefaultSessionAndUserEventRepository
import com.sree.oneapp.shared.domain.sessions.LoadUserSessionsUseCase
import com.sree.oneapp.shared.domain.speakers.LoadSpeakerUseCase
import com.sree.oneapp.test.data.TestData
import com.sree.oneapp.test.util.SyncTaskExecutorRule
import com.sree.oneapp.test.util.fakes.FakeAnalyticsHelper
import com.sree.oneapp.test.util.fakes.FakeEventActionsViewModelDelegate
import com.sree.oneapp.test.util.fakes.FakeSignInViewModelDelegate
import com.sree.oneapp.ui.schedule.day.TestUserEventDataSource
import com.sree.oneapp.ui.sessioncommon.EventActionsViewModelDelegate
import com.sree.oneapp.ui.signin.SignInViewModelDelegate
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

/**
 * Unit tests for the [SpeakerViewModel].
 */
class SpeakerViewModelTest {

    // Executes tasks in the Architecture Components in the same thread
    @get:Rule var instantTaskExecutorRule = InstantTaskExecutorRule()

    // Executes tasks in a synchronous [TaskScheduler]
    @get:Rule var syncTaskExecutorRule = SyncTaskExecutorRule()

    @Test
    fun setSpeakerId_loadsSpeaker() {
        // Given a speaker view model
        val viewModel = createViewModel()

        // When the speaker ID is set
        viewModel.setSpeakerId(TestData.speaker1.id)

        // Then the speaker is loaded
        assertEquals(TestData.speaker1, LiveDataTestUtil.getValue(viewModel.speaker))
    }

    @Test
    fun setSpeakerId_loadsSpeakersEvents_singleEvent() {
        // Given a speaker view model
        val viewModel = createViewModel()

        // When the ID of a speaker with a single event is set
        viewModel.setSpeakerId(TestData.speaker3.id)

        // Then the speakers event is loaded
        assertEquals(
            listOf(TestData.userSession2),
            LiveDataTestUtil.getValue(viewModel.speakerUserSessions)
        )
    }

    @Test
    fun setSpeakerId_loadsSpeakersEvents_multipleEvents() {
        // Given a speaker view model
        val viewModel = createViewModel()

        // When the ID of a speaker with multiple events is set
        viewModel.setSpeakerId(TestData.speaker1.id)

        // Then the speakers events are loaded
        assertEquals(
            listOf(TestData.userSession0, TestData.userSession3, TestData.userSession4),
            LiveDataTestUtil.getValue(viewModel.speakerUserSessions)
        )
    }

    private fun createViewModel(
        loadSpeakerUseCase: LoadSpeakerUseCase = LoadSpeakerUseCase(TestDataRepository),
        loadSpeakerSessionsUseCase: LoadUserSessionsUseCase = LoadUserSessionsUseCase(
            DefaultSessionAndUserEventRepository(
                TestUserEventDataSource(), DefaultSessionRepository(TestDataRepository)
            )
        ),
        signInViewModelDelegate: SignInViewModelDelegate = FakeSignInViewModelDelegate().apply {
            loadUser("123")
        },
        eventActionsDelegate: EventActionsViewModelDelegate = FakeEventActionsViewModelDelegate(),
        analyticsHelper: AnalyticsHelper = FakeAnalyticsHelper()
    ): SpeakerViewModel {
        return SpeakerViewModel(
            loadSpeakerUseCase,
            loadSpeakerSessionsUseCase,
            signInViewModelDelegate,
            eventActionsDelegate,
            analyticsHelper
        )
    }
}
