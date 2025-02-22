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

import android.view.View
import androidx.annotation.DimenRes
import androidx.annotation.RawRes
import androidx.databinding.BindingAdapter
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.TileOverlayOptions
import com.google.android.gms.maps.model.TileProvider
import com.google.maps.android.data.geojson.GeoJsonLayer
import com.sree.oneapp.shared.result.Event
import com.sree.oneapp.util.getFloat
import com.sree.oneapp.widget.BottomSheetBehavior

@BindingAdapter("mapStyle")
fun mapStyle(mapView: MapView, @RawRes resId: Int) {
    if (resId != 0) {
        mapView.getMapAsync { map ->
            map.setMapStyle(MapStyleOptions.loadRawResourceStyle(mapView.context, resId))
        }
    }
}

/**
 * Adds list of markers to the GoogleMap.
 */
@BindingAdapter("mapMarkers")
fun mapMarkers(mapView: MapView, geoJsonLayer: GeoJsonLayer?) {
    geoJsonLayer?.addLayerToMap()
}

/**
 * Sets the map viewport to a specific rectangle specified by two Latitude/Longitude points.
 */
@BindingAdapter("mapViewport")
fun mapViewport(mapView: MapView, bounds: LatLngBounds?) {
    if (bounds != null) {
        mapView.getMapAsync {
            it.setLatLngBoundsForCameraTarget(bounds)
        }
    }
}

/**
 * Sets the center of the map's camera. Call this every time the user selects a marker.
 */
@BindingAdapter("mapCenter")
fun mapCenter(mapView: MapView, event: Event<CameraUpdate>?) {
    val update = event?.getContentIfNotHandled() ?: return
    mapView.getMapAsync {
        it.animateCamera(update)
    }
}

/**
 * Sets the minimum zoom level of the map (how far out the user is allowed to zoom).
 */
@BindingAdapter("mapMinZoom")
fun mapMinZoom(mapView: MapView, @DimenRes resId: Int) {
    val minZoom = mapView.resources.getFloat(resId)
    mapView.getMapAsync {
        it.setMinZoomPreference(minZoom)
    }
}

@BindingAdapter("isIndoorEnabled")
fun isIndoorEnabled(mapView: MapView, isIndoorEnabled: Boolean?) {
    if (isIndoorEnabled != null) {
        mapView.getMapAsync {
            it.isIndoorEnabled = isIndoorEnabled
        }
    }
}

@BindingAdapter("isMapToolbarEnabled")
fun isMapToolbarEnabled(mapView: MapView, isMapToolbarEnabled: Boolean?) {
    if (isMapToolbarEnabled != null) {
        mapView.getMapAsync {
            it.uiSettings.isMapToolbarEnabled = isMapToolbarEnabled
        }
    }
}

@BindingAdapter("mapTileProvider")
fun mapTileDrawable(mapView: MapView, tileProvider: TileProvider?) {
    if (tileProvider != null) {
        mapView.getMapAsync { map ->
            map.addTileOverlay(
                TileOverlayOptions()
                    .tileProvider(tileProvider)
                    .visible(true)
            )
        }
    }
}

@BindingAdapter("bottomSheetState")
fun bottomSheetState(view: View, event: Event<Int>?) {
    val state = event?.getContentIfNotHandled() ?: return
    BottomSheetBehavior.from(view).state = state
}
