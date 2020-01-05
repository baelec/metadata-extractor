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

import com.drew.metadata.Directory
import java.util.*

/**
 * Describes tags specific to Casio (type 2) cameras.
 *
 * A standard TIFF IFD directory but always uses Motorola (Big-Endian) Byte Alignment.
 * Makernote data begins after a 6-byte header: "QVC\x00\x00\x00"
 *
 * @author Drew Noakes https://drewnoakes.com
 */
class CasioType2MakernoteDirectory : Directory() {
  override val tagNameMap = Companion.tagNameMap

  companion object {
    /**
     * 2 values - x,y dimensions in pixels.
     */
    const val TAG_THUMBNAIL_DIMENSIONS = 0x0002
    /**
     * Size in bytes
     */
    const val TAG_THUMBNAIL_SIZE = 0x0003
    /**
     * Offset of Preview Thumbnail
     */
    const val TAG_THUMBNAIL_OFFSET = 0x0004
    /**
     * 1 = Fine
     * 2 = Super Fine
     */
    const val TAG_QUALITY_MODE = 0x0008
    /**
     * 0 = 640 x 480 pixels
     * 4 = 1600 x 1200 pixels
     * 5 = 2048 x 1536 pixels
     * 20 = 2288 x 1712 pixels
     * 21 = 2592 x 1944 pixels
     * 22 = 2304 x 1728 pixels
     * 36 = 3008 x 2008 pixels
     */
    const val TAG_IMAGE_SIZE = 0x0009
    /**
     * 0 = Normal
     * 1 = Macro
     */
    const val TAG_FOCUS_MODE_1 = 0x000D
    /**
     * 3 = 50
     * 4 = 64
     * 6 = 100
     * 9 = 200
     */
    const val TAG_ISO_SENSITIVITY = 0x0014
    /**
     * 0 = Auto
     * 1 = Daylight
     * 2 = Shade
     * 3 = Tungsten
     * 4 = Fluorescent
     * 5 = Manual
     */
    const val TAG_WHITE_BALANCE_1 = 0x0019
    /**
     * Units are tenths of a millimetre
     */
    const val TAG_FOCAL_LENGTH = 0x001D
    /**
     * 0 = -1
     * 1 = Normal
     * 2 = +1
     */
    const val TAG_SATURATION = 0x001F
    /**
     * 0 = -1
     * 1 = Normal
     * 2 = +1
     */
    const val TAG_CONTRAST = 0x0020
    /**
     * 0 = -1
     * 1 = Normal
     * 2 = +1
     */
    const val TAG_SHARPNESS = 0x0021
    /**
     * See PIM specification here: http://www.ozhiker.com/electronics/pjmt/jpeg_info/pim.html
     */
    const val TAG_PRINT_IMAGE_MATCHING_INFO = 0x0E00
    /**
     * Alternate thumbnail offset
     */
    const val TAG_PREVIEW_THUMBNAIL = 0x2000
    /**
     *
     */
    const val TAG_WHITE_BALANCE_BIAS = 0x2011
    /**
     * 12 = Flash
     * 0 = Manual
     * 1 = Auto?
     * 4 = Flash?
     */
    const val TAG_WHITE_BALANCE_2 = 0x2012
    /**
     * Units are millimetres
     */
    const val TAG_OBJECT_DISTANCE = 0x2022
    /**
     * 0 = Off
     */
    const val TAG_FLASH_DISTANCE = 0x2034
    /**
     * 2 = Normal Mode
     */
    const val TAG_RECORD_MODE = 0x3000
    /**
     * 1 = Off?
     */
    const val TAG_SELF_TIMER = 0x3001
    /**
     * 3 = Fine
     */
    const val TAG_QUALITY = 0x3002
    /**
     * 1 = Fixation
     * 6 = Multi-Area Auto Focus
     */
    const val TAG_FOCUS_MODE_2 = 0x3003
    /**
     * (string)
     */
    const val TAG_TIME_ZONE = 0x3006
    /**
     *
     */
    const val TAG_BESTSHOT_MODE = 0x3007
    /**
     * 0 = Off
     * 1 = On?
     */
    const val TAG_CCD_ISO_SENSITIVITY = 0x3014
    /**
     * 0 = Off
     */
    const val TAG_COLOUR_MODE = 0x3015
    /**
     * 0 = Off
     */
    const val TAG_ENHANCEMENT = 0x3016
    /**
     * 0 = Off
     */
    const val TAG_FILTER = 0x3017
    protected val tagNameMap = HashMap<Int, String>()

    init { // TODO add missing names
      tagNameMap[TAG_THUMBNAIL_DIMENSIONS] = "Thumbnail Dimensions"
      tagNameMap[TAG_THUMBNAIL_SIZE] = "Thumbnail Size"
      tagNameMap[TAG_THUMBNAIL_OFFSET] = "Thumbnail Offset"
      tagNameMap[TAG_QUALITY_MODE] = "Quality Mode"
      tagNameMap[TAG_IMAGE_SIZE] = "Image Size"
      tagNameMap[TAG_FOCUS_MODE_1] = "Focus Mode"
      tagNameMap[TAG_ISO_SENSITIVITY] = "ISO Sensitivity"
      tagNameMap[TAG_WHITE_BALANCE_1] = "White Balance"
      tagNameMap[TAG_FOCAL_LENGTH] = "Focal Length"
      tagNameMap[TAG_SATURATION] = "Saturation"
      tagNameMap[TAG_CONTRAST] = "Contrast"
      tagNameMap[TAG_SHARPNESS] = "Sharpness"
      tagNameMap[TAG_PRINT_IMAGE_MATCHING_INFO] = "Print Image Matching (PIM) Info"
      tagNameMap[TAG_PREVIEW_THUMBNAIL] = "Casio Preview Thumbnail"
      tagNameMap[TAG_WHITE_BALANCE_BIAS] = "White Balance Bias"
      tagNameMap[TAG_WHITE_BALANCE_2] = "White Balance"
      tagNameMap[TAG_OBJECT_DISTANCE] = "Object Distance"
      tagNameMap[TAG_FLASH_DISTANCE] = "Flash Distance"
      tagNameMap[TAG_RECORD_MODE] = "Record Mode"
      tagNameMap[TAG_SELF_TIMER] = "Self Timer"
      tagNameMap[TAG_QUALITY] = "Quality"
      tagNameMap[TAG_FOCUS_MODE_2] = "Focus Mode"
      tagNameMap[TAG_TIME_ZONE] = "Time Zone"
      tagNameMap[TAG_BESTSHOT_MODE] = "BestShot Mode"
      tagNameMap[TAG_CCD_ISO_SENSITIVITY] = "CCD ISO Sensitivity"
      tagNameMap[TAG_COLOUR_MODE] = "Colour Mode"
      tagNameMap[TAG_ENHANCEMENT] = "Enhancement"
      tagNameMap[TAG_FILTER] = "Filter"
    }
  }

  override val name: String
    get() = "Casio Makernote"

  init {
    setDescriptor(CasioType2MakernoteDescriptor(this))
  }
}
