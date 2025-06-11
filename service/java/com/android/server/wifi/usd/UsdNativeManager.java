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

package com.android.server.wifi.usd;

import android.net.MacAddress;
import android.net.wifi.usd.PublishConfig;
import android.net.wifi.usd.PublishSessionCallback;
import android.net.wifi.usd.SessionCallback;
import android.net.wifi.usd.SubscribeConfig;
import android.net.wifi.usd.SubscribeSessionCallback;

import com.android.server.wifi.SupplicantStaIfaceHal;
import com.android.server.wifi.WifiNative;

import java.util.concurrent.Executor;

/**
 * Manages the interface to the HAL.
 */
public class UsdNativeManager {
    private final WifiNative mWifiNative;

    /**
     * USD Events from HAL.
     */
    public interface UsdEventsCallback {
        /**
         * Called when publisher is started.
         */
        void onUsdPublishStarted(int cmdId, int publishId);

        /**
         * Called when subscribe is started.
         */
        void onUsdSubscribeStarted(int cmdId, int subscribeId);

        /**
         * Called when publisher is failed to start.
         */
        void onUsdPublishConfigFailed(int cmdId, @SessionCallback.FailureCode int errorCode);

        /**
         * Called when subscriber is failed to start.
         */
        void onUsdSubscribeConfigFailed(int cmdId, @SessionCallback.FailureCode int errorCode);

        /**
         * Called when publish session is terminated.
         */
        void onUsdPublishTerminated(int publishId,
                @SessionCallback.TerminationReasonCode int reasonCode);

        /**
         *  Called when subscribe session is terminated.
         */
        void onUsdSubscribeTerminated(int subscribeId,
                @SessionCallback.TerminationReasonCode int reasonCode);

        /**
         * Called for each Publish replied event.
         */
        void onUsdPublishReplied(
                UsdRequestManager.UsdHalDiscoveryInfo usdHalDiscoveryInfo);

        /**
         * Called when the subscriber discovers publisher.
         */
        void onUsdServiceDiscovered(
                UsdRequestManager.UsdHalDiscoveryInfo usdHalDiscoveryInfo);

        /**
         * Called when a message is received.
         */
        void onUsdMessageReceived(int ownId, int peerId, MacAddress peerMacAddress,
                byte[] message);
    }

    /**
     * Constructor
     */
    public UsdNativeManager(WifiNative wifiNative) {
        mWifiNative = wifiNative;
    }

    /**
     * Register USD events.
     */
    public void registerUsdEventsCallback(
            UsdRequestManager.UsdNativeEventsCallback usdNativeEventsCallback) {
        mWifiNative.registerUsdEventsCallback(usdNativeEventsCallback);
    }

    /**
     * Gets USD capabilities.
     */
    public SupplicantStaIfaceHal.UsdCapabilitiesInternal getUsdCapabilities() {
        return mWifiNative.getUsdCapabilities();
    }

    /**
     * See {@link android.net.wifi.usd.UsdManager#publish(PublishConfig, Executor,
     * PublishSessionCallback)}
     */
    public boolean publish(String interfaceName, int cmdId, PublishConfig publishConfig) {
        return mWifiNative.startUsdPublish(interfaceName, cmdId, publishConfig);
    }

    /**
     * See {@link android.net.wifi.usd.UsdManager#subscribe(SubscribeConfig, Executor,
     * SubscribeSessionCallback)}
     */
    public boolean subscribe(String interfaceName, int cmdId, SubscribeConfig subscribeConfig) {
        return mWifiNative.startUsdSubscribe(interfaceName, cmdId, subscribeConfig);
    }

    /**
     * Update publish.
     */
    public void updatePublish(String interfaceName, int publishId, byte[] ssi) {
        mWifiNative.updateUsdPublish(interfaceName, publishId, ssi);
    }

    /**
     * Cancels publish session identified by publishId.
     */
    public void cancelPublish(String interfaceName, int publishId) {
        mWifiNative.cancelUsdPublish(interfaceName, publishId);
    }

    /**
     * Cancels subscribe identified by subscribeId
     */
    public void cancelSubscribe(String interfaceName, int subscribeId) {
        mWifiNative.cancelUsdSubscribe(interfaceName, subscribeId);
    }

    /**
     * Send a message to the peer identified by the peerId and the peerMacAddress.
     */
    public boolean sendMessage(String interfaceName, int ownId, int peerId,
            MacAddress peerMacAddress, byte[] message) {
        return mWifiNative.sendUsdMessage(interfaceName, ownId, peerId, peerMacAddress, message);
    }
}
