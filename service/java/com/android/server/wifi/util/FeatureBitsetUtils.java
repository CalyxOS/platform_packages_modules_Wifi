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

package com.android.server.wifi.util;

import android.net.wifi.WifiManager;
import android.util.SparseArray;

import java.util.BitSet;

/**
 * Utilities for formatting the WifiManager.FEATURE_ BitSet as a String.
 */
public class FeatureBitsetUtils {
    // All WifiManager.WIFI_FEATURE_ values should be added to the ALL_FEATURES map below
    // to keep the feature logging up to date.
    protected static final SparseArray ALL_FEATURES = new SparseArray() {
        {
            append(WifiManager.WIFI_FEATURE_INFRA, "WIFI_FEATURE_INFRA");
            append(WifiManager.WIFI_FEATURE_PASSPOINT, "WIFI_FEATURE_PASSPOINT");
            append(WifiManager.WIFI_FEATURE_P2P, "WIFI_FEATURE_P2P");
            append(WifiManager.WIFI_FEATURE_MOBILE_HOTSPOT, "WIFI_FEATURE_MOBILE_HOTSPOT");
            append(WifiManager.WIFI_FEATURE_SCANNER, "WIFI_FEATURE_SCANNER");
            append(WifiManager.WIFI_FEATURE_AWARE, "WIFI_FEATURE_AWARE");
            append(WifiManager.WIFI_FEATURE_D2D_RTT, "WIFI_FEATURE_D2D_RTT");
            append(WifiManager.WIFI_FEATURE_D2AP_RTT, "WIFI_FEATURE_D2AP_RTT");
            append(WifiManager.WIFI_FEATURE_PNO, "WIFI_FEATURE_PNO");
            append(WifiManager.WIFI_FEATURE_TDLS, "WIFI_FEATURE_TDLS");
            append(WifiManager.WIFI_FEATURE_TDLS_OFFCHANNEL, "WIFI_FEATURE_TDLS_OFFCHANNEL");
            append(WifiManager.WIFI_FEATURE_AP_STA, "WIFI_FEATURE_AP_STA");
            append(WifiManager.WIFI_FEATURE_LINK_LAYER_STATS, "WIFI_FEATURE_LINK_LAYER_STATS");
            append(WifiManager.WIFI_FEATURE_LOGGER, "WIFI_FEATURE_LOGGER");
            append(WifiManager.WIFI_FEATURE_RSSI_MONITOR, "WIFI_FEATURE_RSSI_MONITOR");
            append(WifiManager.WIFI_FEATURE_MKEEP_ALIVE, "WIFI_FEATURE_MKEEP_ALIVE");
            append(WifiManager.WIFI_FEATURE_CONFIG_NDO, "WIFI_FEATURE_CONFIG_NDO");
            append(WifiManager.WIFI_FEATURE_CONTROL_ROAMING, "WIFI_FEATURE_CONTROL_ROAMING");
            append(WifiManager.WIFI_FEATURE_IE_WHITELIST, "WIFI_FEATURE_IE_WHITELIST");
            append(WifiManager.WIFI_FEATURE_SCAN_RAND, "WIFI_FEATURE_SCAN_RAND");
            append(WifiManager.WIFI_FEATURE_TX_POWER_LIMIT, "WIFI_FEATURE_TX_POWER_LIMIT");
            append(WifiManager.WIFI_FEATURE_WPA3_SAE, "WIFI_FEATURE_WPA3_SAE");
            append(WifiManager.WIFI_FEATURE_WPA3_SUITE_B, "WIFI_FEATURE_WPA3_SUITE_B");
            append(WifiManager.WIFI_FEATURE_OWE, "WIFI_FEATURE_OWE");
            append(WifiManager.WIFI_FEATURE_LOW_LATENCY, "WIFI_FEATURE_LOW_LATENCY");
            append(WifiManager.WIFI_FEATURE_DPP, "WIFI_FEATURE_DPP");
            append(WifiManager.WIFI_FEATURE_P2P_RAND_MAC, "WIFI_FEATURE_P2P_RAND_MAC");
            append(WifiManager.WIFI_FEATURE_CONNECTED_RAND_MAC, "WIFI_FEATURE_CONNECTED_RAND_MAC");
            append(WifiManager.WIFI_FEATURE_AP_RAND_MAC, "WIFI_FEATURE_AP_RAND_MAC");
            append(WifiManager.WIFI_FEATURE_MBO, "WIFI_FEATURE_MBO");
            append(WifiManager.WIFI_FEATURE_OCE, "WIFI_FEATURE_OCE");
            append(WifiManager.WIFI_FEATURE_WAPI, "WIFI_FEATURE_WAPI");
            append(WifiManager.WIFI_FEATURE_FILS_SHA256, "WIFI_FEATURE_FILS_SHA256");
            append(WifiManager.WIFI_FEATURE_FILS_SHA384, "WIFI_FEATURE_FILS_SHA384");
            append(WifiManager.WIFI_FEATURE_SAE_PK, "WIFI_FEATURE_SAE_PK");
            append(WifiManager.WIFI_FEATURE_STA_BRIDGED_AP, "WIFI_FEATURE_STA_BRIDGED_AP");
            append(WifiManager.WIFI_FEATURE_BRIDGED_AP, "WIFI_FEATURE_BRIDGED_AP");
            append(WifiManager.WIFI_FEATURE_INFRA_60G, "WIFI_FEATURE_INFRA_60G");
            append(WifiManager.WIFI_FEATURE_ADDITIONAL_STA_LOCAL_ONLY,
                    "WIFI_FEATURE_ADDITIONAL_STA_LOCAL_ONLY");
            append(WifiManager.WIFI_FEATURE_ADDITIONAL_STA_MBB, "WIFI_FEATURE_ADDITIONAL_STA_MBB");
            append(WifiManager.WIFI_FEATURE_ADDITIONAL_STA_RESTRICTED,
                    "WIFI_FEATURE_ADDITIONAL_STA_RESTRICTED");
            append(WifiManager.WIFI_FEATURE_DPP_ENROLLEE_RESPONDER,
                    "WIFI_FEATURE_DPP_ENROLLEE_RESPONDER");
            append(WifiManager.WIFI_FEATURE_PASSPOINT_TERMS_AND_CONDITIONS,
                    "WIFI_FEATURE_PASSPOINT_TERMS_AND_CONDITIONS");
            append(WifiManager.WIFI_FEATURE_SAE_H2E, "WIFI_FEATURE_SAE_H2E");
            append(WifiManager.WIFI_FEATURE_WFD_R2, "WIFI_FEATURE_WFD_R2");
            append(WifiManager.WIFI_FEATURE_DECORATED_IDENTITY, "WIFI_FEATURE_DECORATED_IDENTITY");
            append(WifiManager.WIFI_FEATURE_TRUST_ON_FIRST_USE, "WIFI_FEATURE_TRUST_ON_FIRST_USE");
            append(WifiManager.WIFI_FEATURE_ADDITIONAL_STA_MULTI_INTERNET,
                    "WIFI_FEATURE_ADDITIONAL_STA_MULTI_INTERNET");
            append(WifiManager.WIFI_FEATURE_DPP_AKM, "WIFI_FEATURE_DPP_AKM");
            append(WifiManager.WIFI_FEATURE_SET_TLS_MINIMUM_VERSION,
                    "WIFI_FEATURE_SET_TLS_MINIMUM_VERSION");
            append(WifiManager.WIFI_FEATURE_TLS_V1_3, "WIFI_FEATURE_TLS_V1_3");
            append(WifiManager.WIFI_FEATURE_DUAL_BAND_SIMULTANEOUS,
                    "WIFI_FEATURE_DUAL_BAND_SIMULTANEOUS");
            append(WifiManager.WIFI_FEATURE_T2LM_NEGOTIATION, "WIFI_FEATURE_T2LM_NEGOTIATION");
            append(WifiManager.WIFI_FEATURE_WEP, "WIFI_FEATURE_WEP");
            append(WifiManager.WIFI_FEATURE_WPA_PERSONAL, "WIFI_FEATURE_WPA_PERSONAL");
            append(WifiManager.WIFI_FEATURE_AGGRESSIVE_ROAMING_MODE_SUPPORT,
                    "WIFI_FEATURE_AGGRESSIVE_ROAMING_MODE_SUPPORT");
            append(WifiManager.WIFI_FEATURE_D2D_WHEN_INFRA_STA_DISABLED,
                    "WIFI_FEATURE_D2D_WHEN_INFRA_STA_DISABLED");
            append(WifiManager.WIFI_FEATURE_SOFTAP_MLO, "WIFI_FEATURE_SOFTAP_MLO");
            append(WifiManager.WIFI_FEATURE_MULTIPLE_MLD_ON_SAP,
                    "WIFI_FEATURE_MULTIPLE_MLD_ON_SAP");
        }
    };

