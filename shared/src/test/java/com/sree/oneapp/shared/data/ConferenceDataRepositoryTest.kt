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

package com.sree.oneapp.shared.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sree.oneapp.androidtest.util.LiveDataTestUtil
import com.sree.oneapp.model.ConferenceData
import com.sree.oneapp.test.data.TestData
import com.sree.oneapp.test.data.TestData.session1
import com.sree.oneapp.test.data.TestData.session3
import com.sree.oneapp.test.util.SyncTaskExecutorRule
import org.hamcrest.core.IsEqual.equalTo
import org.hamcrest.core.IsNot.not
import org.hamcrest.core.IsNull.notNullValue
import org.hamcrest.core.IsNull.nullValue
import org.junit.Assert.assertThat
import org.junit.Rule
import org.junit.Test
import java.io.IOException
import org.hamcrest.core.Is.`is` as Is

/**
 * Unit tests for [ConferenceDataRepository].
 */
class ConferenceDataRepositoryTest {

    // Executes tasks in the Architecture Components in the same thread
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    // Executes tasks in a synchronous [TaskScheduler]
    @get:Rule
    var syncTaskExecutorRule = SyncTaskExecutorRule()

    private lateinit var repo: ConferenceDataRepository

    @Test
    fun remotePrevailsOverBootstrap() {
        // Given a repo with a working remote data source that returns session0
        // and a bootstrap that returns session 3
        repo = ConferenceDataRepository(
            remoteDataSource = TestConfDataSourceSession0(),
            boostrapDataSource = BootstrapDataSourceSession3()
        )

        // When requesting previously-refreshed data
        repo.refreshCacheWithRemoteConferenceData()
        val data = repo.getOfflineConferenceData()

        // Only session 0 should be available because remote has priority
        assertThat(data.sessions.first(), Is(equalTo(TestData.session0)))
        // and meta info should be set
        assertThat(repo.latestUpdateSource, Is(equalTo(UpdateSource.NETWORK)))
        assertThat(LiveDataTestUtil.getValue(repo.dataLastUpdatedObservable), Is(not(equalTo(0L))))
        assertThat(repo.latestException, Is(nullValue()))
        assertThat(repo.currentConferenceDataVersion, Is(equalTo(NETWORK_DATA_VERSION)))
    }

    @Test
    fun remoteNotAvailable_bootstrapUsed() {
        // Given a repo with unavailable remote data source
        // and a bootstrap that returns session 3
        repo = ConferenceDataRepository(
            remoteDataSource = NotAvailableDataSource(),
            boostrapDataSource = BootstrapDataSourceSession3()
        )

        // Remote conference throws an error
        try {
            repo.refreshCacheWithRemoteConferenceData()
        } catch (e: Exception) {
            assertThat(e, Is(notNullValue()))
        }

        // When requesting data, the only data available is Bootstrap
        val data = repo.getOfflineConferenceData()

        // Only session 3 should be available because remote has priority
        assertThat(data.sessions.first(), Is(equalTo(session3)))
        // and meta info should be set
        assertThat(repo.latestUpdateSource, Is(equalTo(UpdateSource.BOOTSTRAP)))
        assertThat(LiveDataTestUtil.getValue(repo.dataLastUpdatedObservable), Is(not(equalTo(0L))))
        assertThat(repo.latestException, Is(notNullValue()))
        assertThat(repo.currentConferenceDataVersion, Is(equalTo(BOOTSTRAP_DATA_VERSION)))
    }

    @Test
    fun networkExceptionCacheUnavailable_cacheReturned() {
        // Given a repo with unavailable remote data (that throws an exception) and no cache
        // and a bootstrap that returns session 1
        repo = ConferenceDataRepository(
            remoteDataSource = ThrowingDataSourceNoCache(),
            boostrapDataSource = TestConfDataSourceSession1()
        )

        // Remote conference throws an error
        try {
            repo.refreshCacheWithRemoteConferenceData()
        } catch (e: Exception) {
            assertThat(e, Is(notNullValue()))
        }

        // When requesting data
        val data = repo.getOfflineConferenceData()

        // Only session 1 should be available because remote has no good data
        assertThat(data.sessions.first(), Is(equalTo(session1)))
        // and meta info should be set
        assertThat(repo.latestUpdateSource, Is(equalTo(UpdateSource.BOOTSTRAP)))
        assertThat(LiveDataTestUtil.getValue(repo.dataLastUpdatedObservable), Is(not(equalTo(0L))))
        assertThat(repo.latestException, Is(notNullValue()))
        assertThat(repo.currentConferenceDataVersion, Is(equalTo(CACHE_DATA_VERSION)))
    }
}

