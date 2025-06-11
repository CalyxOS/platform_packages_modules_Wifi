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

package android.system.wifi.mainline_supplicant;

/**
 * Information for sending a USD message.
 */
parcelable UsdMessageInfo {
    /**
     * Identifier for this device, retrieved from |ServiceDiscoveryInfo|.
     */
    int ownId;

    /**
     * Identifier for the peer device, retrieved from |ServiceDiscoveryInfo|.
     */
    int peerId;

    /**
     * MAC address for the peer device.
     */
    byte[6] peerMacAddress;

    /**
     * Message contents. Note that the maximum message length is
     * |UsdCapabilities.maxLocalSsiLengthBytes|.
     */
    byte[] message;
}
