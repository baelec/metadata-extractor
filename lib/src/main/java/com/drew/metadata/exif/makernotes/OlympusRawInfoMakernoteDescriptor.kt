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

import com.drew.lang.Rational
import com.drew.metadata.TagDescriptor

/**
 * Provides human-readable String representations of tag values stored in a [OlympusRawInfoMakernoteDirectory].
 *
 *
 * Some Description functions converted from Exiftool version 10.33 created by Phil Harvey
 * http://www.sno.phy.queensu.ca/~phil/exiftool/
 * lib\Image\ExifTool\Olympus.pm
 *
 * @author Kevin Mott https://github.com/kwhopper
 * @author Drew Noakes https://drewnoakes.com
 */
class OlympusRawInfoMakernoteDescriptor(directory: OlympusRawInfoMakernoteDirectory) : TagDescriptor<OlympusRawInfoMakernoteDirectory>(directory) {
  override fun getDescription(tagType: Int): String? {
    return when (tagType) {
      OlympusRawInfoMakernoteDirectory.TagRawInfoVersion -> getVersionBytesDescription(OlympusRawInfoMakernoteDirectory.TagRawInfoVersion, 4)
      OlympusRawInfoMakernoteDirectory.TagColorMatrix2 -> colorMatrix2Description
      OlympusRawInfoMakernoteDirectory.TagYCbCrCoefficients -> yCbCrCoefficientsDescription
      OlympusRawInfoMakernoteDirectory.TagLightSource -> olympusLightSourceDescription
      else -> super.getDescription(tagType)
    }
  }

  val colorMatrix2Description: String?
    get() {
      val values = _directory.getIntArray(OlympusRawInfoMakernoteDirectory.TagColorMatrix2) ?: return null
      val string = values.joinToString(" ")

      return if (string.isEmpty()) {
        null
      } else {
        string
      }
    }

  val yCbCrCoefficientsDescription: String?
    get() {
      val values = _directory.getIntArray(OlympusRawInfoMakernoteDirectory.TagYCbCrCoefficients) ?: return null
      return (0 until values.size / 2)
        .map { Rational(values[2 * it], values[2 * it + 1]).toDouble() }
        .joinToString(" ")
        .takeUnless { it.isEmpty() }
    }

  val olympusLightSourceDescription: String?
    get() {
      val value = _directory.getInteger(OlympusRawInfoMakernoteDirectory.TagLightSource) ?: return null
      return when (value) {
        0 -> "Unknown"
        16 -> "Shade"
        17 -> "Cloudy"
        18 -> "Fine Weather"
        20 -> "Tungsten (Incandescent)"
        22 -> "Evening Sunlight"
        33 -> "Daylight Fluorescent"
        34 -> "Day White Fluorescent"
        35 -> "Cool White Fluorescent"
        36 -> "White Fluorescent"
        256 -> "One Touch White Balance"
        512 -> "Custom 1-4"
        else -> "Unknown ($value)"
      }
    }
}
