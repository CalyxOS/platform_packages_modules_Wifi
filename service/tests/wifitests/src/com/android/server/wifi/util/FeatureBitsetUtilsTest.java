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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import android.net.wifi.WifiManager;

import org.junit.Test;

import java.util.BitSet;

/**
 * Unit tests for {@link FeatureBitsetUtils}
 */
public class FeatureBitsetUtilsTest {
    /**
     * Verify that formatting a null or empty BitSet produces the default value.
     */
    @Test
    public void testNoFeatures() {
        assertEquals("[]", FeatureBitsetUtils.formatSupportedFeatures(null));
        assertEquals("[]", FeatureBitsetUtils.formatSupportedFeatures(new BitSet()));
    }

    /**
     * Verify that a simple BitSet can be formatted successfully.
     */
    @Test
    public void testSuccessfulFormatting() {
        BitSet features = new BitSet();
        features.set(WifiManager.WIFI_FEATURE_AP_STA);
        features.set(WifiManager.WIFI_FEATURE_AWARE);
        String formatted = FeatureBitsetUtils.formatSupportedFeatures(features);
        assertTrue(formatted.contains("WIFI_FEATURE_AP_STA"));
        assertTrue(formatted.contains("WIFI_FEATURE_AWARE"));
    }

    /**
     * Verify that the newest feature is formatted successfully.
     */
    @Test
    public void testNewestFeatureFormatting() {
        BitSet features = new BitSet();
        features.set(FeatureBitsetUtils.NEWEST_FEATURE_INDEX);
        String formatted = FeatureBitsetUtils.formatSupportedFeatures(features);
        String newestFeatureName = (String) FeatureBitsetUtils.ALL_FEATURES.get(
                FeatureBitsetUtils.NEWEST_FEATURE_INDEX);
        assertTrue(formatted.contains(newestFeatureName));
    }

    /**
     * Verify that an unrecognized feature produces the expected warning text.
     */
    @Test
    public void testUnrecognizedFeature() {
        BitSet features = new BitSet();
        features.set(FeatureBitsetUtils.NEWEST_FEATURE_INDEX + 1);
        String formatted = FeatureBitsetUtils.formatSupportedFeatures(features);
        assertTrue(formatted.contains("UNRECOGNIZED FEATURE"));
    }
}
