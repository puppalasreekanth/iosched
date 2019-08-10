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
package com.sree.oneapp.di

import com.sree.oneapp.shared.di.ActivityScoped
import com.sree.oneapp.ui.LaunchModule
import com.sree.oneapp.ui.LauncherActivity
import com.sree.oneapp.ui.MainActivity
import com.sree.oneapp.ui.info.InfoModule
import com.sree.oneapp.ui.map.MapActivity
import com.sree.oneapp.ui.map.MapModule
import com.sree.oneapp.ui.onboarding.OnboardingActivity
import com.sree.oneapp.ui.onboarding.OnboardingModule
import com.sree.oneapp.ui.prefs.PreferenceModule
import com.sree.oneapp.ui.reservation.ReservationModule
import com.sree.oneapp.ui.schedule.ScheduleModule
import com.sree.oneapp.ui.sessioncommon.EventActionsViewModelDelegateModule
import com.sree.oneapp.ui.sessiondetail.SessionDetailActivity
import com.sree.oneapp.ui.sessiondetail.SessionDetailModule
import com.sree.oneapp.ui.signin.SignInDialogModule
import com.sree.oneapp.ui.speaker.SpeakerActivity
import com.sree.oneapp.ui.speaker.SpeakerModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * We want Dagger.Android to create a Subcomponent which has a parent Component of whichever module
 * ActivityBindingModule is on, in our case that will be [AppComponent]. You never
 * need to tell [AppComponent] that it is going to have all these subcomponents
 * nor do you need to tell these subcomponents that [AppComponent] exists.
 * We are also telling Dagger.Android that this generated SubComponent needs to include the
 * specified modules and be aware of a scope annotation [@ActivityScoped].
 * When Dagger.Android annotation processor runs it will create 2 subcomponents for us.
 */
@Module
abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = [LaunchModule::class])
    internal abstract fun launcherActivity(): LauncherActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [OnboardingModule::class])
    internal abstract fun onboardingActivity(): OnboardingActivity

    @ActivityScoped
    @ContributesAndroidInjector(
        modules = [
            ScheduleModule::class,
            MapModule::class,
            InfoModule::class,
            SignInDialogModule::class,
            ReservationModule::class,
            PreferenceModule::class
        ]
    )
    internal abstract fun mainActivity(): MainActivity

    @ActivityScoped
    @ContributesAndroidInjector(
        modules = [
            SessionDetailModule::class,
            SignInDialogModule::class,
            ReservationModule::class,
            PreferenceModule::class
        ]
    )
    internal abstract fun sessionDetailActivity(): SessionDetailActivity

    @ActivityScoped
    @ContributesAndroidInjector(
        modules = [
            SpeakerModule::class,
            SignInDialogModule::class,
            EventActionsViewModelDelegateModule::class,
            PreferenceModule::class
        ]
    )
    internal abstract fun speakerActivity(): SpeakerActivity

    @ActivityScoped
    @ContributesAndroidInjector(
        modules = [
            MapModule::class,
            PreferenceModule::class
        ]
    )
    internal abstract fun mapActivity(): MapActivity
}
