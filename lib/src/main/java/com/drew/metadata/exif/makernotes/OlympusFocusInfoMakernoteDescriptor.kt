/*
 * Copyright 2002-2015 Drew Noakes
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

/**
 * Provides human-readable String representations of tag values stored in a [OlympusFocusInfoMakernoteDirectory].
 *
 *
 * Some Description functions converted from Exiftool version 10.10 created by Phil Harvey
 * http://www.sno.phy.queensu.ca/~phil/exiftool/
 * lib\Image\ExifTool\Olympus.pm
 *
 * @author Kevin Mott https://github.com/kwhopper
 * @author Drew Noakes https://drewnoakes.com
 */
class OlympusFocusInfoMakernoteDescriptor(directory: OlympusFocusInfoMakernoteDirectory) : TagDescriptor<OlympusFocusInfoMakernoteDirectory>(directory) {
  override fun getDescription(tagType: Int): String? {
    return when (tagType) {
      OlympusFocusInfoMakernoteDirectory.TagFocusInfoVersion -> focusInfoVersionDescription
      OlympusFocusInfoMakernoteDirectory.TagAutoFocus -> autoFocusDescription
      OlympusFocusInfoMakernoteDirectory.TagFocusDistance -> focusDistanceDescription
      OlympusFocusInfoMakernoteDirectory.TagAfPoint -> afPointDescription
      OlympusFocusInfoMakernoteDirectory.TagExternalFlash -> externalFlashDescription
      OlympusFocusInfoMakernoteDirectory.TagExternalFlashBounce -> externalFlashBounceDescription
      OlympusFocusInfoMakernoteDirectory.TagExternalFlashZoom -> externalFlashZoomDescription
      OlympusFocusInfoMakernoteDirectory.TagManualFlash -> manualFlashDescription
      OlympusFocusInfoMakernoteDirectory.TagMacroLed -> macroLedDescription
      OlympusFocusInfoMakernoteDirectory.TagSensorTemperature -> sensorTemperatureDescription
      OlympusFocusInfoMakernoteDirectory.TagImageStabilization -> imageStabilizationDescription
      else -> super.getDescription(tagType)
    }
  }

  val focusInfoVersionDescription: String?
    get() = getVersionBytesDescription(OlympusFocusInfoMakernoteDirectory.TagFocusInfoVersion, 4)

  val autoFocusDescription: String?
    get() = getIndexedDescription(OlympusFocusInfoMakernoteDirectory.TagAutoFocus,
      "Off", "On")

  val focusDistanceDescription: String?
    get() {
      val (numerator) = _directory.getRational(OlympusFocusInfoMakernoteDirectory.TagFocusDistance) ?: return "inf"
      return when (numerator) {
        0xFFFFFFFFL, 0x00000000L -> "inf"
        else -> (numerator / 1000.0).toString() + " m"
      }
    }

  val afPointDescription: String?
    get() {
      val value = _directory.getInteger(OlympusFocusInfoMakernoteDirectory.TagAfPoint) ?: return null
      return value.toString()
    }

  val externalFlashDescription: String?
    get() {
      val values = _directory.getIntArray(OlympusFocusInfoMakernoteDirectory.TagExternalFlash)
      if (values == null || values.size < 2) return null
      return when (val join = "%d %d".format(values[0].toShort(), values[1].toShort())) {
        "0 0" -> "Off"
        "1 0" -> "On"
        else -> "Unknown ($join)"
      }
    }

  val externalFlashBounceDescription: String?
    get() = getIndexedDescription(OlympusFocusInfoMakernoteDirectory.TagExternalFlashBounce,
      "Bounce or Off", "Direct")

  // check if it's only one value long also
  val externalFlashZoomDescription: String?
    get() {
      var values = _directory.getIntArray(OlympusFocusInfoMakernoteDirectory.TagExternalFlashZoom)
      if (values == null) { // check if it's only one value long also
        val value = _directory.getInteger(OlympusFocusInfoMakernoteDirectory.TagExternalFlashZoom) ?: return null
        values = IntArray(1)
        values[0] = value
      }
      if (values.isEmpty()) return null
      var join = "%d".format(values[0].toShort())
      if (values.size > 1) join += " " + "%d".format(values[1].toShort())
      return when (join) {
        "0" -> "Off"
        "1" -> "On"
        "0 0" -> "Off"
        "1 0" -> "On"
        else -> "Unknown ($join)"
      }
    }

  val manualFlashDescription: String?
    get() {
      val values = _directory.getIntArray(OlympusFocusInfoMakernoteDirectory.TagManualFlash) ?: return null
      if (values[0] == 0) return "Off"
      if (values[1] == 1) return "Full"
      return "On (1/" + values[1].toShort() + " strength)"
    }

  val macroLedDescription: String?
    get() = getIndexedDescription(OlympusFocusInfoMakernoteDirectory.TagMacroLed,
      "Off", "On")

  /// <remarks>
/// <para>TODO: Complete when Camera Model is available.</para>
/// <para>There are differences in how to interpret this tag that can only be reconciled by knowing the model.</para>
/// </remarks>
  val sensorTemperatureDescription: String?
    get() = _directory.getString(OlympusFocusInfoMakernoteDirectory.TagSensorTemperature)

  val imageStabilizationDescription: String?
    get() {
      val values = _directory.getByteArray(OlympusFocusInfoMakernoteDirectory.TagImageStabilization) ?: return null
      return if (values[0].toInt() or values[1].toInt() or values[2].toInt() or values[3].toInt() == 0x0) "Off" else "On, " + if (values[43].toInt() and 1 > 0) "Mode 1" else "Mode 2"
    }
}
