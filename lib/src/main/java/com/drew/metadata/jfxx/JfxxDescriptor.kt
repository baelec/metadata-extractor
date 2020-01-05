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
package com.drew.metadata.jfxx

import com.drew.metadata.TagDescriptor

/**
 * Provides human-readable string versions of the tags stored in a JfxxDirectory.
 *
 *
 *  * http://en.wikipedia.org/wiki/JPEG_File_Interchange_Format
 *  * http://www.w3.org/Graphics/JPEG/jfif3.pdf
 *
 *
 * @author Drew Noakes
 */
class JfxxDescriptor(directory: JfxxDirectory) : TagDescriptor<JfxxDirectory>(directory) {
  override fun getDescription(tagType: Int): String? {
    return when (tagType) {
      JfxxDirectory.TAG_EXTENSION_CODE -> extensionCodeDescription
      else -> super.getDescription(tagType)
    }
  }

  val extensionCodeDescription: String?
    get() {
      val value = _directory.getInteger(JfxxDirectory.TAG_EXTENSION_CODE) ?: return null
      return when (value) {
        0x10 -> "Thumbnail coded using JPEG"
        0x11 -> "Thumbnail stored using 1 byte/pixel"
        0x13 -> "Thumbnail stored using 3 bytes/pixel"
        else -> "Unknown extension code $value"
      }
    }
}
