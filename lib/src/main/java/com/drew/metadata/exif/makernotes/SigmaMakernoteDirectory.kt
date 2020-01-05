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
 * Describes tags specific to Sigma / Foveon cameras.
 *
 * @author Drew Noakes https://drewnoakes.com
 */
class SigmaMakernoteDirectory : Directory() {
  override val tagNameMap = Companion.tagNameMap
  companion object {
    const val TAG_SERIAL_NUMBER = 0x2
    const val TAG_DRIVE_MODE = 0x3
    const val TAG_RESOLUTION_MODE = 0x4
    const val TAG_AUTO_FOCUS_MODE = 0x5
    const val TAG_FOCUS_SETTING = 0x6
    const val TAG_WHITE_BALANCE = 0x7
    const val TAG_EXPOSURE_MODE = 0x8
    const val TAG_METERING_MODE = 0x9
    const val TAG_LENS_RANGE = 0xa
    const val TAG_COLOR_SPACE = 0xb
    const val TAG_EXPOSURE = 0xc
    const val TAG_CONTRAST = 0xd
    const val TAG_SHADOW = 0xe
    const val TAG_HIGHLIGHT = 0xf
    const val TAG_SATURATION = 0x10
    const val TAG_SHARPNESS = 0x11
    const val TAG_FILL_LIGHT = 0x12
    const val TAG_COLOR_ADJUSTMENT = 0x14
    const val TAG_ADJUSTMENT_MODE = 0x15
    const val TAG_QUALITY = 0x16
    const val TAG_FIRMWARE = 0x17
    const val TAG_SOFTWARE = 0x18
    const val TAG_AUTO_BRACKET = 0x19
    protected val tagNameMap = HashMap<Int, String>()

    init {
      tagNameMap[TAG_SERIAL_NUMBER] = "Serial Number"
      tagNameMap[TAG_DRIVE_MODE] = "Drive Mode"
      tagNameMap[TAG_RESOLUTION_MODE] = "Resolution Mode"
      tagNameMap[TAG_AUTO_FOCUS_MODE] = "Auto Focus Mode"
      tagNameMap[TAG_FOCUS_SETTING] = "Focus Setting"
      tagNameMap[TAG_WHITE_BALANCE] = "White Balance"
      tagNameMap[TAG_EXPOSURE_MODE] = "Exposure Mode"
      tagNameMap[TAG_METERING_MODE] = "Metering Mode"
      tagNameMap[TAG_LENS_RANGE] = "Lens Range"
      tagNameMap[TAG_COLOR_SPACE] = "Color Space"
      tagNameMap[TAG_EXPOSURE] = "Exposure"
      tagNameMap[TAG_CONTRAST] = "Contrast"
      tagNameMap[TAG_SHADOW] = "Shadow"
      tagNameMap[TAG_HIGHLIGHT] = "Highlight"
      tagNameMap[TAG_SATURATION] = "Saturation"
      tagNameMap[TAG_SHARPNESS] = "Sharpness"
      tagNameMap[TAG_FILL_LIGHT] = "Fill Light"
      tagNameMap[TAG_COLOR_ADJUSTMENT] = "Color Adjustment"
      tagNameMap[TAG_ADJUSTMENT_MODE] = "Adjustment Mode"
      tagNameMap[TAG_QUALITY] = "Quality"
      tagNameMap[TAG_FIRMWARE] = "Firmware"
      tagNameMap[TAG_SOFTWARE] = "Software"
      tagNameMap[TAG_AUTO_BRACKET] = "Auto Bracket"
    }
  }

  override val name: String
    get() = "Sigma Makernote"

  init {
    setDescriptor(SigmaMakernoteDescriptor(this))
  }
}
