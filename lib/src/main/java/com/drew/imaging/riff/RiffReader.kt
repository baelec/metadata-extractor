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
package com.drew.imaging.riff

import com.drew.lang.SequentialReader
import java.io.IOException

/**
 * Processes RIFF-formatted data, calling into client code via that [RiffHandler] interface.
 *
 *
 * For information on this file format, see:
 *
 *  * http://en.wikipedia.org/wiki/Resource_Interchange_File_Format
 *  * https://developers.google.com/speed/webp/docs/riff_container
 *  * https://www.daubnet.com/en/file-format-riff
 *
 * @author Drew Noakes https://drewnoakes.com
 * @author Payton Garland
 */
class RiffReader {
  /**
   * Processes a RIFF data sequence.
   *
   * @param reader the [SequentialReader] from which the data should be read
   * @param handler the [RiffHandler] that will coordinate processing and accept read values
   * @throws RiffProcessingException if an error occurred during the processing of RIFF data that could not be
   * ignored or recovered from
   * @throws IOException an error occurred while accessing the required data
   */
  @Throws(RiffProcessingException::class, IOException::class)
  fun processRiff(reader: SequentialReader,
                  handler: RiffHandler) { // RIFF files are always little-endian
    reader.isMotorolaByteOrder = false
    // PROCESS FILE HEADER
    val fileFourCC = reader.getString(4)
    if (fileFourCC != "RIFF") throw RiffProcessingException("Invalid RIFF header: $fileFourCC")
    // The total size of the chunks that follow plus 4 bytes for the FourCC
    val fileSize = reader.getInt32()
    var sizeLeft = fileSize
    val identifier = reader.getString(4)
    sizeLeft -= 4
    if (!handler.shouldAcceptRiffIdentifier(identifier)) return
    // PROCESS CHUNKS
    processChunks(reader, sizeLeft, handler)
  }

  @Throws(IOException::class)
  fun processChunks(reader: SequentialReader, sectionSize: Int, handler: RiffHandler) {
    while (reader.position < sectionSize) {
      val fourCC = String(reader.getBytes(4))
      val size = reader.getInt32()
      if (fourCC == "LIST" || fourCC == "RIFF") {
        val listName = String(reader.getBytes(4))
        if (handler.shouldAcceptList(listName)) {
          processChunks(reader, size - 4, handler)
        } else {
          reader.skip(size - 4.toLong())
        }
      } else if (fourCC == "IDIT") { // Avi DateTimeOriginal
        handler.processChunk(fourCC, reader.getBytes(size - 2))
        reader.skip(2) // ?0A 00?
      } else {
        if (handler.shouldAcceptChunk(fourCC)) { // TODO is it feasible to avoid copying the chunk here, and to pass the sequential reader to the handler?
          handler.processChunk(fourCC, reader.getBytes(size))
        } else {
          reader.skip(size.toLong())
        }
        // Bytes read must be even - skip one if not
        if (size and 1 == 1) {
          reader.skip(1)
        }
      }
    }
  }
}
