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
package com.drew.imaging.png

import com.drew.imaging.png.PngChunkType
import com.drew.imaging.png.PngProcessingException
import com.drew.lang.SequentialReader
import java.io.IOException
import java.util.*

/**
 * @author Drew Noakes https://drewnoakes.com
 */
class PngChunkReader {
  @Throws(PngProcessingException::class, IOException::class)
  fun extract(reader: SequentialReader, desiredChunkTypes: Set<PngChunkType>?): Iterable<PngChunk> { //
// PNG DATA STREAM
//
// Starts with a PNG SIGNATURE, followed by a sequence of CHUNKS.
//
// PNG SIGNATURE
//
//   Always composed of these bytes: 89 50 4E 47 0D 0A 1A 0A
//
// CHUNK
//
//   4 - length of the data field (unsigned, but always within 31 bytes), may be zero
//   4 - chunk type, restricted to [65,90] and [97,122] (A-Za-z)
//   * - data field
//   4 - CRC calculated from chunk type and chunk data, but not length
//
// CHUNK TYPES
//
//   Critical Chunk Types:
//
//     IHDR - image header, always the first chunk in the data stream
//     PLTE - palette table, associated with indexed PNG images
//     IDAT - image data chunk, of which there may be many
//     IEND - image trailer, always the last chunk in the data stream
//
//   Ancillary Chunk Types:
//
//     Transparency information:  tRNS
//     Colour space information:  cHRM, gAMA, iCCP, sBIT, sRGB
//     Textual information:       iTXt, tEXt, zTXt
//     Miscellaneous information: bKGD, hIST, pHYs, sPLT
//     Time information:          tIME
//
    reader.isMotorolaByteOrder = true // network byte order
    if (!Arrays.equals(PNG_SIGNATURE_BYTES, reader.getBytes(PNG_SIGNATURE_BYTES.size))) {
      throw PngProcessingException("PNG signature mismatch")
    }
    var seenImageHeader = false
    var seenImageTrailer = false
    val chunks: MutableList<PngChunk> = ArrayList()
    val seenChunkTypes: MutableSet<PngChunkType> = HashSet()
    while (!seenImageTrailer) { // Process the next chunk.
      val chunkDataLength = reader.getInt32()
      if (chunkDataLength < 0) throw PngProcessingException("PNG chunk length exceeds maximum")
      val chunkType = PngChunkType(reader.getBytes(4))
      val willStoreChunk = desiredChunkTypes == null || desiredChunkTypes.contains(chunkType)
      val chunkData = reader.getBytes(chunkDataLength)
      // Skip the CRC bytes at the end of the chunk
// TODO consider verifying the CRC value to determine if we're processing bad data
      reader.skip(4)
      if (willStoreChunk && seenChunkTypes.contains(chunkType) && !chunkType.areMultipleAllowed()) {
        throw PngProcessingException(String.format("Observed multiple instances of PNG chunk '%s', for which multiples are not allowed", chunkType))
      }
      if (chunkType == PngChunkType.IHDR) {
        seenImageHeader = true
      } else if (!seenImageHeader) {
        throw PngProcessingException(String.format("First chunk should be '%s', but '%s' was observed", PngChunkType.IHDR, chunkType))
      }
      if (chunkType == PngChunkType.IEND) {
        seenImageTrailer = true
      }
      if (willStoreChunk) {
        chunks.add(PngChunk(chunkType, chunkData))
      }
      seenChunkTypes.add(chunkType)
    }
    return chunks
  }

  companion object {
    private val PNG_SIGNATURE_BYTES = byteArrayOf(0x89.toByte(), 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A)
  }
}
