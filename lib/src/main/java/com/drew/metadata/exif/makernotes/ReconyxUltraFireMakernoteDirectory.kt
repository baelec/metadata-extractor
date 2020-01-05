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
 * Describes tags specific to Reconyx UltraFire cameras.
 *
 * Reconyx uses a fixed makernote block.  Tag values are the byte index of the tag within the makernote.
 * @author Todd West http://cascadescarnivoreproject.blogspot.com
 */
class ReconyxUltraFireMakernoteDirectory : Directory() {
  override val tagNameMap = Companion.tagNameMap

  companion object {
    /**
     * Version number used for identifying makernotes from Reconyx UltraFire cameras.
     */
    const val MAKERNOTE_ID = 0x00010000
    /**
     * Version number used for identifying the public portion of makernotes from Reconyx UltraFire cameras.
     */
    const val MAKERNOTE_PUBLIC_ID = 0x07f10001
    const val TAG_LABEL = 0
    const val TAG_MAKERNOTE_ID = 10
    const val TAG_MAKERNOTE_SIZE = 14
    const val TAG_MAKERNOTE_PUBLIC_ID = 18
    const val TAG_MAKERNOTE_PUBLIC_SIZE = 22
    const val TAG_CAMERA_VERSION = 24
    const val TAG_UIB_VERSION = 31
    const val TAG_BTL_VERSION = 38
    const val TAG_PEX_VERSION = 45
    const val TAG_EVENT_TYPE = 52
    const val TAG_SEQUENCE = 53
    const val TAG_EVENT_NUMBER = 55
    const val TAG_DATE_TIME_ORIGINAL = 59
    const val TAG_DAY_OF_WEEK = 66
    const val TAG_MOON_PHASE = 67
    const val TAG_AMBIENT_TEMPERATURE_FAHRENHEIT = 68
    const val TAG_AMBIENT_TEMPERATURE = 70
    const val TAG_FLASH = 72
    const val TAG_BATTERY_VOLTAGE = 73
    const val TAG_SERIAL_NUMBER = 75
    const val TAG_USER_LABEL = 80
    protected val tagNameMap = HashMap<Int, String>()

    init {
      tagNameMap[TAG_LABEL] = "Makernote Label"
      tagNameMap[TAG_MAKERNOTE_ID] = "Makernote ID"
      tagNameMap[TAG_MAKERNOTE_SIZE] = "Makernote Size"
      tagNameMap[TAG_MAKERNOTE_PUBLIC_ID] = "Makernote Public ID"
      tagNameMap[TAG_MAKERNOTE_PUBLIC_SIZE] = "Makernote Public Size"
      tagNameMap[TAG_CAMERA_VERSION] = "Camera Version"
      tagNameMap[TAG_UIB_VERSION] = "Uib Version"
      tagNameMap[TAG_BTL_VERSION] = "Btl Version"
      tagNameMap[TAG_PEX_VERSION] = "Pex Version"
      tagNameMap[TAG_EVENT_TYPE] = "Event Type"
      tagNameMap[TAG_SEQUENCE] = "Sequence"
      tagNameMap[TAG_EVENT_NUMBER] = "Event Number"
      tagNameMap[TAG_DATE_TIME_ORIGINAL] = "Date/Time Original"
      tagNameMap[TAG_DAY_OF_WEEK] = "Day of Week"
      tagNameMap[TAG_MOON_PHASE] = "Moon Phase"
      tagNameMap[TAG_AMBIENT_TEMPERATURE_FAHRENHEIT] = "Ambient Temperature Fahrenheit"
      tagNameMap[TAG_AMBIENT_TEMPERATURE] = "Ambient Temperature"
      tagNameMap[TAG_FLASH] = "Flash"
      tagNameMap[TAG_BATTERY_VOLTAGE] = "Battery Voltage"
      tagNameMap[TAG_SERIAL_NUMBER] = "Serial Number"
      tagNameMap[TAG_USER_LABEL] = "User Label"
    }
  }

  override val name: String
    get() = "Reconyx UltraFire Makernote"

  init {
    setDescriptor(ReconyxUltraFireMakernoteDescriptor(this))
  }
}
