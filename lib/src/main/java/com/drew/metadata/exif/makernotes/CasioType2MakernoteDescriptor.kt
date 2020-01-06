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

/**
 * Provides human-readable string representations of tag values stored in a [CasioType2MakernoteDirectory].
 *
 * @author Drew Noakes https://drewnoakes.com
 */
class CasioType2MakernoteDescriptor(directory: CasioType2MakernoteDirectory) : TagDescriptor<CasioType2MakernoteDirectory>(directory) {
  override fun getDescription(tagType: Int): String? {
    return when (tagType) {
      CasioType2MakernoteDirectory.TAG_THUMBNAIL_DIMENSIONS -> thumbnailDimensionsDescription
      CasioType2MakernoteDirectory.TAG_THUMBNAIL_SIZE -> thumbnailSizeDescription
      CasioType2MakernoteDirectory.TAG_THUMBNAIL_OFFSET -> thumbnailOffsetDescription
      CasioType2MakernoteDirectory.TAG_QUALITY_MODE -> qualityModeDescription
      CasioType2MakernoteDirectory.TAG_IMAGE_SIZE -> imageSizeDescription
      CasioType2MakernoteDirectory.TAG_FOCUS_MODE_1 -> focusMode1Description
      CasioType2MakernoteDirectory.TAG_ISO_SENSITIVITY -> isoSensitivityDescription
      CasioType2MakernoteDirectory.TAG_WHITE_BALANCE_1 -> whiteBalance1Description
      CasioType2MakernoteDirectory.TAG_FOCAL_LENGTH -> focalLengthDescription
      CasioType2MakernoteDirectory.TAG_SATURATION -> saturationDescription
      CasioType2MakernoteDirectory.TAG_CONTRAST -> contrastDescription
      CasioType2MakernoteDirectory.TAG_SHARPNESS -> sharpnessDescription
      CasioType2MakernoteDirectory.TAG_PREVIEW_THUMBNAIL -> casioPreviewThumbnailDescription
      CasioType2MakernoteDirectory.TAG_WHITE_BALANCE_BIAS -> whiteBalanceBiasDescription
      CasioType2MakernoteDirectory.TAG_WHITE_BALANCE_2 -> whiteBalance2Description
      CasioType2MakernoteDirectory.TAG_OBJECT_DISTANCE -> objectDistanceDescription
      CasioType2MakernoteDirectory.TAG_FLASH_DISTANCE -> flashDistanceDescription
      CasioType2MakernoteDirectory.TAG_RECORD_MODE -> recordModeDescription
      CasioType2MakernoteDirectory.TAG_SELF_TIMER -> selfTimerDescription
      CasioType2MakernoteDirectory.TAG_QUALITY -> qualityDescription
      CasioType2MakernoteDirectory.TAG_FOCUS_MODE_2 -> focusMode2Description
      CasioType2MakernoteDirectory.TAG_TIME_ZONE -> timeZoneDescription
      CasioType2MakernoteDirectory.TAG_CCD_ISO_SENSITIVITY -> ccdIsoSensitivityDescription
      CasioType2MakernoteDirectory.TAG_COLOUR_MODE -> colourModeDescription
      CasioType2MakernoteDirectory.TAG_ENHANCEMENT -> enhancementDescription
      CasioType2MakernoteDirectory.TAG_FILTER -> filterDescription
      else -> super.getDescription(tagType)
    }
  }

  val filterDescription: String?
    get() = getIndexedDescription(CasioType2MakernoteDirectory.TAG_FILTER, "Off")

  val enhancementDescription: String?
    get() = getIndexedDescription(CasioType2MakernoteDirectory.TAG_ENHANCEMENT, "Off")

  val colourModeDescription: String?
    get() = getIndexedDescription(CasioType2MakernoteDirectory.TAG_COLOUR_MODE, "Off")

  val ccdIsoSensitivityDescription: String?
    get() = getIndexedDescription(CasioType2MakernoteDirectory.TAG_CCD_ISO_SENSITIVITY, "Off", "On")

  val timeZoneDescription: String?
    get() = _directory.getString(CasioType2MakernoteDirectory.TAG_TIME_ZONE)

  val focusMode2Description: String?
    get() {
      val value = _directory.getInteger(CasioType2MakernoteDirectory.TAG_FOCUS_MODE_2) ?: return null
      return when (value) {
        1 -> "Fixation"
        6 -> "Multi-Area Focus"
        else -> "Unknown ($value)"
      }
    }

  val qualityDescription: String?
    get() = getIndexedDescription(CasioType2MakernoteDirectory.TAG_QUALITY, 3, "Fine")

