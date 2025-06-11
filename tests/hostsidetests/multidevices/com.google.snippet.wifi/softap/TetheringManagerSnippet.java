/*
 * Copyright (C) 2024 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.snippet.wifi.softap;

import android.content.Context;
import android.net.TetheringManager;
import android.net.TetheringManager.TetheringRequest;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;

import com.google.android.mobly.snippet.Snippet;
import com.google.android.mobly.snippet.event.EventCache;
import com.google.android.mobly.snippet.event.SnippetEvent;
import com.google.android.mobly.snippet.rpc.AsyncRpc;
import com.google.android.mobly.snippet.rpc.Rpc;

/** Snippet class for TetheringManager. */
public class TetheringManagerSnippet implements Snippet {
    private static final String TAG = "TetheringManagerSnippet";

    private final TetheringManager mTetheringManager;
    private final Handler mHandler;


    /** Callback class to get the results of tethering start. */
    private static class SnippetStartTetheringCallback implements
            TetheringManager.StartTetheringCallback {
        private final String mCallbackId;

        SnippetStartTetheringCallback(String callbackId) {
            mCallbackId = callbackId;
        }

        @Override
        public void onTetheringStarted() {
            Log.d(TAG, "onTetheringStarted");
            SnippetEvent event = new SnippetEvent(mCallbackId, "onTetheringStarted");
            EventCache.getInstance().postEvent(event);
        }

        @Override
        public void onTetheringFailed(final int error) {
            Log.d(TAG, "onTetheringFailed, error=" + error);
            SnippetEvent event = new SnippetEvent(mCallbackId, "onTetheringFailed");
            event.getData().putInt("error", error);
            EventCache.getInstance().postEvent(event);
        }
    }

    public TetheringManagerSnippet() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        mTetheringManager = context.getSystemService(TetheringManager.class);
        HandlerThread handlerThread = new HandlerThread(getClass().getSimpleName());
        handlerThread.start();
        mHandler = new Handler(handlerThread.getLooper());
    }

    /**
     * Starts tethering.
     *
     * @param callbackId A unique identifier assigned automatically by Mobly.
     */
    @AsyncRpc(description = "Call to start tethering.")
    public void tetheringStartTethering(String callbackId) {
        TetheringRequest request =
                new TetheringRequest.Builder(TetheringManager.TETHERING_WIFI).build();

        SnippetStartTetheringCallback callback = new SnippetStartTetheringCallback(callbackId);
        mTetheringManager.startTethering(request, mHandler::post, callback);
    }

    /**
     * Stop tethering.
     */
    @Rpc(description = "Call to stop tethering.")
    public void tetheringStopTethering() {
        mTetheringManager.stopTethering(TetheringManager.TETHERING_WIFI);
    }
}
