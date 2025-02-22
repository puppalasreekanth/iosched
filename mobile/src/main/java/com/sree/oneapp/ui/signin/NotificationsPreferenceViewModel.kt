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

package com.sree.oneapp.ui.signin

import androidx.lifecycle.ViewModel
import com.sree.oneapp.shared.domain.prefs.NotificationsPrefSaveActionUseCase
import com.sree.oneapp.shared.domain.prefs.NotificationsPrefShownActionUseCase
import javax.inject.Inject

/**
 * ViewModel for the dialog to show notifications preference
 */
class NotificationsPreferenceViewModel @Inject constructor(
    private val notificationsPrefShownActionUseCase: NotificationsPrefShownActionUseCase,
    private val notificationsPrefSaveActionUseCase: NotificationsPrefSaveActionUseCase
) : ViewModel() {

    fun onYesClicked() {
        notificationsPrefSaveActionUseCase(true)
    }

    fun onNoClicked() {
        notificationsPrefSaveActionUseCase(false)
    }

    fun onDismissed() {
        notificationsPrefShownActionUseCase(true)
    }
}
