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

import com.drew.lang.SequentialByteArrayReader
import com.drew.lang.SequentialReader
import com.drew.lang.UTF_8
import com.drew.metadata.heif.HeifDirectory
import java.util.*

/**
 * ISO/IEC 14496-12:2015 pg.81-83
 */
class ItemInfoBox(reader: SequentialReader, box: Box) : FullBox(reader, box) {
  var entryCount: Long = 0
  var entries: ArrayList<ItemInfoEntry>

  class ItemInfoEntry(reader: SequentialReader, box: Box) : FullBox(reader, box) {
    var itemID: Long = 0
    var itemProtectionIndex: Long = 0
    var itemName: String? = null
    var contentType: String? = null
    var contentEncoding: String? = null
    var extensionType: String? = null
    var itemType: String? = null
    var itemUriType: String? = null

    init {
      if (version == 0 || version == 1) {
        itemID = reader.getUInt16().toLong()
        itemProtectionIndex = reader.getUInt16().toLong()
        itemName = reader.getNullTerminatedString((box.size - reader.position).toInt(), UTF_8)
        contentType = reader.getNullTerminatedString((box.size - reader.position).toInt(), UTF_8)
        if (box.size - reader.position > 0) {
          extensionType = reader.getNullTerminatedString((box.size - reader.position).toInt(), UTF_8)
        }
      }
      if (version == 1) {
        if (box.size - 28 >= 4) {
          contentEncoding = reader.getString(4)
        }
      }
      if (version >= 2) {
        if (version == 2) {
          itemID = reader.getUInt16().toLong()
        } else if (version == 3) {
          itemID = reader.getUInt32()
        }
        itemProtectionIndex = reader.getUInt16().toLong()
        itemType = reader.getString(4)
        itemName = reader.getNullTerminatedString((box.size - reader.position).toInt(), UTF_8)
        if (itemType == "mime") {
          contentType = reader.getNullTerminatedString((box.size - reader.position).toInt(), UTF_8)
          if (box.size - reader.position > 0) {
            contentEncoding = reader.getNullTerminatedString((box.size - reader.position).toInt(), UTF_8)
          }
        } else if (itemType == "uri ") {
          itemUriType = reader.getString((box.size - reader.position).toInt())
        }
      }
    }
  }

  fun addMetadata(directory: HeifDirectory?) {}

  init {
    entryCount = if (version == 0) {
      reader.getUInt16().toLong()
    } else {
      reader.getUInt32()
    }
    entries = ArrayList()
    for (i in 1..entryCount) {
      val entryBox = Box(reader)
      val byteReader = SequentialByteArrayReader(reader.getBytes(entryBox.size.toInt() - 8))
      entries.add(ItemInfoEntry(byteReader, entryBox))
    }
  }
}
