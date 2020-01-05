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
 * Describes tags specific to Kodak cameras.
 *
 * @author Drew Noakes https://drewnoakes.com
 */
class KodakMakernoteDirectory : Directory() {
  override val tagNameMap = Companion.tagNameMap

  companion object {
    const val TAG_KODAK_MODEL = 0
    const val TAG_QUALITY = 9
    const val TAG_BURST_MODE = 10
    const val TAG_IMAGE_WIDTH = 12
    const val TAG_IMAGE_HEIGHT = 14
    const val TAG_YEAR_CREATED = 16
    const val TAG_MONTH_DAY_CREATED = 18
    const val TAG_TIME_CREATED = 20
    const val TAG_BURST_MODE_2 = 24
    const val TAG_SHUTTER_MODE = 27
    const val TAG_METERING_MODE = 28
    const val TAG_SEQUENCE_NUMBER = 29
    const val TAG_F_NUMBER = 30
    const val TAG_EXPOSURE_TIME = 32
    const val TAG_EXPOSURE_COMPENSATION = 36
    const val TAG_FOCUS_MODE = 56
    const val TAG_WHITE_BALANCE = 64
    const val TAG_FLASH_MODE = 92
    const val TAG_FLASH_FIRED = 93
    const val TAG_ISO_SETTING = 94
    const val TAG_ISO = 96
    const val TAG_TOTAL_ZOOM = 98
    const val TAG_DATE_TIME_STAMP = 100
    const val TAG_COLOR_MODE = 102
    const val TAG_DIGITAL_ZOOM = 104
    const val TAG_SHARPNESS = 107
    protected val tagNameMap = HashMap<Int, String>()

    init {
      tagNameMap[TAG_KODAK_MODEL] = "Kodak Model"
      tagNameMap[TAG_QUALITY] = "Quality"
      tagNameMap[TAG_BURST_MODE] = "Burst Mode"
      tagNameMap[TAG_IMAGE_WIDTH] = "Image Width"
      tagNameMap[TAG_IMAGE_HEIGHT] = "Image Height"
      tagNameMap[TAG_YEAR_CREATED] = "Year Created"
      tagNameMap[TAG_MONTH_DAY_CREATED] = "Month/Day Created"
      tagNameMap[TAG_TIME_CREATED] = "Time Created"
      tagNameMap[TAG_BURST_MODE_2] = "Burst Mode 2"
      tagNameMap[TAG_SHUTTER_MODE] = "Shutter Speed"
      tagNameMap[TAG_METERING_MODE] = "Metering Mode"
      tagNameMap[TAG_SEQUENCE_NUMBER] = "Sequence Number"
      tagNameMap[TAG_F_NUMBER] = "F Number"
      tagNameMap[TAG_EXPOSURE_TIME] = "Exposure Time"
      tagNameMap[TAG_EXPOSURE_COMPENSATION] = "Exposure Compensation"
      tagNameMap[TAG_FOCUS_MODE] = "Focus Mode"
      tagNameMap[TAG_WHITE_BALANCE] = "White Balance"
      tagNameMap[TAG_FLASH_MODE] = "Flash Mode"
      tagNameMap[TAG_FLASH_FIRED] = "Flash Fired"
      tagNameMap[TAG_ISO_SETTING] = "ISO Setting"
      tagNameMap[TAG_ISO] = "ISO"
      tagNameMap[TAG_TOTAL_ZOOM] = "Total Zoom"
      tagNameMap[TAG_DATE_TIME_STAMP] = "Date/Time Stamp"
      tagNameMap[TAG_COLOR_MODE] = "Color Mode"
      tagNameMap[TAG_DIGITAL_ZOOM] = "Digital Zoom"
      tagNameMap[TAG_SHARPNESS] = "Sharpness"
    }
  }

  override val name: String
    get() = "Kodak Makernote"

  init {
    setDescriptor(KodakMakernoteDescriptor(this))
  }
}
