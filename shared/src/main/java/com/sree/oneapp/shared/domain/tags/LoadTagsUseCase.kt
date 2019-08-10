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
import com.sree.oneapp.shared.domain.UseCase
import javax.inject.Inject

/**
 * Loads tags from a repository. Sub classes should manipulate the tags' order for their use case.
 */
open class LoadTagsUseCase @Inject constructor(private val repository: TagRepository) :
    UseCase<Unit, List<Tag>>() {

    override fun execute(parameters: Unit) = repository.getTags()
}
