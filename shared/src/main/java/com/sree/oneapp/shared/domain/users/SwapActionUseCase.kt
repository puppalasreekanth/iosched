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

package com.sree.oneapp.shared.domain.users

import com.sree.oneapp.model.SessionId
import com.sree.oneapp.shared.data.userevent.SessionAndUserEventRepository
import com.sree.oneapp.shared.domain.MediatorUseCase
import com.sree.oneapp.shared.domain.internal.DefaultScheduler
import com.sree.oneapp.shared.result.Result
import timber.log.Timber
import javax.inject.Inject

/**
 * Sends a request to replace reservations.
 */
open class SwapActionUseCase @Inject constructor(
    private val repository: SessionAndUserEventRepository
) : MediatorUseCase<SwapRequestParameters, SwapRequestAction>() {

    override fun execute(parameters: SwapRequestParameters) {

        DefaultScheduler.execute {
            try {
                val (userId, sessionId, _, toId) = parameters
                val updateResult = repository.swapReservation(userId, sessionId, toId)

                result.removeSource(updateResult)
                result.addSource(updateResult, {
                    result.postValue(updateResult.value)
                })
            } catch (e: Exception) {
                Timber.d("Exception in Swapping reservations")
                result.postValue(Result.Error(e))
            }
        }
    }
}

/**
 * Parameters required to process the swap reservations request.
 */
data class SwapRequestParameters(
    val userId: String,
    val fromId: SessionId,
    val fromTitle: String,
    val toId: SessionId,
    val toTitle: String
)

class SwapRequestAction