/**
 * Test data and classes:
 */

private const val NETWORK_DATA_VERSION = 42
private const val CACHE_DATA_VERSION = 23
private const val BOOTSTRAP_DATA_VERSION = 314

class TestConfDataSourceSession0 : ConferenceDataSource {
    override fun getRemoteConferenceData(): ConferenceData? {
        return conferenceData
    }

    override fun getOfflineConferenceData(): ConferenceData? {
        return conferenceData
    }

    private val conferenceData = ConferenceData(
        sessions = listOf(TestData.session0),
        tags = listOf(TestData.androidTag, TestData.webTag),
        speakers = listOf(TestData.speaker1),
        rooms = emptyList(),
        version = NETWORK_DATA_VERSION
    )
}

private class TestConfDataSourceSession1 : ConferenceDataSource {
    override fun getRemoteConferenceData(): ConferenceData? {
        return ConferenceData(
            sessions = listOf(TestData.session1),
            tags = listOf(TestData.androidTag, TestData.webTag),
            speakers = listOf(TestData.speaker1),
            rooms = emptyList(),
            version = NETWORK_DATA_VERSION
        )
    }

    override fun getOfflineConferenceData(): ConferenceData? {
        return ConferenceData(
            sessions = listOf(TestData.session1),
            tags = listOf(TestData.androidTag, TestData.webTag),
            speakers = listOf(TestData.speaker1),
            rooms = emptyList(),
            version = CACHE_DATA_VERSION
        )
    }
}

class BootstrapDataSourceSession3 : ConferenceDataSource {
    override fun getRemoteConferenceData(): ConferenceData? {
        throw NotImplementedError() // Not used
    }

    override fun getOfflineConferenceData(): ConferenceData? {
        return ConferenceData(
            sessions = listOf(TestData.session3),
            tags = listOf(TestData.androidTag, TestData.webTag),
            speakers = listOf(TestData.speaker1),
            rooms = emptyList(),
            version = BOOTSTRAP_DATA_VERSION
        )
    }
}

private class TestConfDataSourceOnlyCachedSession1 : ConferenceDataSource {
    override fun getRemoteConferenceData(): ConferenceData? {
        return null
    }

    override fun getOfflineConferenceData(): ConferenceData? {
        return ConferenceData(
            sessions = listOf(TestData.session1),
            tags = listOf(TestData.androidTag, TestData.webTag),
            speakers = listOf(TestData.speaker1),
            rooms = emptyList(),
            version = CACHE_DATA_VERSION
        )
    }
}

class NotAvailableDataSource : ConferenceDataSource {
    override fun getRemoteConferenceData(): ConferenceData? {
        return null
    }

    override fun getOfflineConferenceData(): ConferenceData? {
        return null
    }
}

private class ThrowingDataSourceCacheSession2 : ConferenceDataSource {
    override fun getRemoteConferenceData(): ConferenceData? {
        throw IOException("Test")
    }

    override fun getOfflineConferenceData(): ConferenceData? {
        return ConferenceData(
            sessions = listOf(TestData.session2),
            tags = listOf(TestData.androidTag, TestData.webTag),
            speakers = listOf(TestData.speaker1),
            rooms = emptyList(),
            version = CACHE_DATA_VERSION
        )
    }
}

private class ThrowingDataSourceNoCache : ConferenceDataSource {
    override fun getRemoteConferenceData(): ConferenceData? {
        throw IOException("Test")
    }

    override fun getOfflineConferenceData(): ConferenceData? {
        return null
    }
}
