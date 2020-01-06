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
package com.drew.metadata.heif.boxes

import com.drew.lang.SequentialReader
import java.io.IOException

/**
 * ISO/IEC 14496-12:2015 pg.77-80
 */
class ItemLocationBox(reader: SequentialReader, box: Box) : FullBox(reader, box) {
  var indexSize = 0
  var offsetSize: Int
  var lengthSize: Int
  var baseOffsetSize: Int
  var itemCount: Long = 0
  var itemID: Long = 0
  var constructionMethod = 0
  var dataReferenceIndex = 0
  lateinit var baseOffset: ByteArray
  var extentCount = 0
  lateinit var extents: Array<Extent>
  @Throws(IOException::class)
  fun getIntFromUnknownByte(variable: Int, reader: SequentialReader): Long? {
    return when (variable) {
      1 -> reader.getUInt8().toLong()
      2 -> reader.getUInt16().toLong()
      4 -> reader.getUInt32()
      8 -> reader.getInt64()
      else -> null
    }
  }

  class Extent(var index: Long?, var offset: Long, var length: Long)

  init {
    var holder = reader.getUInt8().toInt()
    offsetSize = holder and 0xF0 shr 4
    lengthSize = holder and 0x0F
    holder = reader.getUInt8().toInt()
    baseOffsetSize = holder and 0xF0 shr 4
    if (version == 1 || version == 2) {
      indexSize = holder and 0x0F
    } else { // Reserved
    }
    if (version < 2) {
      itemCount = reader.getUInt16().toLong()
    } else if (version == 2) {
      itemCount = reader.getUInt32()
    }

    for (i in 0 until itemCount) {
      if (version < 2) {
        itemID = reader.getUInt16().toLong()
      } else if (version == 2) {
        itemID = reader.getUInt32()
      }
      if (version == 1 || version == 2) {
        holder = reader.getUInt16()
        constructionMethod = holder and 0x000F
      }
      dataReferenceIndex = reader.getUInt16()
      baseOffset = reader.getBytes(baseOffsetSize)
      extentCount = reader.getUInt16()
      var extentIndex: Long? = null

      extents = (0 until extentCount).mapNotNull {
        if (version == 1 || version == 2 && indexSize > 0) {
          extentIndex = getIntFromUnknownByte(indexSize, reader)
        }
        val extentOffset = getIntFromUnknownByte(offsetSize, reader)
        val extentLength = getIntFromUnknownByte(lengthSize, reader)
        if (extentOffset !== null && extentLength != null) {
          //TODO: Add logger here. This should never happen.
          Extent(extentIndex, extentOffset, extentLength)
        }
        null
      }.toTypedArray()
    }
  }
}
