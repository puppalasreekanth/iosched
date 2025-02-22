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

package com.sree.oneapp.ui

/**
 * Fragments representing main navigation destinations (shown by [MainActivity]) implement this
 * interface.
 */
interface MainNavigationFragment {

    /**
     * Called by the hosting activity when the Back button is pressed.
     * @return True if the fragment handled the back press, false otherwise.
     */
    fun onBackPressed(): Boolean {
        return false
    }

    /** Called by the hosting activity when the user interacts with it. */
    fun onUserInteraction() {}
}
