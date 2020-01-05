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
 * Describes tags specific to Reconyx HyperFire cameras.
 *
 * Reconyx uses a fixed makernote block.  Tag values are the byte index of the tag within the makernote.
 * @author Todd West http://cascadescarnivoreproject.blogspot.com
 */
class ReconyxHyperFireMakernoteDirectory : Directory() {
  override val tagNameMap = Companion.tagNameMap

  companion object {
    /**
     * Version number used for identifying makernotes from Reconyx HyperFire cameras.
     */
    const val MAKERNOTE_VERSION = 61697
    const val TAG_MAKERNOTE_VERSION = 0
    const val TAG_FIRMWARE_VERSION = 2
    const val TAG_TRIGGER_MODE = 12
    const val TAG_SEQUENCE = 14
    const val TAG_EVENT_NUMBER = 18
    const val TAG_DATE_TIME_ORIGINAL = 22
    const val TAG_MOON_PHASE = 36
    const val TAG_AMBIENT_TEMPERATURE_FAHRENHEIT = 38
    const val TAG_AMBIENT_TEMPERATURE = 40
    const val TAG_SERIAL_NUMBER = 42
    const val TAG_CONTRAST = 72
    const val TAG_BRIGHTNESS = 74
    const val TAG_SHARPNESS = 76
    const val TAG_SATURATION = 78
    const val TAG_INFRARED_ILLUMINATOR = 80
    const val TAG_MOTION_SENSITIVITY = 82
    const val TAG_BATTERY_VOLTAGE = 84
    const val TAG_USER_LABEL = 86
    protected val tagNameMap = HashMap<Int, String>()

    init {
      tagNameMap[TAG_MAKERNOTE_VERSION] = "Makernote Version"
      tagNameMap[TAG_FIRMWARE_VERSION] = "Firmware Version"
      tagNameMap[TAG_TRIGGER_MODE] = "Trigger Mode"
      tagNameMap[TAG_SEQUENCE] = "Sequence"
      tagNameMap[TAG_EVENT_NUMBER] = "Event Number"
      tagNameMap[TAG_DATE_TIME_ORIGINAL] = "Date/Time Original"
      tagNameMap[TAG_MOON_PHASE] = "Moon Phase"
      tagNameMap[TAG_AMBIENT_TEMPERATURE_FAHRENHEIT] = "Ambient Temperature Fahrenheit"
      tagNameMap[TAG_AMBIENT_TEMPERATURE] = "Ambient Temperature"
      tagNameMap[TAG_SERIAL_NUMBER] = "Serial Number"
      tagNameMap[TAG_CONTRAST] = "Contrast"
      tagNameMap[TAG_BRIGHTNESS] = "Brightness"
      tagNameMap[TAG_SHARPNESS] = "Sharpness"
      tagNameMap[TAG_SATURATION] = "Saturation"
      tagNameMap[TAG_INFRARED_ILLUMINATOR] = "Infrared Illuminator"
      tagNameMap[TAG_MOTION_SENSITIVITY] = "Motion Sensitivity"
      tagNameMap[TAG_BATTERY_VOLTAGE] = "Battery Voltage"
      tagNameMap[TAG_USER_LABEL] = "User Label"
    }
  }

  override val name: String
    get() = "Reconyx HyperFire Makernote"

  init {
    setDescriptor(ReconyxHyperFireMakernoteDescriptor(this))
  }
}
