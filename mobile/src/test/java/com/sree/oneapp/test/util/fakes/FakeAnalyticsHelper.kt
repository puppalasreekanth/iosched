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

package com.sree.oneapp.test.util.fakes

import android.app.Activity
import com.sree.oneapp.shared.analytics.AnalyticsHelper

class FakeAnalyticsHelper : AnalyticsHelper {
    override fun sendScreenView(screenName: String, activity: Activity) {}

    override fun logUiEvent(itemId: String, action: String) {}

    override fun setUserSignedIn(isSignedIn: Boolean) {}

    override fun setUserRegistered(isRegistered: Boolean) {}
}
