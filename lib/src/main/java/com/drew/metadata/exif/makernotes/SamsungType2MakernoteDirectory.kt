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
 * Describes tags specific certain 'newer' Samsung cameras.
 *
 *
 * Tag reference from: http://www.sno.phy.queensu.ca/~phil/exiftool/TagNames/Samsung.html
 *
 * @author Kevin Mott https://github.com/kwhopper
 * @author Drew Noakes https://drewnoakes.com
 */
class SamsungType2MakernoteDirectory : Directory() {
  override val tagNameMap = Companion.tagNameMap

  companion object {
    // This list is incomplete
    const val TagMakerNoteVersion = 0x001
    const val TagDeviceType = 0x0002
    const val TagSamsungModelId = 0x0003
    const val TagCameraTemperature = 0x0043
    const val TagFaceDetect = 0x0100
    const val TagFaceRecognition = 0x0120
    const val TagFaceName = 0x0123
    // following tags found only in SRW images
    const val TagFirmwareName = 0xa001
    protected val tagNameMap = HashMap<Int, String>()

    init {
      tagNameMap[TagMakerNoteVersion] = "Maker Note Version"
      tagNameMap[TagDeviceType] = "Device Type"
      tagNameMap[TagSamsungModelId] = "Model Id"
      tagNameMap[TagCameraTemperature] = "Camera Temperature"
      tagNameMap[TagFaceDetect] = "Face Detect"
      tagNameMap[TagFaceRecognition] = "Face Recognition"
      tagNameMap[TagFaceName] = "Face Name"
      tagNameMap[TagFirmwareName] = "Firmware Name"
    }
  }

  override val name: String
    get() = "Samsung Makernote"

  init {
    setDescriptor(SamsungType2MakernoteDescriptor(this))
  }
}
