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
package com.drew.imaging.heif

import com.drew.lang.StreamReader
import com.drew.metadata.Metadata
import com.drew.metadata.heif.boxes.Box
import java.io.IOException
import java.io.InputStream
import java.util.zip.DataFormatException

class HeifReader {
  @Throws(IOException::class, DataFormatException::class)
  fun extract(metadata: Metadata?, inputStream: InputStream, handler: HeifHandler<*>) {
    val reader = StreamReader(inputStream)
    reader.isMotorolaByteOrder = true
    processBoxes(reader, -1, handler)
  }

  fun processBoxes(reader: StreamReader, atomEnd: Long, handler: HeifHandler<*>) {
    var handler = handler
    try {
      while (if (atomEnd == -1L) true else reader.position < atomEnd) {
        val box = Box(reader)
        // Determine if fourCC is container/atom and process accordingly
        // Unknown atoms will be skipped
        if (handler.shouldAcceptContainer(box)) {
          handler.processContainer(box, reader)
          processBoxes(reader, box.size + reader.position - 8, handler)
        } else if (handler.shouldAcceptBox(box)) {
          handler = handler.processBox(box, reader.getBytes(box.size.toInt() - 8))
        } else if (box.size > 1) {
          reader.skip(box.size - 8)
        } else if (box.size == -1L) {
          break
        }
      }
    } catch (e: IOException) { // Currently, reader relies on IOException to end
    }
  }
}
