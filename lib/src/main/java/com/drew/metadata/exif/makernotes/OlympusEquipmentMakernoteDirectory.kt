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

import com.drew.metadata.Directory
import java.util.*

/**
 * The Olympus equipment makernote is used by many manufacturers (Epson, Konica, Minolta and Agfa...), and as such contains some tags
 * that appear specific to those manufacturers.
 *
 * @author Kevin Mott https://github.com/kwhopper
 * @author Drew Noakes https://drewnoakes.com
 */
class OlympusEquipmentMakernoteDirectory : Directory() {
  override val tagNameMap = Companion.tagNameMap

  companion object {
    const val TAG_EQUIPMENT_VERSION = 0x0000
    const val TAG_CAMERA_TYPE_2 = 0x0100
    const val TAG_SERIAL_NUMBER = 0x0101
    const val TAG_INTERNAL_SERIAL_NUMBER = 0x0102
    const val TAG_FOCAL_PLANE_DIAGONAL = 0x0103
    const val TAG_BODY_FIRMWARE_VERSION = 0x0104
    const val TAG_LENS_TYPE = 0x0201
    const val TAG_LENS_SERIAL_NUMBER = 0x0202
    const val TAG_LENS_MODEL = 0x0203
    const val TAG_LENS_FIRMWARE_VERSION = 0x0204
    const val TAG_MAX_APERTURE_AT_MIN_FOCAL = 0x0205
    const val TAG_MAX_APERTURE_AT_MAX_FOCAL = 0x0206
    const val TAG_MIN_FOCAL_LENGTH = 0x0207
    const val TAG_MAX_FOCAL_LENGTH = 0x0208
    const val TAG_MAX_APERTURE = 0x020A
    const val TAG_LENS_PROPERTIES = 0x020B
    const val TAG_EXTENDER = 0x0301
    const val TAG_EXTENDER_SERIAL_NUMBER = 0x0302
    const val TAG_EXTENDER_MODEL = 0x0303
    const val TAG_EXTENDER_FIRMWARE_VERSION = 0x0304
    const val TAG_CONVERSION_LENS = 0x0403
    const val TAG_FLASH_TYPE = 0x1000
    const val TAG_FLASH_MODEL = 0x1001
    const val TAG_FLASH_FIRMWARE_VERSION = 0x1002
    const val TAG_FLASH_SERIAL_NUMBER = 0x1003
    protected val tagNameMap = HashMap<Int, String>()

    init {
      tagNameMap[TAG_EQUIPMENT_VERSION] = "Equipment Version"
      tagNameMap[TAG_CAMERA_TYPE_2] = "Camera Type 2"
      tagNameMap[TAG_SERIAL_NUMBER] = "Serial Number"
      tagNameMap[TAG_INTERNAL_SERIAL_NUMBER] = "Internal Serial Number"
      tagNameMap[TAG_FOCAL_PLANE_DIAGONAL] = "Focal Plane Diagonal"
      tagNameMap[TAG_BODY_FIRMWARE_VERSION] = "Body Firmware Version"
      tagNameMap[TAG_LENS_TYPE] = "Lens Type"
      tagNameMap[TAG_LENS_SERIAL_NUMBER] = "Lens Serial Number"
      tagNameMap[TAG_LENS_MODEL] = "Lens Model"
      tagNameMap[TAG_LENS_FIRMWARE_VERSION] = "Lens Firmware Version"
      tagNameMap[TAG_MAX_APERTURE_AT_MIN_FOCAL] = "Max Aperture At Min Focal"
      tagNameMap[TAG_MAX_APERTURE_AT_MAX_FOCAL] = "Max Aperture At Max Focal"
      tagNameMap[TAG_MIN_FOCAL_LENGTH] = "Min Focal Length"
      tagNameMap[TAG_MAX_FOCAL_LENGTH] = "Max Focal Length"
      tagNameMap[TAG_MAX_APERTURE] = "Max Aperture"
      tagNameMap[TAG_LENS_PROPERTIES] = "Lens Properties"
      tagNameMap[TAG_EXTENDER] = "Extender"
      tagNameMap[TAG_EXTENDER_SERIAL_NUMBER] = "Extender Serial Number"
      tagNameMap[TAG_EXTENDER_MODEL] = "Extender Model"
      tagNameMap[TAG_EXTENDER_FIRMWARE_VERSION] = "Extender Firmware Version"
      tagNameMap[TAG_CONVERSION_LENS] = "Conversion Lens"
      tagNameMap[TAG_FLASH_TYPE] = "Flash Type"
      tagNameMap[TAG_FLASH_MODEL] = "Flash Model"
      tagNameMap[TAG_FLASH_FIRMWARE_VERSION] = "Flash Firmware Version"
      tagNameMap[TAG_FLASH_SERIAL_NUMBER] = "Flash Serial Number"
    }
  }

  override val name: String
    get() = "Olympus Equipment"

  init {
    setDescriptor(OlympusEquipmentMakernoteDescriptor(this))
  }
}
