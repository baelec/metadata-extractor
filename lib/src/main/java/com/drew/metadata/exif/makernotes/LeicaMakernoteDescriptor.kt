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
 * Provides human-readable string representations of tag values stored in a [LeicaMakernoteDirectory].
 *
 *
 * Tag reference from: http://gvsoft.homedns.org/exif/makernote-leica-type1.html
 *
 * @author Drew Noakes https://drewnoakes.com
 */
class LeicaMakernoteDescriptor(directory: LeicaMakernoteDirectory) : TagDescriptor<LeicaMakernoteDirectory>(directory) {
  override fun getDescription(tagType: Int): String? {
    return when (tagType) {
      LeicaMakernoteDirectory.TAG_QUALITY -> qualityDescription
      LeicaMakernoteDirectory.TAG_USER_PROFILE -> userProfileDescription
      LeicaMakernoteDirectory.TAG_WHITE_BALANCE -> whiteBalanceDescription
      LeicaMakernoteDirectory.TAG_EXTERNAL_SENSOR_BRIGHTNESS_VALUE -> externalSensorBrightnessValueDescription
      LeicaMakernoteDirectory.TAG_MEASURED_LV -> measuredLvDescription
      LeicaMakernoteDirectory.TAG_APPROXIMATE_F_NUMBER -> approximateFNumberDescription
      LeicaMakernoteDirectory.TAG_CAMERA_TEMPERATURE -> cameraTemperatureDescription
      LeicaMakernoteDirectory.TAG_WB_RED_LEVEL, LeicaMakernoteDirectory.TAG_WB_BLUE_LEVEL, LeicaMakernoteDirectory.TAG_WB_GREEN_LEVEL -> getSimpleRational(tagType)
      else -> super.getDescription(tagType)
    }
  }

  private val cameraTemperatureDescription: String?
    private get() = getFormattedInt(LeicaMakernoteDirectory.TAG_CAMERA_TEMPERATURE, "%d C")

  private val approximateFNumberDescription: String?
    private get() = getSimpleRational(LeicaMakernoteDirectory.TAG_APPROXIMATE_F_NUMBER)

  private val measuredLvDescription: String?
    private get() = getSimpleRational(LeicaMakernoteDirectory.TAG_MEASURED_LV)

  private val externalSensorBrightnessValueDescription: String?
    private get() = getSimpleRational(LeicaMakernoteDirectory.TAG_EXTERNAL_SENSOR_BRIGHTNESS_VALUE)

  private val whiteBalanceDescription: String?
    private get() = getIndexedDescription(LeicaMakernoteDirectory.TAG_WHITE_BALANCE,
      "Auto or Manual",
      "Daylight",
      "Fluorescent",
      "Tungsten",
      "Flash",
      "Cloudy",
      "Shadow"
    )

  private val userProfileDescription: String?
    private get() = getIndexedDescription(LeicaMakernoteDirectory.TAG_QUALITY, 1,
      "User Profile 1",
      "User Profile 2",
      "User Profile 3",
      "User Profile 0 (Dynamic)"
    )

  private val qualityDescription: String?
    private get() = getIndexedDescription(LeicaMakernoteDirectory.TAG_QUALITY, 1, "Fine", "Basic")
}
