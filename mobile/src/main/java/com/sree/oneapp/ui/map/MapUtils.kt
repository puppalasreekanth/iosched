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
import android.graphics.Bitmap
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.maps.android.data.geojson.GeoJsonLayer
import com.google.maps.android.data.geojson.GeoJsonPointStyle
import com.google.maps.android.ui.IconGenerator
import com.sree.oneapp.R
import java.util.Locale

/** Process a [GeoJsonLayer] for display on a Map. */
fun processGeoJsonLayer(layer: GeoJsonLayer, context: Context) {
    val iconGenerator = getLabelIconGenerator(context)
    layer.features.forEach { feature ->
        val id = feature.getProperty("id")
        val icon = feature.getProperty("icon")
        val title = feature.getProperty("title")

        val drawableRes = getDrawableResourceForIcon(context, icon)
        feature.pointStyle = when {
            drawableRes != 0 -> createIconMarker(context, drawableRes, title, id)
            title != null -> createLabelMarker(iconGenerator, title, id) // Fall back to title
            else -> GeoJsonPointStyle() // no styling
        }
    }
}

/** Creates a new IconGenerator for labels on the map. */
private fun getLabelIconGenerator(context: Context): IconGenerator {
    return IconGenerator(context).apply {
        setTextAppearance(context, R.style.TextApparance_IOSched_Map_Label)
        setBackground(null)
    }
}

/**
 * Returns the drawable resource id for an icon marker, or 0 if no resource with this name exists.
 */
@DrawableRes
fun getDrawableResourceForIcon(context: Context, iconType: String?): Int {
    if (iconType == null) {
        return 0
    }
    return context.resources.getIdentifier(
        iconType.toLowerCase(Locale.US),
        "drawable",
        context.packageName
    )
}

/** Creates a GeoJsonPointStyle for a label. */
private fun createLabelMarker(
    iconGenerator: IconGenerator,
    title: String,
    id: String
): GeoJsonPointStyle {
    val icon = BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon(title))
    // Note: We add the ID as the snippet since there's no other way to store metaata in the marker
    // and we need to look it up later when the user clicks on the marker.
    return GeoJsonPointStyle().apply {
        setAnchor(.5f, .5f)
        setTitle(title)
        snippet = id
        setIcon(icon)
        isVisible = true
    }
}

/**
 * Creates a GeoJsonPointStyle for a map icon. The icon is chosen based on the marker type and is
 * anchored at the bottom center of the marker's location.
 */
private fun createIconMarker(
    context: Context,
    drawableRes: Int,
    title: String,
    id: String
): GeoJsonPointStyle {
    val bitmap = drawableToBitmap(context, drawableRes)
    val icon = BitmapDescriptorFactory.fromBitmap(bitmap)
    // Note: We add the ID as the snippet since there's no other way to store metaata in the marker
    // and we need to look it up later when the user clicks on the marker.
    return GeoJsonPointStyle().apply {
        setAnchor(0.5f, 1f)
        setTitle(title)
        snippet = id
        setIcon(icon)
        isVisible = true
    }
}

/** Convert a drawable resource to a Bitmap. */
private fun drawableToBitmap(context: Context, @DrawableRes resId: Int): Bitmap {
    return requireNotNull(AppCompatResources.getDrawable(context, resId)).toBitmap()
}
