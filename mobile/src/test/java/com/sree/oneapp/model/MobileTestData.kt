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

package com.sree.oneapp.model

import com.sree.oneapp.shared.data.ConferenceDataRepository
import com.sree.oneapp.shared.data.ConferenceDataSource
import com.sree.oneapp.test.data.TestData
import com.sree.oneapp.test.data.TestData.androidTag
import com.sree.oneapp.test.data.TestData.cloudTag
import com.sree.oneapp.test.data.TestData.codelabsTag
import com.sree.oneapp.test.data.TestData.sessionsTag
import com.sree.oneapp.test.data.TestData.webTag
import com.sree.oneapp.ui.schedule.filters.EventFilter.TagFilter

/**
 * Test data for unit tests.
 */
object MobileTestData {

    val tagFiltersList = listOf(
        androidTag, webTag, cloudTag, // TOPIC
        sessionsTag, codelabsTag      // TYPE
    ).map { TagFilter(it, false) }
}

object TestDataSource : ConferenceDataSource {
    override fun getRemoteConferenceData(): ConferenceData? {
        return TestData.conferenceData
    }

    override fun getOfflineConferenceData(): ConferenceData? {
        return TestData.conferenceData
    }
}

/** ConferenceDataRepository for tests */
object TestDataRepository : ConferenceDataRepository(TestDataSource, TestDataSource)
