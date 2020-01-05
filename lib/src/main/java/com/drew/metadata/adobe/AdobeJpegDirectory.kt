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
package com.drew.metadata.adobe

import com.drew.metadata.Directory
import java.util.*

/**
 * Contains image encoding information for DCT filters, as stored by Adobe.
 */
class AdobeJpegDirectory : Directory() {
  override val tagNameMap = Companion.tagNameMap

  companion object {
    const val TAG_DCT_ENCODE_VERSION = 0
    /**
     * The convention for TAG_APP14_FLAGS0 and TAG_APP14_FLAGS1 is that 0 bits are benign.
     * 1 bits in TAG_APP14_FLAGS0 pass information that is possibly useful but not essential for decoding.
     *
     *
     * 0x8000 bit: Encoder used Blend=1 downsampling
     */
    const val TAG_APP14_FLAGS0 = 1
    /**
     * The convention for TAG_APP14_FLAGS0 and TAG_APP14_FLAGS1 is that 0 bits are benign.
     * 1 bits in TAG_APP14_FLAGS1 pass information essential for decoding. DCTDecode could reject a compressed
     * image, if there are 1 bits in TAG_APP14_FLAGS1 or color transform codes that it cannot interpret.
     */
    const val TAG_APP14_FLAGS1 = 2
    const val TAG_COLOR_TRANSFORM = 3
    private val tagNameMap = HashMap<Int, String>()

    init {
      tagNameMap[TAG_DCT_ENCODE_VERSION] = "DCT Encode Version"
      tagNameMap[TAG_APP14_FLAGS0] = "Flags 0"
      tagNameMap[TAG_APP14_FLAGS1] = "Flags 1"
      tagNameMap[TAG_COLOR_TRANSFORM] = "Color Transform"
    }
  }

  override val name: String
    get() = "Adobe JPEG"

  init {
    setDescriptor(AdobeJpegDescriptor(this))
  }
}
