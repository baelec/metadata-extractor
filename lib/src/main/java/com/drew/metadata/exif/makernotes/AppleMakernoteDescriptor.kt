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
 * Provides human-readable string representations of tag values stored in a [AppleMakernoteDirectory].
 *
 *
 * Using information from http://owl.phy.queensu.ca/~phil/exiftool/TagNames/Apple.html
 *
 * @author Drew Noakes https://drewnoakes.com
 */
class AppleMakernoteDescriptor(directory: AppleMakernoteDirectory) : TagDescriptor<AppleMakernoteDirectory>(directory) {
  override fun getDescription(tagType: Int): String? {
    return when (tagType) {
      AppleMakernoteDirectory.TAG_HDR_IMAGE_TYPE -> hdrImageTypeDescription
      else -> super.getDescription(tagType)
    }
  }

  val hdrImageTypeDescription: String?
    get() = getIndexedDescription(AppleMakernoteDirectory.TAG_HDR_IMAGE_TYPE, 3, "HDR Image", "Original Image")
}
