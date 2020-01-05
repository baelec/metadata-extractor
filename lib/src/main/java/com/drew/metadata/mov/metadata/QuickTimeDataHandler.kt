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
package com.drew.metadata.mov.metadata

import com.drew.imaging.quicktime.QuickTimeHandler
import com.drew.lang.SequentialByteArrayReader
import com.drew.lang.getInt32
import com.drew.metadata.Metadata
import com.drew.metadata.mov.QuickTimeAtomTypes
import com.drew.metadata.mov.QuickTimeContainerTypes
import com.drew.metadata.mov.QuickTimeContext
import com.drew.metadata.mov.QuickTimeMetadataHandler
import com.drew.metadata.mov.atoms.Atom
import java.io.IOException
import java.util.*

/**
 * @author Payton Garland
 */
class QuickTimeDataHandler(metadata: Metadata) : QuickTimeMetadataHandler(metadata) {
  private var currentIndex = 0
  private val keys = ArrayList<String>()
  override fun shouldAcceptAtom(atom: Atom): Boolean {
    return atom.type == QuickTimeAtomTypes.ATOM_HANDLER || atom.type == QuickTimeAtomTypes.ATOM_KEYS || atom.type == QuickTimeAtomTypes.ATOM_DATA
  }

  override fun shouldAcceptContainer(atom: Atom): Boolean {
    return atom.type == QuickTimeContainerTypes.ATOM_METADATA_LIST || getInt32(atom.type.toByteArray(), 0, true) <= keys.size
  }

  @Throws(IOException::class)
  override fun processAtom(atom: Atom, payload: ByteArray?, context: QuickTimeContext): QuickTimeHandler<*> {
    if (payload != null) {
      val reader = SequentialByteArrayReader(payload)
      if (atom.type == QuickTimeAtomTypes.ATOM_KEYS) {
        processKeys(reader)
      } else if (atom.type == QuickTimeAtomTypes.ATOM_DATA) {
        processData(payload, reader)
      }
    } else {
      val numValue = getInt32(atom.type.toByteArray(), 0, true)
      if (numValue > 0 && numValue < keys.size + 1) {
        currentIndex = numValue - 1
      }
    }
    return this
  }

  @Throws(IOException::class)
  override fun processKeys(reader: SequentialByteArrayReader) { // Version 1-byte and Flags 3-bytes
    reader.skip(4)
    val entryCount = reader.getInt32()
    for (i in 0 until entryCount) {
      val keySize = reader.getInt32()
      reader.skip(4) // key namespace
      val keyValue = String(reader.getBytes(keySize - 8))
      keys.add(keyValue)
    }
  }

  @Throws(IOException::class)
  override fun processData(payload: ByteArray, reader: SequentialByteArrayReader) {
    val type = reader.getInt32()
    // 4 bytes: locale indicator
    reader.skip(4)
    val tag = QuickTimeMetadataDirectory._tagIntegerMap[keys[currentIndex]]
    if (tag != null) {
      val length = payload.size - 8
      when (type) {
        1 -> directory.setString(tag, reader.getString(length, "UTF-8"))
        13, 14, 27 -> directory.setByteArray(tag, reader.getBytes(length))
        22 -> {
          val buf = ByteArray(4)
          reader.getBytes(buf, 4 - length, length)
          directory.setInt(tag, SequentialByteArrayReader(buf).getInt32())
        }
        23 -> directory.setFloat(tag, reader.getFloat32())
        30 -> {
          val value = IntArray(length / 4)
          var i = 0
          while (i < value.size) {
            value[i] = reader.getInt32()
            i++
          }
          directory.setIntArray(tag, value)
        }
      }
    }
  }
}
