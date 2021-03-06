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

import com.drew.metadata.TagDescriptor

/**
 * Provides human-readable string versions of the tags stored in an AdobeJpegDirectory.
 */
class AdobeJpegDescriptor(directory: AdobeJpegDirectory) : TagDescriptor<AdobeJpegDirectory>(directory) {
  override fun getDescription(tagType: Int): String? {
    return when (tagType) {
      AdobeJpegDirectory.TAG_COLOR_TRANSFORM -> colorTransformDescription
      AdobeJpegDirectory.TAG_DCT_ENCODE_VERSION -> dctEncodeVersionDescription
      else -> super.getDescription(tagType)
    }
  }

  private val dctEncodeVersionDescription: String?
    get() {
      val value = _directory.getInteger(AdobeJpegDirectory.TAG_DCT_ENCODE_VERSION)
      return if (value == null) null else if (value == 0x64) "100" else value.toString()
    }

  private val colorTransformDescription: String?
    get() = getIndexedDescription(AdobeJpegDirectory.TAG_COLOR_TRANSFORM,
      "Unknown (RGB or CMYK)",
      "YCbCr",
      "YCCK")
}
