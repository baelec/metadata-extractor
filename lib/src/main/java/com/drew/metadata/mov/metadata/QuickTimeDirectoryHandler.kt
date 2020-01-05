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
import com.drew.metadata.Metadata
import com.drew.metadata.mov.QuickTimeAtomTypes
import com.drew.metadata.mov.QuickTimeContainerTypes
import com.drew.metadata.mov.QuickTimeContext
import com.drew.metadata.mov.QuickTimeMetadataHandler
import com.drew.metadata.mov.atoms.Atom
import java.io.IOException

/**
 * @author Payton Garland
 */
class QuickTimeDirectoryHandler(metadata: Metadata) : QuickTimeMetadataHandler(metadata) {
  private var currentData: String? = null
  override fun shouldAcceptAtom(atom: Atom): Boolean {
    return atom.type == QuickTimeAtomTypes.ATOM_DATA
  }

  override fun shouldAcceptContainer(atom: Atom): Boolean {
    return (QuickTimeMetadataDirectory._tagIntegerMap.containsKey(atom.type)
      || atom.type == QuickTimeContainerTypes.ATOM_METADATA_LIST)
  }

  @Throws(IOException::class)
  override fun processAtom(atom: Atom, payload: ByteArray?, context: QuickTimeContext): QuickTimeHandler<*> {
    if (payload != null) {
      val reader = SequentialByteArrayReader(payload)
      if (atom.type == QuickTimeAtomTypes.ATOM_DATA && currentData != null) {
        processData(payload, reader)
      } else {
        currentData = String(reader.getBytes(4))
      }
    } else {
      currentData = if (QuickTimeMetadataDirectory._tagIntegerMap.containsKey(atom.type)) {
        atom.type
      } else {
        null
      }
    }
    return this
  }

  @Throws(IOException::class)
  override fun processData(payload: ByteArray, reader: SequentialByteArrayReader) { // 4 bytes: type indicator
    // 4 bytes: locale indicator
    reader.skip(8)
    val value = String(reader.getBytes(payload.size - 8))
    QuickTimeMetadataDirectory._tagIntegerMap[currentData]?.let {
      directory.setString(it, value)
    }
  }

  @Throws(IOException::class)
  override fun processKeys(reader: SequentialByteArrayReader) { // Do nothing
  }
}
