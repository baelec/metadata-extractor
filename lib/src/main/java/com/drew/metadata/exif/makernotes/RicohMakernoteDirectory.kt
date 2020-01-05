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
 * Describes tags specific to Ricoh cameras.
 *
 * @author Drew Noakes https://drewnoakes.com
 */
class RicohMakernoteDirectory : Directory() {
  override val tagNameMap = Companion.tagNameMap

  companion object {
    const val TAG_MAKERNOTE_DATA_TYPE = 0x0001
    const val TAG_VERSION = 0x0002
    const val TAG_PRINT_IMAGE_MATCHING_INFO = 0x0E00
    const val TAG_RICOH_CAMERA_INFO_MAKERNOTE_SUB_IFD_POINTER = 0x2001
    protected val tagNameMap = HashMap<Int, String>()

    init {
      tagNameMap[TAG_MAKERNOTE_DATA_TYPE] = "Makernote Data Type"
      tagNameMap[TAG_VERSION] = "Version"
      tagNameMap[TAG_PRINT_IMAGE_MATCHING_INFO] = "Print Image Matching (PIM) Info"
      tagNameMap[TAG_RICOH_CAMERA_INFO_MAKERNOTE_SUB_IFD_POINTER] = "Ricoh Camera Info Makernote Sub-IFD"
    }
  }

  override val name: String
    get() = "Ricoh Makernote"

  init {
    setDescriptor(RicohMakernoteDescriptor(this))
  }
}
