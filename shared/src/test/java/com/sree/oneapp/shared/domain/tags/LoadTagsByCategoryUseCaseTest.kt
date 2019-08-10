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

package com.sree.oneapp.shared.domain.tags

import com.sree.oneapp.model.Tag
import com.sree.oneapp.shared.data.tag.TagRepository
import com.sree.oneapp.shared.model.TestDataRepository
import com.sree.oneapp.shared.result.Result
import com.sree.oneapp.test.data.TestData.advancedTag
import com.sree.oneapp.test.data.TestData.androidTag
import com.sree.oneapp.test.data.TestData.beginnerTag
import com.sree.oneapp.test.data.TestData.cloudTag
import com.sree.oneapp.test.data.TestData.codelabsTag
import com.sree.oneapp.test.data.TestData.intermediateTag
import com.sree.oneapp.test.data.TestData.sessionsTag
import com.sree.oneapp.test.data.TestData.webTag
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Unit tests for [LoadTagsByCategoryUseCase]
 */
class LoadTagsByCategoryUseCaseTest {

    @Test
    fun returnsOrderedTags() {
        val useCase = LoadTagsByCategoryUseCase(TagRepository(TestDataRepository))
        val tags = useCase.executeNow(Unit) as Result.Success<List<Tag>>

        // Expected values to assert
        val expected = listOf(
            // category = LEVEL
            beginnerTag, intermediateTag, advancedTag,
            // category = TRACK
            androidTag, cloudTag, webTag,
            // category = TYPE
            sessionsTag, codelabsTag
        )

        assertEquals(expected, tags.data)
    }
}
