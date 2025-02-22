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

package com.sree.oneapp.ui.sessioncommon

import com.sree.oneapp.shared.domain.users.StarEventUseCase
import com.sree.oneapp.ui.messages.SnackbarMessageManager
import com.sree.oneapp.ui.signin.SignInViewModelDelegate
import dagger.Module
import dagger.Provides

/**
 * Provides a default implementation of [EventActionsViewModelDelegate].
 */
@Module
internal class EventActionsViewModelDelegateModule {

    @Provides
    fun provideEventActionsViewModelDelegate(
        signInViewModelDelegate: SignInViewModelDelegate,
        starEventUseCase: StarEventUseCase,
        snackbarMessageManager: SnackbarMessageManager
    ): EventActionsViewModelDelegate {
        return DefaultEventActionsViewModelDelegate(
            signInViewModelDelegate,
            starEventUseCase,
            snackbarMessageManager
        )
    }
}
