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

package com.sree.oneapp.shared.domain.prefs

import com.sree.oneapp.shared.data.prefs.PreferenceStorage
import com.sree.oneapp.shared.domain.UseCase
import javax.inject.Inject

/**
 * Records the user preference if the user wants to receive notifications from oneapp.
 */
open class NotificationsPrefSaveActionUseCase @Inject constructor(
    private val preferenceStorage: PreferenceStorage
) : UseCase<Boolean, Boolean>() {
    override fun execute(parameters: Boolean): Boolean {
        preferenceStorage.preferToReceiveNotifications = parameters
        return preferenceStorage.preferToReceiveNotifications
    }
}
