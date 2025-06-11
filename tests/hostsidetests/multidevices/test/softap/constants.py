#  Copyright (C) 2024 The Android Open Source Project
#
#  Licensed under the Apache License, Version 2.0 (the "License");
#  you may not use this file except in compliance with the License.
#  You may obtain a copy of the License at
#
#       http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.

# Lint as: python3
"""Constants for SoftAp Mobly test."""

import datetime
import enum


# Timeout duration for receiving callbacks
CALLBACK_TIMEOUT = datetime.timedelta(seconds=10)
# Wi-Fi scan result interval
WIFI_SCAN_INTERVAL_SEC = datetime.timedelta(seconds=5)


@enum.unique
class LocalOnlyHotspotCallbackEventName(enum.StrEnum):
  """Event names for WifiManager#LocalOnlyHotspotCallback."""

  ON_STARTED = 'onStarted'
  ON_FAILED = 'onFailed'


@enum.unique
class LocalOnlyOnStartedDataKey(enum.StrEnum):
  """Data keys received from LocalOnlyHotspotCallback#onStarted."""

  SSID = 'ssid'
  PASSPHRASE = 'passphrase'


@enum.unique
class LocalOnlyOnFailedDataKey(enum.StrEnum):
  """Data keys received from LocalOnlyHotspotCallback#onFailed."""

  REASON = 'reason'


@enum.unique
class StartTetheringCallbackEventName(enum.StrEnum):
  """Event names for TetheringManager#StartTetheringCallback."""

  ON_TETHERING_STARTED = 'onTetheringStarted'
  ON_TETHERING_FAILED = 'onTetheringFailed'


@enum.unique
class TetheringOnTetheringFailedDataKey(enum.StrEnum):
  """Data keys received from the StartTetheringCallback#onTetheringFailed."""

  ERROR = 'error'


@enum.unique
class SoftApCallbackEventName(enum.StrEnum):
  """Event names for WifiManager#SoftApCallback."""

  ON_CONNECTED_CLIENTS_CHANGED = 'onConnectedClientsChanged'
  ON_CLIENTS_DISCONNECTED = 'onClientsDisconnected'


@enum.unique
class SoftApOnConnectedClientsChangedDataKey(enum.StrEnum):
  """Data keys received from SoftApCallback#onConnectedClientsChanged."""

  CONNECTED_CLIENTS_COUNT = 'connectedClientsCount'
  CLIENT_MAC_ADDRESS = 'clientMacAddress'


@enum.unique
class SoftApOnClientsDisconnectedDataKey(enum.StrEnum):
  """Data keys received from SoftApCallback#onClientsDisconnected."""

  DISCONNECTED_CLIENTS_COUNT = 'disconnectedClientsCount'
  CLIENT_MAC_ADDRESS = 'clientMacAddress'
