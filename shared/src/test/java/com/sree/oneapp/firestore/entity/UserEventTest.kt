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

package com.sree.oneapp.firestore.entity

import com.sree.oneapp.model.reservations.ReservationRequestResult
import com.sree.oneapp.model.reservations.ReservationRequestResult.ReservationRequestStatus
import com.sree.oneapp.model.userdata.UserEvent
import com.sree.oneapp.model.userdata.UserEvent.ReservationStatus
import com.sree.oneapp.model.userdata.UserEvent.ReservationStatus.NONE
import com.sree.oneapp.model.userdata.UserEvent.ReservationStatus.RESERVED
import com.sree.oneapp.model.userdata.UserEvent.ReservationStatus.WAITLISTED
import com.sree.oneapp.test.data.TestData
import org.junit.Assert.assertTrue
import org.junit.Test

class UserEventTest {

    private fun createTestEvent(
        isStarred: Boolean = false,
        status: ReservationStatus = NONE,
        requestResult: ReservationRequestResult? = null
    ): UserEvent {
        return TestData.userEvents[0].copy(
            isStarred = isStarred,
            reservationStatus = status,
            reservationRequestResult = requestResult
        )
    }

    private fun createRequestResult(
        requestStatus: ReservationRequestStatus
    ): ReservationRequestResult {
        return ReservationRequestResult(requestStatus, requestId = "dummy", timestamp = -1)
    }

    @Test
    fun starred_isPinned() {
        val userEvent = createTestEvent(isStarred = true, status = NONE)
        assertTrue(userEvent.isPinned())
    }

    @Test
    fun notStarred_waitlisted_isPinned() {
        val waitlisted = createTestEvent(isStarred = false, status = WAITLISTED)
        assertTrue(waitlisted.isPinned())
    }

    @Test
    fun notStarred_reserved_isPinned() {
        val reserved = createTestEvent(isStarred = false, status = RESERVED)
        assertTrue(reserved.isPinned())
    }

    @Test
    fun changedBySwap_isLastRequestResultBySwap() {
        val reservedUserEvent = createTestEvent(
            status = RESERVED,
            requestResult = createRequestResult(ReservationRequestStatus.SWAP_SUCCEEDED)
        )
        assertTrue(reservedUserEvent.isLastRequestResultBySwap())

        val noneUserEvent = createTestEvent(
            status = NONE,
            requestResult = createRequestResult(ReservationRequestStatus.SWAP_SUCCEEDED)
        )
        assertTrue(noneUserEvent.isLastRequestResultBySwap())

        val waitlistedUserEvent = createTestEvent(
            status = WAITLISTED,
            requestResult = createRequestResult(ReservationRequestStatus.SWAP_WAITLISTED)
        )
        assertTrue(waitlistedUserEvent.isLastRequestResultBySwap())
    }

    // TODO: Add tests for isReserved, isWaitlisted, etc.
}
