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
 * Describes tags specific to certain Leica cameras.
 *
 *
 * Tag reference from: http://gvsoft.homedns.org/exif/makernote-leica-type1.html
 *
 * @author Drew Noakes https://drewnoakes.com
 */
class LeicaMakernoteDirectory : Directory() {
  override val tagNameMap = Companion.tagNameMap

  companion object {
    const val TAG_QUALITY = 0x0300
    const val TAG_USER_PROFILE = 0x0302
    const val TAG_SERIAL_NUMBER = 0x0303
    const val TAG_WHITE_BALANCE = 0x0304
    const val TAG_LENS_TYPE = 0x0310
    const val TAG_EXTERNAL_SENSOR_BRIGHTNESS_VALUE = 0x0311
    const val TAG_MEASURED_LV = 0x0312
    const val TAG_APPROXIMATE_F_NUMBER = 0x0313
    const val TAG_CAMERA_TEMPERATURE = 0x0320
    const val TAG_COLOR_TEMPERATURE = 0x0321
    const val TAG_WB_RED_LEVEL = 0x0322
    const val TAG_WB_GREEN_LEVEL = 0x0323
    const val TAG_WB_BLUE_LEVEL = 0x0324
    const val TAG_CCD_VERSION = 0x0330
    const val TAG_CCD_BOARD_VERSION = 0x0331
    const val TAG_CONTROLLER_BOARD_VERSION = 0x0332
    const val TAG_M16_C_VERSION = 0x0333
    const val TAG_IMAGE_ID_NUMBER = 0x0340
    protected val tagNameMap = HashMap<Int, String>()

    init {
      tagNameMap[TAG_QUALITY] = "Quality"
      tagNameMap[TAG_USER_PROFILE] = "User Profile"
      tagNameMap[TAG_SERIAL_NUMBER] = "Serial Number"
      tagNameMap[TAG_WHITE_BALANCE] = "White Balance"
      tagNameMap[TAG_LENS_TYPE] = "Lens Type"
      tagNameMap[TAG_EXTERNAL_SENSOR_BRIGHTNESS_VALUE] = "External Sensor Brightness Value"
      tagNameMap[TAG_MEASURED_LV] = "Measured LV"
      tagNameMap[TAG_APPROXIMATE_F_NUMBER] = "Approximate F Number"
      tagNameMap[TAG_CAMERA_TEMPERATURE] = "Camera Temperature"
      tagNameMap[TAG_COLOR_TEMPERATURE] = "Color Temperature"
      tagNameMap[TAG_WB_RED_LEVEL] = "WB Red Level"
      tagNameMap[TAG_WB_GREEN_LEVEL] = "WB Green Level"
      tagNameMap[TAG_WB_BLUE_LEVEL] = "WB Blue Level"
      tagNameMap[TAG_CCD_VERSION] = "CCD Version"
      tagNameMap[TAG_CCD_BOARD_VERSION] = "CCD Board Version"
      tagNameMap[TAG_CONTROLLER_BOARD_VERSION] = "Controller Board Version"
      tagNameMap[TAG_M16_C_VERSION] = "M16 C Version"
      tagNameMap[TAG_IMAGE_ID_NUMBER] = "Image ID Number"
    }
  }

  override val name: String
    get() = "Leica Makernote"

  init {
    setDescriptor(LeicaMakernoteDescriptor(this))
  }
}
