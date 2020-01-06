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
 * Provides human-readable String representations of tag values stored in a [OlympusImageProcessingMakernoteDirectory].
 *
 *
 * Some Description functions converted from Exiftool version 10.33 created by Phil Harvey
 * http://www.sno.phy.queensu.ca/~phil/exiftool/
 * lib\Image\ExifTool\Olympus.pm
 *
 * @author Kevin Mott https://github.com/kwhopper
 * @author Drew Noakes https://drewnoakes.com
 */
class OlympusImageProcessingMakernoteDescriptor(directory: OlympusImageProcessingMakernoteDirectory) : TagDescriptor<OlympusImageProcessingMakernoteDirectory>(directory) {
  override fun getDescription(tagType: Int): String? {
    return when (tagType) {
      OlympusImageProcessingMakernoteDirectory.TagImageProcessingVersion -> imageProcessingVersionDescription
      OlympusImageProcessingMakernoteDirectory.TagColorMatrix -> colorMatrixDescription
      OlympusImageProcessingMakernoteDirectory.TagNoiseReduction2 -> noiseReduction2Description
      OlympusImageProcessingMakernoteDirectory.TagDistortionCorrection2 -> distortionCorrection2Description
      OlympusImageProcessingMakernoteDirectory.TagShadingCompensation2 -> shadingCompensation2Description
      OlympusImageProcessingMakernoteDirectory.TagMultipleExposureMode -> multipleExposureModeDescription
      OlympusImageProcessingMakernoteDirectory.TagAspectRatio -> aspectRatioDescription
      OlympusImageProcessingMakernoteDirectory.TagKeystoneCompensation -> keystoneCompensationDescription
      OlympusImageProcessingMakernoteDirectory.TagKeystoneDirection -> keystoneDirectionDescription
      else -> super.getDescription(tagType)
    }
  }

  val imageProcessingVersionDescription: String?
    get() = getVersionBytesDescription(OlympusImageProcessingMakernoteDirectory.TagImageProcessingVersion, 4)

  val colorMatrixDescription: String?
    get() {
      val obj = _directory.getIntArray(OlympusImageProcessingMakernoteDirectory.TagColorMatrix) ?: return null
      val sb = StringBuilder()
      for (i in obj.indices) {
        if (i != 0) sb.append(" ")
        sb.append(obj[i])
      }
      return sb.toString()
    }

  val noiseReduction2Description: String?
    get() {
      val value = _directory.getInteger(OlympusImageProcessingMakernoteDirectory.TagNoiseReduction2) ?: return null
      if (value == 0) return "(none)"
      val sb = StringBuilder()
      val v = value.toInt()
      if (v and 1 != 0) sb.append("Noise Reduction, ")
      if (v shr 1 and 1 != 0) sb.append("Noise Filter, ")
      if (v shr 2 and 1 != 0) sb.append("Noise Filter (ISO Boost), ")
      return sb.substring(0, sb.length - 2)
    }

  val distortionCorrection2Description: String?
    get() = getIndexedDescription(OlympusImageProcessingMakernoteDirectory.TagDistortionCorrection2, "Off", "On")

  val shadingCompensation2Description: String?
    get() = getIndexedDescription(OlympusImageProcessingMakernoteDirectory.TagShadingCompensation2, "Off", "On")

  // check if it's only one value long also
  val multipleExposureModeDescription: String?
    get() {
      var values = _directory.getIntArray(OlympusImageProcessingMakernoteDirectory.TagMultipleExposureMode)
      if (values == null) { // check if it's only one value long also
        val value = _directory.getInteger(OlympusImageProcessingMakernoteDirectory.TagMultipleExposureMode)
          ?: return null
        values = IntArray(1)
        values[0] = value
      }
      if (values.isEmpty()) return null
      val sb = StringBuilder()
      when (values[0]) {
        0 -> sb.append("Off")
        2 -> sb.append("On (2 frames)")
        3 -> sb.append("On (3 frames)")
        else -> sb.append("Unknown (").append(values[0]).append(")")
      }
      if (values.size > 1) sb.append("; ").append(values[1])
      return sb.toString()
    }

  val aspectRatioDescription: String?
    get() {
      val values = _directory.getByteArray(OlympusImageProcessingMakernoteDirectory.TagAspectRatio)
      if (values == null || values.size < 2) return null
      val join = "%d %d".format(values[0], values[1])
      val ret: String
      ret = when (join) {
        "1 1" -> "4:3"
        "1 4" -> "1:1"
        "2 1" -> "3:2 (RAW)"
        "2 2" -> "3:2"
        "3 1" -> "16:9 (RAW)"
        "3 3" -> "16:9"
        "4 1" -> "1:1 (RAW)"
        "4 4" -> "6:6"
        "5 5" -> "5:4"
        "6 6" -> "7:6"
        "7 7" -> "6:5"
        "8 8" -> "7:5"
        "9 1" -> "3:4 (RAW)"
        "9 9" -> "3:4"
        else -> "Unknown ($join)"
      }
      return ret
    }

  val keystoneCompensationDescription: String?
    get() {
      val values = _directory.getByteArray(OlympusImageProcessingMakernoteDirectory.TagKeystoneCompensation)
      if (values == null || values.size < 2) return null
      val join = String.format("%d %d", values[0], values[1])
      val ret: String
      ret = when (join) {
        "0 0" -> "Off"
        "0 1" -> "On"
        else -> "Unknown ($join)"
      }
      return ret
    }

  val keystoneDirectionDescription: String?
    get() = getIndexedDescription(OlympusImageProcessingMakernoteDirectory.TagKeystoneDirection, "Vertical", "Horizontal")
}
