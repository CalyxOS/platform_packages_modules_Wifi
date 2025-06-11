/*
 * Copyright (C) 2022 The Android Open Source Project
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

package com.android.server.wifi.hal;

import android.annotation.IntDef;
import android.annotation.NonNull;
import android.annotation.Nullable;
import android.hardware.wifi.Akm;
import android.hardware.wifi.CipherSuite;
import android.net.MacAddress;
import android.net.wifi.rtt.PasnConfig;
import android.net.wifi.rtt.RangingRequest;
import android.net.wifi.rtt.RangingResult;
import android.util.Log;

import java.io.PrintWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Wrapper around a WifiRttController.
 * May be initialized using a HIDL or AIDL WifiRttController.
 */
public class WifiRttController {
    private static final String TAG = "WifiRttController";
    protected static final int CONVERSION_US_TO_MS = 1_000;

    private IWifiRttController mWifiRttController;

    /** Unknown status */
    public static final int FRAMEWORK_RTT_STATUS_UNKNOWN = -1;
    /** Success */
    public static final int FRAMEWORK_RTT_STATUS_SUCCESS = 0;
    /** General failure status */
    public static final int FRAMEWORK_RTT_STATUS_FAILURE = 1;
    /** Target STA does not respond to request */
    public static final int FRAMEWORK_RTT_STATUS_FAIL_NO_RSP = 2;
    /** Request rejected. Applies to 2-sided RTT only */
    public static final int FRAMEWORK_RTT_STATUS_FAIL_REJECTED = 3;
    public static final int FRAMEWORK_RTT_STATUS_FAIL_NOT_SCHEDULED_YET = 4;
    /** Timing measurement times out */
    public static final int FRAMEWORK_RTT_STATUS_FAIL_TM_TIMEOUT = 5;
    /** Target on different channel, cannot range */
    public static final int FRAMEWORK_RTT_STATUS_FAIL_AP_ON_DIFF_CHANNEL = 6;
    /** Ranging not supported */
    public static final int FRAMEWORK_RTT_STATUS_FAIL_NO_CAPABILITY = 7;
    /** Request aborted for unknown reason */
    public static final int FRAMEWORK_RTT_STATUS_ABORTED = 8;
    /** Invalid T1-T4 timestamp */
    public static final int FRAMEWORK_RTT_STATUS_FAIL_INVALID_TS = 9;
    /** 11mc protocol failed */
    public static final int FRAMEWORK_RTT_STATUS_FAIL_PROTOCOL = 10;
    /** Request could not be scheduled */
    public static final int FRAMEWORK_RTT_STATUS_FAIL_SCHEDULE = 11;
    /** Responder cannot collaborate at time of request */
    public static final int FRAMEWORK_RTT_STATUS_FAIL_BUSY_TRY_LATER = 12;
    /** Bad request args */
    public static final int FRAMEWORK_RTT_STATUS_INVALID_REQ = 13;
    /** WiFi not enabled. */
    public static final int FRAMEWORK_RTT_STATUS_NO_WIFI = 14;
    /** Responder overrides param info, cannot range with new params */
    public static final int FRAMEWORK_RTT_STATUS_FAIL_FTM_PARAM_OVERRIDE = 15;

