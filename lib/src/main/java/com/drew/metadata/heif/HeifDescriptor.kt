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
package com.drew.metadata.heif

import com.drew.metadata.TagDescriptor

class HeifDescriptor(directory: HeifDirectory) : TagDescriptor<HeifDirectory>(directory) {
  override fun getDescription(tagType: Int): String? {
    return when (tagType) {
      HeifDirectory.TAG_IMAGE_WIDTH, HeifDirectory.TAG_IMAGE_HEIGHT -> getPixelDescription(tagType)
      HeifDirectory.TAG_IMAGE_ROTATION -> getRotationDescription(tagType)
      else -> super.getDescription(tagType)
    }
  }

  fun getPixelDescription(tagType: Int): String {
    return "${_directory.getString(tagType)} pixels"
  }

  fun getRotationDescription(tagType: Int): String {
    return "${(_directory.getInteger(tagType) ?: 0) * 90} degrees"
  }
}
