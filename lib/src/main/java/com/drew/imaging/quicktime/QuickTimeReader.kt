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
package com.drew.imaging.quicktime

import com.drew.lang.StreamReader
import com.drew.metadata.mov.QuickTimeContext
import com.drew.metadata.mov.atoms.Atom
import java.io.IOException
import java.io.InputStream

/**
 * @author Payton Garland
 */
object QuickTimeReader {
  fun extract(inputStream: InputStream, handler: QuickTimeHandler<*>) {
    val reader = StreamReader(inputStream)
    reader.isMotorolaByteOrder = true
    val context = QuickTimeContext()
    processAtoms(reader, -1, handler, context)
  }

  private fun processAtoms(reader: StreamReader, atomEnd: Long, handler: QuickTimeHandler<*>, context: QuickTimeContext) {
    var handler = handler
    try {
      while (atomEnd == -1L || reader.position < atomEnd) {
        val atom = Atom(reader)
        // Determine if fourCC is container/atom and process accordingly.
        // Unknown atoms will be skipped
        if (handler.shouldAcceptContainer(atom)) {
          processAtoms(reader, atom.size + reader.position - 8, handler.processContainer(atom, context), context)
        } else if (handler.shouldAcceptAtom(atom)) {
          handler = handler.processAtom(atom, reader.getBytes(atom.size.toInt() - 8), context)
        } else if (atom.size > 1) {
          reader.skip(atom.size - 8)
        } else if (atom.size == -1L) {
          break
        }
      }
    } catch (e: IOException) {
      e.message?.let {
        handler.addError(it)
      }
    }
  }
}
