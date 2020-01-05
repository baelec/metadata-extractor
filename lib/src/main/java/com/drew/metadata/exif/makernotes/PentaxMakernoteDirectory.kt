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
 * Describes tags specific to Pentax and Asahi cameras.
 *
 * @author Drew Noakes https://drewnoakes.com
 */
class PentaxMakernoteDirectory : Directory() {
  override val tagNameMap = Companion.tagNameMap

  companion object {
    /**
     * 0 = Auto
     * 1 = Night-scene
     * 2 = Manual
     * 4 = Multiple
     */
    const val TAG_CAPTURE_MODE = 0x0001
    /**
     * 0 = Good
     * 1 = Better
     * 2 = Best
     */
    const val TAG_QUALITY_LEVEL = 0x0002
    /**
     * 2 = Custom
     * 3 = Auto
     */
    const val TAG_FOCUS_MODE = 0x0003
    /**
     * 1 = Auto
     * 2 = Flash on
     * 4 = Flash off
     * 6 = Red-eye Reduction
     */
    const val TAG_FLASH_MODE = 0x0004
    /**
     * 0 = Auto
     * 1 = Daylight
     * 2 = Shade
     * 3 = Tungsten
     * 4 = Fluorescent
     * 5 = Manual
     */
    const val TAG_WHITE_BALANCE = 0x0007
    /**
     * (0 = Off)
     */
    const val TAG_DIGITAL_ZOOM = 0x000A
    /**
     * 0 = Normal
     * 1 = Soft
     * 2 = Hard
     */
    const val TAG_SHARPNESS = 0x000B
    /**
     * 0 = Normal
     * 1 = Low
     * 2 = High
     */
    const val TAG_CONTRAST = 0x000C
    /**
     * 0 = Normal
     * 1 = Low
     * 2 = High
     */
    const val TAG_SATURATION = 0x000D
    /**
     * 10 = ISO 100
     * 16 = ISO 200
     * 100 = ISO 100
     * 200 = ISO 200
     */
    const val TAG_ISO_SPEED = 0x0014
    /**
     * 1 = Normal
     * 2 = Black &amp; White
     * 3 = Sepia
     */
    const val TAG_COLOUR = 0x0017
    /**
     * See Print Image Matching for specification.
     * http://www.ozhiker.com/electronics/pjmt/jpeg_info/pim.html
     */
    const val TAG_PRINT_IMAGE_MATCHING_INFO = 0x0E00
    /**
     * (String).
     */
    const val TAG_TIME_ZONE = 0x1000
    /**
     * (String).
     */
    const val TAG_DAYLIGHT_SAVINGS = 0x1001
    protected val tagNameMap = HashMap<Int, String>()

    init {
      tagNameMap[TAG_CAPTURE_MODE] = "Capture Mode"
      tagNameMap[TAG_QUALITY_LEVEL] = "Quality Level"
      tagNameMap[TAG_FOCUS_MODE] = "Focus Mode"
      tagNameMap[TAG_FLASH_MODE] = "Flash Mode"
      tagNameMap[TAG_WHITE_BALANCE] = "White Balance"
      tagNameMap[TAG_DIGITAL_ZOOM] = "Digital Zoom"
      tagNameMap[TAG_SHARPNESS] = "Sharpness"
      tagNameMap[TAG_CONTRAST] = "Contrast"
      tagNameMap[TAG_SATURATION] = "Saturation"
      tagNameMap[TAG_ISO_SPEED] = "ISO Speed"
      tagNameMap[TAG_COLOUR] = "Colour"
      tagNameMap[TAG_PRINT_IMAGE_MATCHING_INFO] = "Print Image Matching (PIM) Info"
      tagNameMap[TAG_TIME_ZONE] = "Time Zone"
      tagNameMap[TAG_DAYLIGHT_SAVINGS] = "Daylight Savings"
    }
  }

  override val name: String
    get() = "Pentax Makernote"

  init {
    setDescriptor(PentaxMakernoteDescriptor(this))
  }
}
