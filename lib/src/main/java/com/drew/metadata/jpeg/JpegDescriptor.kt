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
package com.drew.metadata.jpeg

import com.drew.metadata.TagDescriptor

/**
 * Provides human-readable string versions of the tags stored in a JpegDirectory.
 * Thanks to Darrell Silver (www.darrellsilver.com) for the initial version of this class.
 *
 * @author Drew Noakes https://drewnoakes.com
 */
class JpegDescriptor(directory: JpegDirectory) : TagDescriptor<JpegDirectory>(directory) {
  override fun getDescription(tagType: Int): String? {
    return when (tagType) {
      JpegDirectory.TAG_COMPRESSION_TYPE -> imageCompressionTypeDescription
      JpegDirectory.TAG_COMPONENT_DATA_1 -> getComponentDataDescription(0)
      JpegDirectory.TAG_COMPONENT_DATA_2 -> getComponentDataDescription(1)
      JpegDirectory.TAG_COMPONENT_DATA_3 -> getComponentDataDescription(2)
      JpegDirectory.TAG_COMPONENT_DATA_4 -> getComponentDataDescription(3)
      JpegDirectory.TAG_DATA_PRECISION -> dataPrecisionDescription
      JpegDirectory.TAG_IMAGE_HEIGHT -> imageHeightDescription
      JpegDirectory.TAG_IMAGE_WIDTH -> imageWidthDescription
      else -> super.getDescription(tagType)
    }
  }

  // no 4
  // no 12
  val imageCompressionTypeDescription: String?
    get() = getIndexedDescription(JpegDirectory.TAG_COMPRESSION_TYPE,
      "Baseline",
      "Extended sequential, Huffman",
      "Progressive, Huffman",
      "Lossless, Huffman",
      null,  // no 4
      "Differential sequential, Huffman",
      "Differential progressive, Huffman",
      "Differential lossless, Huffman",
      "Reserved for JPEG extensions",
      "Extended sequential, arithmetic",
      "Progressive, arithmetic",
      "Lossless, arithmetic",
      null,  // no 12
      "Differential sequential, arithmetic",
      "Differential progressive, arithmetic",
      "Differential lossless, arithmetic")

  val imageWidthDescription: String?
    get() {
      val value = _directory.getString(JpegDirectory.TAG_IMAGE_WIDTH) ?: return null
      return "$value pixels"
    }

  val imageHeightDescription: String?
    get() {
      val value = _directory.getString(JpegDirectory.TAG_IMAGE_HEIGHT) ?: return null
      return "$value pixels"
    }

  val dataPrecisionDescription: String?
    get() {
      val value = _directory.getString(JpegDirectory.TAG_DATA_PRECISION) ?: return null
      return "$value bits"
    }

  fun getComponentDataDescription(componentNumber: Int): String? {
    val value = _directory.getComponent(componentNumber) ?: return null
    return "${value.componentName} component: $value"
  }
}
