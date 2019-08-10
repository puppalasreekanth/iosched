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

package com.sree.oneapp.shared.di

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.sree.oneapp.shared.BuildConfig
import com.sree.oneapp.shared.R
import com.sree.oneapp.shared.data.BootstrapConferenceDataSource
import com.sree.oneapp.shared.data.ConferenceDataRepository
import com.sree.oneapp.shared.data.ConferenceDataSource
import com.sree.oneapp.shared.data.NetworkConferenceDataSource
import com.sree.oneapp.shared.data.logistics.LogisticsDataSource
import com.sree.oneapp.shared.data.logistics.LogisticsRepository
import com.sree.oneapp.shared.data.logistics.RemoteConfigLogisticsDataSource
import com.sree.oneapp.shared.data.session.DefaultSessionRepository
import com.sree.oneapp.shared.data.session.SessionRepository
import com.sree.oneapp.shared.data.userevent.DefaultSessionAndUserEventRepository
import com.sree.oneapp.shared.data.userevent.FirestoreUserEventDataSource
import com.sree.oneapp.shared.data.userevent.SessionAndUserEventRepository
import com.sree.oneapp.shared.data.userevent.UserEventDataSource
import com.sree.oneapp.shared.fcm.FcmTopicSubscriber
import com.sree.oneapp.shared.fcm.TopicSubscriber
import com.sree.oneapp.shared.time.DefaultTimeProvider
import com.sree.oneapp.shared.time.TimeProvider
import com.sree.oneapp.shared.util.NetworkUtils
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

/**
 * Module where classes created in the shared module are created.
 */
@Module
class SharedModule {

// Define the data source implementations that should be used. All data sources are singletons.

    @Singleton
    @Provides
    @Named("remoteConfDatasource")
    fun provideConferenceDataSource(
        context: Context,
        networkUtils: NetworkUtils
    ): ConferenceDataSource {
        return NetworkConferenceDataSource(context, networkUtils)
    }

    @Singleton
    @Provides
    @Named("bootstrapConfDataSource")
    fun provideBootstrapRemoteSessionDataSource(): ConferenceDataSource {
        return BootstrapConferenceDataSource
    }

    @Singleton
    @Provides
    fun provideConferenceDataRepository(
        @Named("remoteConfDatasource") remoteDataSource: ConferenceDataSource,
        @Named("bootstrapConfDataSource") boostrapDataSource: ConferenceDataSource
    ): ConferenceDataRepository {
        return ConferenceDataRepository(remoteDataSource, boostrapDataSource)
    }

    @Singleton
    @Provides
    fun provideSessionRepository(
        conferenceDataRepository: ConferenceDataRepository
    ): SessionRepository {
        return DefaultSessionRepository(conferenceDataRepository)
    }

    @Singleton
    @Provides
    fun provideUserEventDataSource(firestore: FirebaseFirestore): UserEventDataSource {
        return FirestoreUserEventDataSource(firestore)
    }

    @Singleton
    @Provides
    fun provideSessionAndUserEventRepository(
        userEventDataSource: UserEventDataSource,
        sessionRepository: SessionRepository
    ): SessionAndUserEventRepository {
        return DefaultSessionAndUserEventRepository(userEventDataSource, sessionRepository)
    }

    @Singleton
    @Provides
    fun provideFirebaseFireStore(): FirebaseFirestore {
        val firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder()
            // This is to enable the offline data
            // https://firebase.google.com/docs/firestore/manage-data/enable-offline
            .setPersistenceEnabled(true)
            .build()
        return firestore
    }

    @Singleton
    @Provides
    fun provideTopicSubscriber(): TopicSubscriber {
        return FcmTopicSubscriber()
    }

    @Singleton
    @Provides
    fun provideFirebaseRemoteConfig(): FirebaseRemoteConfig {
        val remoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setDeveloperModeEnabled(BuildConfig.DEBUG)
            .build()
        remoteConfig.setConfigSettings(configSettings)
        remoteConfig.setDefaults(R.xml.remote_config_defaults)
        return remoteConfig
    }

    @Singleton
    @Provides
    fun provideLogisticsDataSource(remoteConfig: FirebaseRemoteConfig): LogisticsDataSource {
        return RemoteConfigLogisticsDataSource(remoteConfig)
    }

    @Singleton
    @Provides
    fun provideLogisticsRepository(
        logisticsDataSource: LogisticsDataSource
    ): LogisticsRepository {
        return LogisticsRepository(logisticsDataSource)
    }

    @Singleton
    @Provides
    fun provideTimeProvider(): TimeProvider {
        return DefaultTimeProvider
    }
}
