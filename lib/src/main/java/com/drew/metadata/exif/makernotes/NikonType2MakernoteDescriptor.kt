/*
 * Copyright 2002-2019 Drew Noakes and contributors
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 * More information about this project is available at:
 *
 *    https://drewnoakes.com/code/exif/
 *    https://github.com/drewnoakes/metadata-extractor
 */
package com.drew.metadata.exif.makernotes

import com.drew.metadata.TagDescriptor
import java.nio.ByteBuffer
import java.text.DecimalFormat
import kotlin.math.pow

/**
 * Provides human-readable string representations of tag values stored in a [NikonType2MakernoteDirectory].
 *
 * Type-2 applies to the E990 and D-series cameras such as the D1, D70 and D100.
 *
 * @author Drew Noakes https://drewnoakes.com
 */
class NikonType2MakernoteDescriptor(directory: NikonType2MakernoteDirectory) : TagDescriptor<NikonType2MakernoteDirectory>(directory) {
  override fun getDescription(tagType: Int): String? {
    return when (tagType) {
      NikonType2MakernoteDirectory.TAG_PROGRAM_SHIFT -> programShiftDescription
      NikonType2MakernoteDirectory.TAG_EXPOSURE_DIFFERENCE -> exposureDifferenceDescription
      NikonType2MakernoteDirectory.TAG_LENS -> lensDescription
      NikonType2MakernoteDirectory.TAG_CAMERA_HUE_ADJUSTMENT -> hueAdjustmentDescription
      NikonType2MakernoteDirectory.TAG_CAMERA_COLOR_MODE -> colorModeDescription
      NikonType2MakernoteDirectory.TAG_AUTO_FLASH_COMPENSATION -> autoFlashCompensationDescription
      NikonType2MakernoteDirectory.TAG_FLASH_EXPOSURE_COMPENSATION -> flashExposureCompensationDescription
      NikonType2MakernoteDirectory.TAG_FLASH_BRACKET_COMPENSATION -> flashBracketCompensationDescription
      NikonType2MakernoteDirectory.TAG_EXPOSURE_TUNING -> exposureTuningDescription
      NikonType2MakernoteDirectory.TAG_LENS_STOPS -> lensStopsDescription
      NikonType2MakernoteDirectory.TAG_COLOR_SPACE -> colorSpaceDescription
      NikonType2MakernoteDirectory.TAG_ACTIVE_D_LIGHTING -> activeDLightingDescription
      NikonType2MakernoteDirectory.TAG_VIGNETTE_CONTROL -> vignetteControlDescription
      NikonType2MakernoteDirectory.TAG_ISO_1 -> isoSettingDescription
      NikonType2MakernoteDirectory.TAG_DIGITAL_ZOOM -> digitalZoomDescription
      NikonType2MakernoteDirectory.TAG_FLASH_USED -> flashUsedDescription
      NikonType2MakernoteDirectory.TAG_AF_FOCUS_POSITION -> autoFocusPositionDescription
      NikonType2MakernoteDirectory.TAG_FIRMWARE_VERSION -> firmwareVersionDescription
      NikonType2MakernoteDirectory.TAG_LENS_TYPE -> lensTypeDescription
      NikonType2MakernoteDirectory.TAG_SHOOTING_MODE -> shootingModeDescription
      NikonType2MakernoteDirectory.TAG_NEF_COMPRESSION -> nEFCompressionDescription
      NikonType2MakernoteDirectory.TAG_HIGH_ISO_NOISE_REDUCTION -> highISONoiseReductionDescription
      NikonType2MakernoteDirectory.TAG_POWER_UP_TIME -> powerUpTimeDescription
      else -> super.getDescription(tagType)
    }
  }

  // this is generally a byte[] of length 8 directly representing a date and time.
  // the format is : first 2 bytes together are the year, and then each byte after
  //                 is month, day, hour, minute, second with the eighth byte unused
  // e.g., 2011:04:25 01:54:58
  val powerUpTimeDescription: String?
    get() { // this is generally a byte[] of length 8 directly representing a date and time.
      // the format is : first 2 bytes together are the year, and then each byte after
      //                 is month, day, hour, minute, second with the eighth byte unused
      // e.g., 2011:04:25 01:54:58
      val values = _directory.getByteArray(NikonType2MakernoteDirectory.TAG_POWER_UP_TIME)
      val year = ByteBuffer.wrap(byteArrayOf(values!![0], values[1])).short
      return "%04d:%02d:%02d %02d:%02d:%02d".format(year, values[2], values[3],
        values[4], values[5], values[6])
    }

  val highISONoiseReductionDescription: String?
    get() = getIndexedDescription(NikonType2MakernoteDirectory.TAG_HIGH_ISO_NOISE_REDUCTION,
      "Off",
      "Minimal",
      "Low",
      null,
      "Normal",
      null,
      "High"
    )

  val flashUsedDescription: String?
    get() = getIndexedDescription(NikonType2MakernoteDirectory.TAG_FLASH_USED,
      "Flash Not Used",
      "Manual Flash",
      null,
      "Flash Not Ready",
      null,
      null,
      null,
      "External Flash",
      "Fired, Commander Mode",
      "Fired, TTL Mode"
    )

  val nEFCompressionDescription: String?
    get() = getIndexedDescription(NikonType2MakernoteDirectory.TAG_NEF_COMPRESSION,
      1,
      "Lossy (Type 1)",
      null,
      "Uncompressed",
      null,
      null,
      null,
      "Lossless",
      "Lossy (Type 2)"
    )

