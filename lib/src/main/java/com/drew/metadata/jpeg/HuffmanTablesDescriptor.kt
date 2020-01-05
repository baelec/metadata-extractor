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
 * Provides a human-readable string version of the tag stored in a [HuffmanTablesDirectory].
 *
 *
 *  * https://en.wikipedia.org/wiki/Huffman_coding
 *  * http://stackoverflow.com/a/4954117
 *
 *
 * @author Nadahar
 */
class HuffmanTablesDescriptor(directory: HuffmanTablesDirectory) : TagDescriptor<HuffmanTablesDirectory>(directory) {
  override fun getDescription(tagType: Int): String? {
    return when (tagType) {
      HuffmanTablesDirectory.TAG_NUMBER_OF_TABLES -> numberOfTablesDescription
      else -> super.getDescription(tagType)
    }
  }

  val numberOfTablesDescription: String?
    get() {
      val value = _directory.getInteger(HuffmanTablesDirectory.TAG_NUMBER_OF_TABLES) ?: return null
      return value.toString() + if (value == 1) " Huffman table" else " Huffman tables"
    }
}
