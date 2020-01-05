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
package com.drew.metadata.jfif

import com.drew.metadata.TagDescriptor

/**
 * Provides human-readable string versions of the tags stored in a JfifDirectory.
 *
 *
 *  * http://en.wikipedia.org/wiki/JPEG_File_Interchange_Format
 *  * http://www.w3.org/Graphics/JPEG/jfif3.pdf
 *
 *
 * @author Yuri Binev, Drew Noakes
 */
class JfifDescriptor(directory: JfifDirectory) : TagDescriptor<JfifDirectory>(directory) {
  override fun getDescription(tagType: Int): String? {
    return when (tagType) {
      JfifDirectory.TAG_RESX -> imageResXDescription
      JfifDirectory.TAG_RESY -> imageResYDescription
      JfifDirectory.TAG_VERSION -> imageVersionDescription
      JfifDirectory.TAG_UNITS -> imageResUnitsDescription
      else -> super.getDescription(tagType)
    }
  }

  val imageVersionDescription: String?
    get() {
      val value = _directory.getInteger(JfifDirectory.TAG_VERSION) ?: return null
      return "%d.%d".format(value and 0xFF00 shr 8, value and 0xFF)
    }

  val imageResYDescription: String?
    get() {
      val value = _directory.getInteger(JfifDirectory.TAG_RESY) ?: return null
      return "%d dot%s".format(
        value,
        if (value == 1) "" else "s")
    }

  val imageResXDescription: String?
    get() {
      val value = _directory.getInteger(JfifDirectory.TAG_RESX) ?: return null
      return "%d dot%s".format(
        value,
        if (value == 1) "" else "s")
    }

  val imageResUnitsDescription: String?
    get() {
      val value = _directory.getInteger(JfifDirectory.TAG_UNITS) ?: return null
      return when (value) {
        0 -> "none"
        1 -> "inch"
        2 -> "centimetre"
        else -> "unit"
      }
    }
}