  val shootingModeDescription: String?
    get() = getBitFlagDescription(NikonType2MakernoteDirectory.TAG_SHOOTING_MODE, arrayOf("Single Frame", "Continuous"),
      "Delay",
      null,
      "PC Control",
      "Exposure Bracketing",
      "Auto ISO",
      "White-Balance Bracketing",
      "IR Control"
    )

  val lensTypeDescription: String?
    get() = getBitFlagDescription(NikonType2MakernoteDirectory.TAG_LENS_TYPE, arrayOf("AF", "MF"),
      "D",
      "G",
      "VR"
    )

  val colorSpaceDescription: String?
    get() = getIndexedDescription(NikonType2MakernoteDirectory.TAG_COLOR_SPACE,
      1,
      "sRGB",
      "Adobe RGB"
    )

  val activeDLightingDescription: String?
    get() {
      val value = _directory.getInteger(NikonType2MakernoteDirectory.TAG_ACTIVE_D_LIGHTING) ?: return null
      return when (value) {
        0 -> "Off"
        1 -> "Light"
        3 -> "Normal"
        5 -> "High"
        7 -> "Extra High"
        65535 -> "Auto"
        else -> "Unknown ($value)"
      }
    }

  val vignetteControlDescription: String?
    get() {
      val value = _directory.getInteger(NikonType2MakernoteDirectory.TAG_VIGNETTE_CONTROL) ?: return null
      return when (value) {
        0 -> "Off"
        1 -> "Low"
        3 -> "Normal"
        5 -> "High"
        else -> "Unknown ($value)"
      }
    }

  val autoFocusPositionDescription: String?
    get() {
      val values = _directory.getIntArray(NikonType2MakernoteDirectory.TAG_AF_FOCUS_POSITION) ?: return null
      return if (values.size != 4 || values[0] != 0 || values[2] != 0 || values[3] != 0) {
        "Unknown (${_directory.getString(NikonType2MakernoteDirectory.TAG_AF_FOCUS_POSITION)})"
      } else when (values[1]) {
        0 -> "Centre"
        1 -> "Top"
        2 -> "Bottom"
        3 -> "Left"
        4 -> "Right"
        else -> "Unknown (${values[1]})"
      }
    }

  val digitalZoomDescription: String?
    get() {
      val value = _directory.getRational(NikonType2MakernoteDirectory.TAG_DIGITAL_ZOOM) ?: return null
      return if (value.toInt() == 1) "No digital zoom" else "${value.toSimpleString(true)}x digital zoom"
    }

  val programShiftDescription: String?
    get() = getEVDescription(NikonType2MakernoteDirectory.TAG_PROGRAM_SHIFT)

  val exposureDifferenceDescription: String?
    get() = getEVDescription(NikonType2MakernoteDirectory.TAG_EXPOSURE_DIFFERENCE)

  val autoFlashCompensationDescription: String?
    get() = getEVDescription(NikonType2MakernoteDirectory.TAG_AUTO_FLASH_COMPENSATION)

  val flashExposureCompensationDescription: String?
    get() = getEVDescription(NikonType2MakernoteDirectory.TAG_FLASH_EXPOSURE_COMPENSATION)

  val flashBracketCompensationDescription: String?
    get() = getEVDescription(NikonType2MakernoteDirectory.TAG_FLASH_BRACKET_COMPENSATION)

  val exposureTuningDescription: String?
    get() = getEVDescription(NikonType2MakernoteDirectory.TAG_EXPOSURE_TUNING)

  val lensStopsDescription: String?
    get() = getEVDescription(NikonType2MakernoteDirectory.TAG_LENS_STOPS)

  private fun getEVDescription(tagType: Int): String? {
    val values = _directory.getIntArray(tagType)
    if (values == null || values.size < 2) return null
    if (values.size < 3 || values[2] == 0) return null
    val decimalFormat = DecimalFormat("0.##")
    val ev = values[0] * values[1] / values[2].toDouble()
    return "${decimalFormat.format(ev)} EV"
  }

  val isoSettingDescription: String?
    get() {
      val values = _directory.getIntArray(NikonType2MakernoteDirectory.TAG_ISO_1) ?: return null
      return if (values[0] != 0 || values[1] == 0) "Unknown (${_directory.getString(NikonType2MakernoteDirectory.TAG_ISO_1)})" else "ISO ${values[1]}"
    }

  val lensDescription: String?
    get() = getLensSpecificationDescription(NikonType2MakernoteDirectory.TAG_LENS)

  val lensFocusDistance: String?
    get() {
      val values = _directory.getDecryptedIntArray(NikonType2MakernoteDirectory.TAG_LENS_DATA)
      return if (values == null || values.size < 11) null else "%.2fm".format(getDistanceInMeters(values[10]))
    }

  val hueAdjustmentDescription: String?
    get() = getFormattedString(NikonType2MakernoteDirectory.TAG_CAMERA_HUE_ADJUSTMENT, "%s degrees")

  val colorModeDescription: String?
    get() {
      val value = _directory.getString(NikonType2MakernoteDirectory.TAG_CAMERA_COLOR_MODE)
      return if (value == null) null else if (value.startsWith("MODE1")) "Mode I (sRGB)" else value
    }

  val firmwareVersionDescription: String?
    get() = getVersionBytesDescription(NikonType2MakernoteDirectory.TAG_FIRMWARE_VERSION, 2)

  private fun getDistanceInMeters(value: Int): Double {
    var value = value
    if (value < 0) {
      value += 256
    }
    return 0.01 * 10.0.pow(value / 40.0f.toDouble())
  }
}
