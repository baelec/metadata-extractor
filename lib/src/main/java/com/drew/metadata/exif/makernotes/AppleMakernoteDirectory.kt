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
 * Describes tags specific to Apple cameras.
 *
 *
 * Using information from http://owl.phy.queensu.ca/~phil/exiftool/TagNames/Apple.html
 *
 * @author Drew Noakes https://drewnoakes.com
 */
class AppleMakernoteDirectory : Directory() {
  override val tagNameMap = Companion.tagNameMap

  companion object {
    const val TAG_RUN_TIME = 0x0003
    const val TAG_HDR_IMAGE_TYPE = 0x000a
    const val TAG_BURST_UUID = 0x000b
    protected val tagNameMap = HashMap<Int, String>()

    init {
      tagNameMap[TAG_RUN_TIME] = "Run Time"
      tagNameMap[TAG_HDR_IMAGE_TYPE] = "HDR Image Type"
      tagNameMap[TAG_BURST_UUID] = "Burst UUID"
    }
  }

  override val name: String
    get() = "Apple Makernote"

  init {
    setDescriptor(AppleMakernoteDescriptor(this))
  }
}
