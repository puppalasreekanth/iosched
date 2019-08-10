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

package com.sree.oneapp.ui.info

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sree.oneapp.shared.domain.prefs.NotificationsPrefSaveActionUseCase
import com.sree.oneapp.shared.domain.settings.GetAnalyticsSettingUseCase
import com.sree.oneapp.shared.domain.settings.GetNotificationsSettingUseCase
import com.sree.oneapp.shared.domain.settings.GetTimeZoneUseCase
import com.sree.oneapp.shared.domain.settings.SetAnalyticsSettingUseCase
import com.sree.oneapp.shared.domain.settings.SetTimeZoneUseCase
import com.sree.oneapp.shared.result.Result
import com.sree.oneapp.shared.result.Result.Success
import com.sree.oneapp.shared.util.map
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    val setTimeZoneUseCase: SetTimeZoneUseCase,
    getTimeZoneUseCase: GetTimeZoneUseCase,
    val notificationsPrefSaveActionUseCase: NotificationsPrefSaveActionUseCase,
    getNotificationsSettingUseCase: GetNotificationsSettingUseCase,
    val setAnalyticsSettingUseCase: SetAnalyticsSettingUseCase,
    getAnalyticsSettingUseCase: GetAnalyticsSettingUseCase
) : ViewModel() {

    // Time Zone setting
    private val preferConferenceTimeZoneResult = MutableLiveData<Result<Boolean>>()
    val preferConferenceTimeZone: LiveData<Boolean>

    // Notifications setting
    val enableNotificationsResult = MutableLiveData<Result<Boolean>>()
    val enableNotifications: LiveData<Boolean>

    // Analytics setting
    private val sendUsageStatisticsResult = MutableLiveData<Result<Boolean>>()
    val sendUsageStatistics: LiveData<Boolean>

    init {
        getTimeZoneUseCase(Unit, preferConferenceTimeZoneResult)
        preferConferenceTimeZone = preferConferenceTimeZoneResult.map {
            (it as? Success<Boolean>)?.data ?: true
        }

        getAnalyticsSettingUseCase(Unit, sendUsageStatisticsResult)
        sendUsageStatistics = sendUsageStatisticsResult.map {
            (it as? Success<Boolean>)?.data ?: false
        }

        getNotificationsSettingUseCase(Unit, enableNotificationsResult)
        enableNotifications = enableNotificationsResult.map {
            (it as? Success<Boolean>)?.data ?: false
        }
    }

    fun toggleTimeZone(checked: Boolean) {
        setTimeZoneUseCase(checked, preferConferenceTimeZoneResult)
    }

    fun toggleSendUsageStatistics(checked: Boolean) {
        setAnalyticsSettingUseCase(checked, sendUsageStatisticsResult)
    }

    fun toggleEnableNotifications(checked: Boolean) {
        notificationsPrefSaveActionUseCase(checked, enableNotificationsResult)
    }
}