  val selfTimerDescription: String?
    get() = getIndexedDescription(CasioType2MakernoteDirectory.TAG_SELF_TIMER, 1, "Off")

  val recordModeDescription: String?
    get() = getIndexedDescription(CasioType2MakernoteDirectory.TAG_RECORD_MODE, 2, "Normal")

  val flashDistanceDescription: String?
    get() = getIndexedDescription(CasioType2MakernoteDirectory.TAG_FLASH_DISTANCE, "Off")

  val objectDistanceDescription: String?
    get() {
      val value = _directory.getInteger(CasioType2MakernoteDirectory.TAG_OBJECT_DISTANCE) ?: return null
      return Integer.toString(value) + " mm"
    }

  // unsure about this
  // unsure about this
  val whiteBalance2Description: String?
    get() {
      val value = _directory.getInteger(CasioType2MakernoteDirectory.TAG_WHITE_BALANCE_2) ?: return null
      return when (value) {
        0 -> "Manual"
        1 -> "Auto" // unsure about this
        4 -> "Flash" // unsure about this
        12 -> "Flash"
        else -> "Unknown ($value)"
      }
    }

  val whiteBalanceBiasDescription: String?
    get() = _directory.getString(CasioType2MakernoteDirectory.TAG_WHITE_BALANCE_BIAS)

  val casioPreviewThumbnailDescription: String?
    get() {
      val bytes = _directory.getByteArray(CasioType2MakernoteDirectory.TAG_PREVIEW_THUMBNAIL) ?: return null
      return "<${bytes.size} bytes of image data>"
    }

  val sharpnessDescription: String?
    get() = getIndexedDescription(CasioType2MakernoteDirectory.TAG_SHARPNESS, "-1", "Normal", "+1")

  val contrastDescription: String?
    get() = getIndexedDescription(CasioType2MakernoteDirectory.TAG_CONTRAST, "-1", "Normal", "+1")

  val saturationDescription: String?
    get() = getIndexedDescription(CasioType2MakernoteDirectory.TAG_SATURATION, "-1", "Normal", "+1")

  val focalLengthDescription: String?
    get() {
      val value = _directory.getDoubleObject(CasioType2MakernoteDirectory.TAG_FOCAL_LENGTH)
      return if (value == null) null else getFocalLengthDescription(value / 10.0)
    }

  val whiteBalance1Description: String?
    get() = getIndexedDescription(
      CasioType2MakernoteDirectory.TAG_WHITE_BALANCE_1,
      "Auto",
      "Daylight",
      "Shade",
      "Tungsten",
      "Florescent",
      "Manual"
    )

  val isoSensitivityDescription: String?
    get() {
      val value = _directory.getInteger(CasioType2MakernoteDirectory.TAG_ISO_SENSITIVITY) ?: return null
      return when (value) {
        3 -> "50"
        4 -> "64"
        6 -> "100"
        9 -> "200"
        else -> "Unknown ($value)"
      }
    }

  val focusMode1Description: String?
    get() = getIndexedDescription(CasioType2MakernoteDirectory.TAG_FOCUS_MODE_1, "Normal", "Macro")

  val imageSizeDescription: String?
    get() {
      val value = _directory.getInteger(CasioType2MakernoteDirectory.TAG_IMAGE_SIZE) ?: return null
      return when (value) {
        0 -> "640 x 480 pixels"
        4 -> "1600 x 1200 pixels"
        5 -> "2048 x 1536 pixels"
        20 -> "2288 x 1712 pixels"
        21 -> "2592 x 1944 pixels"
        22 -> "2304 x 1728 pixels"
        36 -> "3008 x 2008 pixels"
        else -> "Unknown ($value)"
      }
    }

  val qualityModeDescription: String?
    get() = getIndexedDescription(CasioType2MakernoteDirectory.TAG_QUALITY_MODE, 1, "Fine", "Super Fine")

  val thumbnailOffsetDescription: String?
    get() = _directory.getString(CasioType2MakernoteDirectory.TAG_THUMBNAIL_OFFSET)

  val thumbnailSizeDescription: String?
    get() {
      val value = _directory.getInteger(CasioType2MakernoteDirectory.TAG_THUMBNAIL_SIZE) ?: return null
      return "${Integer.toString(value)} bytes"
    }

  val thumbnailDimensionsDescription: String?
    get() {
      val dimensions = _directory.getIntArray(CasioType2MakernoteDirectory.TAG_THUMBNAIL_DIMENSIONS)
      return if (dimensions == null || dimensions.size != 2) _directory.getString(CasioType2MakernoteDirectory.TAG_THUMBNAIL_DIMENSIONS) else "${dimensions[0]} x ${dimensions[1]} pixels"
    }
}