    // Index of the newest available feature. This will be calculated automatically
    // in the static block below.
    protected static final int NEWEST_FEATURE_INDEX;
    static {
        int newestFeatureIndex = 0;
        for (int i = 0; i < ALL_FEATURES.size(); i++) {
            newestFeatureIndex = Math.max(ALL_FEATURES.keyAt(i), newestFeatureIndex);
        }
        NEWEST_FEATURE_INDEX = newestFeatureIndex;
    }

    /**
     * Format a BitSet of WifiManager.WIFI_FEATURE_ features as a String.
     */
    public static String formatSupportedFeatures(BitSet supportedFeatures) {
        if (supportedFeatures == null || supportedFeatures.isEmpty()) return "[]";
        StringBuilder formatted = new StringBuilder("[");
        for (int i = 0; i < ALL_FEATURES.size(); i++) {
            int capabilityIndex = ALL_FEATURES.keyAt(i);
            if (supportedFeatures.get(capabilityIndex)) {
                String capabilityName = (String) ALL_FEATURES.valueAt(i);
                formatted.append(capabilityName);
                formatted.append(", ");
            }
        }

        // Include a warning if an unrecognized feature is supported. It may have been added
        // to WifiManager without updating this file.
        if (supportedFeatures.length() > NEWEST_FEATURE_INDEX + 1) {
            formatted.append("+ UNRECOGNIZED FEATURE(S)");
        } else {
            // Otherwise, trim the last 2 characters (", ") from the string
            formatted.setLength(formatted.length() - 2);
        }
        formatted.append("]");
        return formatted.toString();
    }
}
