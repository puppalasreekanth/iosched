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

package com.sree.oneapp.shared.fcm

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.app.job.JobScheduler.RESULT_FAILURE
import android.app.job.JobScheduler.RESULT_SUCCESS
import android.content.ComponentName
import android.content.Context
import com.google.firebase.messaging.RemoteMessage
import com.sree.oneapp.shared.data.job.ConferenceDataService
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * Receives Firebase Cloud Messages and starts a [ConferenceDataService] to download new data.
 */
class IoschedFirebaseMessagingService : DaggerFirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        Timber.d("Message data payload: ${remoteMessage?.data}")
        val data = remoteMessage?.data ?: return
        if (data[TRIGGER_EVENT_DATA_SYNC_key] == TRIGGER_EVENT_DATA_SYNC) {
            // Schedule job on JobScheduler when FCM message with action `TRIGGER_EVENT_DATA_SYNC`
            // is received.
            scheduleFetchEventData()
        }
    }

    private fun scheduleFetchEventData() {
        val serviceComponent = ComponentName(this, ConferenceDataService::class.java)
        val builder = JobInfo.Builder(ConferenceDataService.JOB_ID, serviceComponent)
            .setMinimumLatency(MINIMUM_LATENCY) // wait at least
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED) // Unmetered if possible
            .setOverrideDeadline(OVERRIDE_DEADLINE) // run by deadline if conditions not met

        val jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler

        val result = jobScheduler.schedule(builder.build())

        if (result == RESULT_FAILURE) {
            Timber.e(
                "Invalid param supplied to JobScheduler when starting ConferenceDataService job."
            )
        } else if (result == RESULT_SUCCESS) {
            Timber.i("ConferenceDataService job scheduled..")
        }
    }

    companion object {
        private const val TRIGGER_EVENT_DATA_SYNC = "SYNC_EVENT_DATA"
        private const val TRIGGER_EVENT_DATA_SYNC_key = "action"

        // Some latency to avoid load spikes
        private val MINIMUM_LATENCY = TimeUnit.SECONDS.toSeconds(5)

        // Job scheduled to run only with Wi-Fi but with a deadline
        private val OVERRIDE_DEADLINE = TimeUnit.SECONDS.toMinutes(15)
    }
}