    /** @hide */
    @IntDef(prefix = "FRAMEWORK_RTT_STATUS_", value = {FRAMEWORK_RTT_STATUS_UNKNOWN,
            FRAMEWORK_RTT_STATUS_SUCCESS, FRAMEWORK_RTT_STATUS_FAILURE,
            FRAMEWORK_RTT_STATUS_FAIL_NO_RSP, FRAMEWORK_RTT_STATUS_FAIL_REJECTED,
            FRAMEWORK_RTT_STATUS_FAIL_NOT_SCHEDULED_YET, FRAMEWORK_RTT_STATUS_FAIL_TM_TIMEOUT,
            FRAMEWORK_RTT_STATUS_FAIL_AP_ON_DIFF_CHANNEL, FRAMEWORK_RTT_STATUS_FAIL_NO_CAPABILITY,
            FRAMEWORK_RTT_STATUS_ABORTED, FRAMEWORK_RTT_STATUS_FAIL_INVALID_TS,
            FRAMEWORK_RTT_STATUS_FAIL_PROTOCOL, FRAMEWORK_RTT_STATUS_FAIL_SCHEDULE,
            FRAMEWORK_RTT_STATUS_FAIL_BUSY_TRY_LATER, FRAMEWORK_RTT_STATUS_INVALID_REQ,
            FRAMEWORK_RTT_STATUS_NO_WIFI, FRAMEWORK_RTT_STATUS_FAIL_FTM_PARAM_OVERRIDE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface FrameworkRttStatus {}

    /**
     * Framework representation of RTT capabilities.
     */
    public static class Capabilities {
        // 1-sided rtt measurement is supported.
        public boolean oneSidedRttSupported;
        // Location configuration information supported.
        public boolean lciSupported;
        // Location civic records supported.
        public boolean lcrSupported;
        // Preamble supported, see bit mask definition above.
        public int preambleSupported;
        // RTT bandwidth supported.
        public int bwSupported;
        // Whether STA responder role is supported.
        public boolean responderSupported;
        // Draft 11mc version supported, including major and minor version. e.g., draft 4.3 is 43.
        public byte mcVersion;
        // Whether ftm rtt data collection is supported.
        public boolean rttFtmSupported;
        // IEEE 802.11az preamble supported, see bit mask definition above.
        public int azPreambleSupported;
        // IEE 802.11az RTT bandwidth supported.
        public int azBwSupported;
        // Whether IEEE 802.11az Non-Trigger-based (non-TB) responder mode is supported.
        public boolean ntbInitiatorSupported;
        // Whether IEEE 802.11az Non-Trigger-based (non-TB) responder mode is supported.
        public boolean ntbResponderSupported;
         // Bitmap of AKM values indicating the set of supported AKMs.
        public @PasnConfig.AkmType int akmsSupported;
         // Bitmap of cipher values indicating the set of supported pairwise cipher suites.
        public @PasnConfig.Cipher int cipherSuitesSupported;
        // Whether secure HE-LTF (Long Training Field) is supported
        public boolean secureHeLtfSupported;
        // Whether ranging frame protection is supported
        public boolean rangingFrameProtectionSupported;
        // Maximum supported secure HE-LTF protocol version
        public int maxSupportedSecureHeLtfProtocolVersion;

        public Capabilities() {
        }

        public Capabilities(android.hardware.wifi.V1_0.RttCapabilities rttHalCapabilities) {
            oneSidedRttSupported = rttHalCapabilities.rttOneSidedSupported;
            lciSupported = rttHalCapabilities.lciSupported;
            lcrSupported = rttHalCapabilities.lcrSupported;
            responderSupported = rttHalCapabilities.responderSupported;
            preambleSupported = rttHalCapabilities.preambleSupport;
            mcVersion = rttHalCapabilities.mcVersion;
            bwSupported = rttHalCapabilities.bwSupport;
            rttFtmSupported = rttHalCapabilities.rttFtmSupported;
        }

        public Capabilities(android.hardware.wifi.V1_4.RttCapabilities rttHalCapabilities) {
            oneSidedRttSupported = rttHalCapabilities.rttOneSidedSupported;
            lciSupported = rttHalCapabilities.lciSupported;
            lcrSupported = rttHalCapabilities.lcrSupported;
            responderSupported = rttHalCapabilities.responderSupported;
            preambleSupported = rttHalCapabilities.preambleSupport;
            mcVersion = rttHalCapabilities.mcVersion;
            bwSupported = rttHalCapabilities.bwSupport;
            rttFtmSupported = rttHalCapabilities.rttFtmSupported;
        }

        public Capabilities(android.hardware.wifi.V1_6.RttCapabilities rttHalCapabilities) {
            oneSidedRttSupported = rttHalCapabilities.rttOneSidedSupported;
            lciSupported = rttHalCapabilities.lciSupported;
            lcrSupported = rttHalCapabilities.lcrSupported;
            responderSupported = rttHalCapabilities.responderSupported;
            preambleSupported = rttHalCapabilities.preambleSupport;
            mcVersion = rttHalCapabilities.mcVersion;
            bwSupported = rttHalCapabilities.bwSupport;
            rttFtmSupported = rttHalCapabilities.rttFtmSupported;
        }

        public Capabilities(android.hardware.wifi.RttCapabilities rttHalCapabilities) {
            oneSidedRttSupported = rttHalCapabilities.rttOneSidedSupported;
            lciSupported = rttHalCapabilities.lciSupported;
            lcrSupported = rttHalCapabilities.lcrSupported;
            responderSupported = rttHalCapabilities.responderSupported;
            preambleSupported = rttHalCapabilities.preambleSupport;
            mcVersion = rttHalCapabilities.mcVersion;
            bwSupported = rttHalCapabilities.bwSupport;
            rttFtmSupported = rttHalCapabilities.rttFtmSupported;
            azPreambleSupported = rttHalCapabilities.azPreambleSupport;
            azBwSupported = rttHalCapabilities.azBwSupport;
            ntbInitiatorSupported = rttHalCapabilities.ntbInitiatorSupported;
            ntbResponderSupported = rttHalCapabilities.ntbResponderSupported;
            secureHeLtfSupported = rttHalCapabilities.secureHeLtfSupported;
            rangingFrameProtectionSupported = rttHalCapabilities.rangingFrameProtectionSupported;
            maxSupportedSecureHeLtfProtocolVersion =
                    rttHalCapabilities.maxSupportedSecureHeLtfProtocolVersion;
            akmsSupported = convertAkmsToFramework(rttHalCapabilities.akmsSupported);
            cipherSuitesSupported = convertCiphersToFramework(
                    rttHalCapabilities.cipherSuitesSupported);
        }
    }

    @PasnConfig.Cipher
    private static int convertCiphersToFramework(long ciphersSupported) {
        @PasnConfig.Cipher int ciphers = PasnConfig.CIPHER_NONE;
        if ((ciphersSupported & CipherSuite.GCMP_256) != 0) {
            ciphers |= PasnConfig.CIPHER_GCMP_256;
        }
        if ((ciphersSupported & CipherSuite.GCMP_128) != 0) {
            ciphers |= PasnConfig.CIPHER_GCMP_128;
        }
        if ((ciphersSupported & CipherSuite.CCMP_256) != 0) {
            ciphers |= PasnConfig.CIPHER_CCMP_256;
        }
        if ((ciphersSupported & CipherSuite.CCMP_128) != 0) {
            ciphers |= PasnConfig.CIPHER_CCMP_128;
        }
        return ciphers;
    }

    @PasnConfig.AkmType
    private static int convertAkmsToFramework(long akmsSupported) {
        @PasnConfig.AkmType int akms = PasnConfig.AKM_NONE;
        if ((akmsSupported & Akm.FT_EAP_SHA384) != 0) {
            akms |= PasnConfig.AKM_FT_EAP_SHA384;
        }
        if ((akmsSupported & Akm.FILS_EAP_SHA384) != 0) {
            akms |= PasnConfig.AKM_FILS_EAP_SHA384;
        }
        if ((akmsSupported & Akm.FILS_EAP_SHA256) != 0) {
            akms |= PasnConfig.AKM_FILS_EAP_SHA256;
        }
        if ((akmsSupported & Akm.FT_EAP_SHA256) != 0) {
            akms |= PasnConfig.AKM_FT_EAP_SHA256;
        }
        if ((akmsSupported & Akm.FT_PSK_SHA384) != 0) {
            akms |= PasnConfig.AKM_FT_PSK_SHA384;
        }
        if ((akmsSupported & Akm.FT_PSK_SHA256) != 0) {
            akms |= PasnConfig.AKM_FT_PSK_SHA256;
        }
        if ((akmsSupported & Akm.SAE) != 0) {
            akms |= PasnConfig.AKM_SAE;
        }
        if ((akmsSupported & Akm.PASN) != 0) {
            akms |= PasnConfig.AKM_PASN;
        }
        return akms;
    }



    /**
     * Callback to receive ranging results.
     */
    public interface RttControllerRangingResultsCallback {
        /**
         * Called when ranging results are received from the HAL.
         *
         * @param cmdId Command ID specified in the original request.
         * @param rangingResults A list of range results.
         */
        void onRangingResults(int cmdId, List<RangingResult> rangingResults);
    }

    public WifiRttController(@NonNull android.hardware.wifi.V1_0.IWifiRttController rttController) {
        mWifiRttController = createWifiRttControllerHidlImplMockable(rttController);
    }

    public WifiRttController(@NonNull android.hardware.wifi.IWifiRttController rttController) {
        mWifiRttController = createWifiRttControllerAidlImplMockable(rttController);
    }

    protected WifiRttControllerHidlImpl createWifiRttControllerHidlImplMockable(
            @NonNull android.hardware.wifi.V1_0.IWifiRttController rttController) {
        return new WifiRttControllerHidlImpl(rttController);
    }

    protected WifiRttControllerAidlImpl createWifiRttControllerAidlImplMockable(
            @NonNull android.hardware.wifi.IWifiRttController rttController) {
        return new WifiRttControllerAidlImpl(rttController);
    }

    private <T> T validateAndCall(String methodStr, T defaultVal, @NonNull Supplier<T> supplier) {
        if (mWifiRttController == null) {
            Log.wtf(TAG, "Cannot call " + methodStr + " because mWifiRttController is null");
            return defaultVal;
        }
        return supplier.get();
    }

    /**
     * See comments for {@link IWifiRttController#setup()}
     */
    public boolean setup() {
        return validateAndCall("setup", false,
                () -> mWifiRttController.setup());
    }

    /**
     * See comments for {@link IWifiRttController#enableVerboseLogging(boolean)}
     */
    public void enableVerboseLogging(boolean verbose) {
        if (mWifiRttController != null) {
            mWifiRttController.enableVerboseLogging(verbose);
        }
    }

    /**
     * See comments for {@link IWifiRttController#registerRangingResultsCallback(
     *                         RttControllerRangingResultsCallback)}
     */
    public void registerRangingResultsCallback(RttControllerRangingResultsCallback callback) {
        if (mWifiRttController != null) {
            mWifiRttController.registerRangingResultsCallback(callback);
        }
    }

    /**
     * See comments for {@link IWifiRttController#validate()}
     */
    public boolean validate() {
        return validateAndCall("validate", false,
                () -> mWifiRttController.validate());
    }

    /**
     * See comments for {@link IWifiRttController#getRttCapabilities()}
     */
    @Nullable
    public Capabilities getRttCapabilities() {
        return validateAndCall("getRttCapabilities", null,
                () -> mWifiRttController.getRttCapabilities());
    }

    /**
     * See comments for {@link IWifiRttController#rangeRequest(int, RangingRequest)}
     */
    public boolean rangeRequest(int cmdId, RangingRequest request) {
        return validateAndCall("rangeRequest", false,
                () -> mWifiRttController.rangeRequest(cmdId, request));
    }

    /**
     * See comments for {@link IWifiRttController#rangeCancel(int, List)}
     */
    public boolean rangeCancel(int cmdId, ArrayList<MacAddress> macAddresses) {
        return validateAndCall("rangeCancel", false,
                () -> mWifiRttController.rangeCancel(cmdId, macAddresses));
    }

    /**
     * See comments for {@link IWifiRttController#dump(PrintWriter)}
     */
    public void dump(PrintWriter pw) {
        if (mWifiRttController != null) {
            mWifiRttController.dump(pw);
        }
    }

    /**
     * Get optimum burst duration corresponding to a burst size.
     *
     * IEEE 802.11 spec, Section 11.21.6.3 Fine timing measurement procedure negotiation, burst
     * duration is defined as
     *
     * Burst duration = (N_FTMPB  * (K + 1)) – 1) * T_MDFTM + T_FTM + aSIFSTime + T_Ack, where
     *  - N_FTMPB is the value of the FTMs Per Burst subfield
     *  - K is the maximum number of Fine Timing Measurement frame retransmissions the
     *    responding STA might attempt
     *  - T_MDFTM is the duration indicated by the Min Delta FTM subfield of the Fine Timing
     *    Measurement Parameters field of the initial Fine Timing Measurement frame (FTM_1)
     *  - T_FTM is the duration of the initial Fine Timing Measurement frame if the FTMs Per Burst
     *    subfield of the Fine Timing Measurement Parameters field of FTM_1 is set to 1,
     *    and the duration of the non-initial Fine Timing Measurement frame otherwise
     *    T_Ack is the duration of the Ack frame expected as a response
     *
     * Since many of the parameters are dependent on the chip and the vendor software, framework is
     * doing a simple conversion with experimented values. Vendor Software may override the burst
     * duration with more optimal values.
     *
     * Section '9.4.2.167 Fine Timing Measurement Parameters element' defines Burst Duration
     * subfield encoding as,
     * +--------------------+
     * |Value|   Represents |
     * +--------------------+
     * | 0-1 |  Reserved    |
     * |  2  |    250 us    |
     * |  3  |    500 us    |
     * |  4  |      1 ms    |
     * |  5  |      2 ms    |
     * |  6  |      4 ms    |
     * |  7  |      8 ms    |
     * |  8  |     16 ms    |
     * |  9  |     32 ms    |
     * | 10  |     64 ms    |
     * | 11  |    128 ms    |
     * |12-14|  Reserved    |
     * | 15  | No Preference|
     * +-----+--------------+
     */
    public static int getOptimumBurstDuration(int burstSize) {
        if (burstSize <= 8) return 9; // 32 ms
        if (burstSize <= 24) return 10; // 64 ms
        return 11; // 128 ms
    }
}
