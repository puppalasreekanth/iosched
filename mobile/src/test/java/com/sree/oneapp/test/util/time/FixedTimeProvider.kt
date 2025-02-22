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

package com.sree.oneapp.test.util.time

import com.sree.oneapp.shared.time.DefaultTimeProvider
import com.sree.oneapp.shared.time.TimeProvider
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.threeten.bp.Instant

/**
 * Fix the TimeProvider to a fixed time
 */
class FixedTimeProvider(var instant: Instant) : TimeProvider {
    constructor(timeInMilis: Long) : this(Instant.ofEpochMilli(timeInMilis))

    override fun now(): Instant {
        return instant
    }
}

/**
 * Rule to be used in tests that sets the clocked used by DefaultTimeProvider.
 */
class FixedTimeExecutorRule(
    private val fixedTime: FixedTimeProvider = FixedTimeProvider(1_000_000)
) : TestWatcher() {

    var time: Instant
        get() = fixedTime.instant
        set(value) {
            fixedTime.instant = value
        }

    override fun starting(description: Description?) {
        super.starting(description)
        DefaultTimeProvider.setDelegate(fixedTime)
    }

    override fun finished(description: Description?) {
        super.finished(description)
        DefaultTimeProvider.setDelegate(null)
    }
}
