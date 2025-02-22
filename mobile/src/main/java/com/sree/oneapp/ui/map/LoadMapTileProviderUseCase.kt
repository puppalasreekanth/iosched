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

package com.sree.oneapp.ui.map

import android.content.Context
import com.google.android.gms.maps.model.TileProvider
import com.sree.oneapp.shared.domain.UseCase
import javax.inject.Inject

class LoadMapTileProviderUseCase @Inject constructor(
    private val context: Context
) : UseCase<Int, TileProvider>() {

    companion object {
        private const val DPI_SCALE_1X = 160f
    }

    override fun execute(parameters: Int): TileProvider {
        val dpi = context.resources.displayMetrics.densityDpi / DPI_SCALE_1X
        val drawable = context.getDrawable(parameters)
        return DrawableTileProvider(dpi, drawable)
    }
}
