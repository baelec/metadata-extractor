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
package com.drew.metadata.webp

import com.drew.imaging.riff.RiffHandler
import com.drew.lang.ByteArrayReader
import com.drew.lang.RandomAccessReader
import com.drew.metadata.Metadata
import com.drew.metadata.exif.ExifReader
import com.drew.metadata.icc.IccReader
import com.drew.metadata.xmp.XmpReader
import java.io.IOException

/**
 * Implementation of [RiffHandler] specialising in WebP support.
 *
 * Extracts data from chunk types:
 *
 *
 *  * `"VP8X"`: width, height, is animation, has alpha
 *  * `"EXIF"`: full Exif data
 *  * `"ICCP"`: full ICC profile
 *  * `"XMP "`: full XMP data
 *
 */
class WebpRiffHandler(private val _metadata: Metadata) : RiffHandler {
  override fun shouldAcceptRiffIdentifier(identifier: String): Boolean {
    return identifier == WebpDirectory.FORMAT
  }

  override fun shouldAcceptChunk(fourCC: String): Boolean {
    return fourCC == WebpDirectory.CHUNK_VP8X || fourCC == WebpDirectory.CHUNK_VP8L || fourCC == WebpDirectory.CHUNK_VP8 || fourCC == WebpDirectory.CHUNK_EXIF || fourCC == WebpDirectory.CHUNK_ICCP || fourCC == WebpDirectory.CHUNK_XMP
  }

  override fun shouldAcceptList(fourCC: String): Boolean {
    return false
  }

  override fun processChunk(fourCC: String, payload: ByteArray) {
    // System.out.println("Chunk " + fourCC + " " + payload.length + " bytes");
    val directory = WebpDirectory()
    if (fourCC == WebpDirectory.CHUNK_EXIF) {
      ExifReader().extract(ByteArrayReader(payload), _metadata)
    } else if (fourCC == WebpDirectory.CHUNK_ICCP) {
      IccReader().extract(ByteArrayReader(payload), _metadata)
    } else if (fourCC == WebpDirectory.CHUNK_XMP) {
      XmpReader().extract(payload, _metadata)
    } else if (fourCC == WebpDirectory.CHUNK_VP8X && payload.size == 10) {
      val reader: RandomAccessReader = ByteArrayReader(payload)
      reader.isMotorolaByteOrder = false
      try {
        // Flags
        // boolean hasFragments = reader.getBit(0);
        val isAnimation = reader.getBit(1)
        //boolean hasXmp = reader.getBit(2);
        // boolean hasExif = reader.getBit(3);
        val hasAlpha = reader.getBit(4)
        // boolean hasIcc = reader.getBit(5);
        // Image size
        val widthMinusOne = reader.getInt24(4)
        val heightMinusOne = reader.getInt24(7)
        directory.setInt(WebpDirectory.TAG_IMAGE_WIDTH, widthMinusOne + 1)
        directory.setInt(WebpDirectory.TAG_IMAGE_HEIGHT, heightMinusOne + 1)
        directory.setBoolean(WebpDirectory.TAG_HAS_ALPHA, hasAlpha)
        directory.setBoolean(WebpDirectory.TAG_IS_ANIMATION, isAnimation)
        _metadata.addDirectory(directory)
      } catch (e: IOException) {
        e.printStackTrace(System.err)
      }
    } else if (fourCC == WebpDirectory.CHUNK_VP8L && payload.size > 4) {
      val reader: RandomAccessReader = ByteArrayReader(payload)
      reader.isMotorolaByteOrder = false
      try { // https://developers.google.com/speed/webp/docs/webp_lossless_bitstream_specification#2_riff_header
        // Expect the signature byte
        if (reader.getInt8(0).toInt() != 0x2F) return
        val b1 = reader.getUInt8(1).toInt()
        val b2 = reader.getUInt8(2).toInt()
        val b3 = reader.getUInt8(3).toInt()
        val b4 = reader.getUInt8(4).toInt()
        // 14 bits for width
        val widthMinusOne = b2 and 0x3F shl 8 or b1
        // 14 bits for height
        val heightMinusOne = b4 and 0x0F shl 10 or (b3 shl 2) or (b2 and 0xC0 shr 6)
        directory.setInt(WebpDirectory.TAG_IMAGE_WIDTH, widthMinusOne + 1)
        directory.setInt(WebpDirectory.TAG_IMAGE_HEIGHT, heightMinusOne + 1)
        _metadata.addDirectory(directory)
      } catch (e: IOException) {
        e.printStackTrace(System.err)
      }
    } else if (fourCC == WebpDirectory.CHUNK_VP8 && payload.size > 9) {
      val reader: RandomAccessReader = ByteArrayReader(payload)
      reader.isMotorolaByteOrder = false
      try {
        // https://tools.ietf.org/html/rfc6386#section-9.1
        // https://github.com/webmproject/libwebp/blob/master/src/enc/syntax.c#L115
        // Expect the signature bytes
        if (reader.getUInt8(3).toInt() != 0x9D || reader.getUInt8(4).toInt() != 0x01 || reader.getUInt8(5).toInt() != 0x2A) return
        val width = reader.getUInt16(6)
        val height = reader.getUInt16(8)
        directory.setInt(WebpDirectory.TAG_IMAGE_WIDTH, width)
        directory.setInt(WebpDirectory.TAG_IMAGE_HEIGHT, height)
        _metadata.addDirectory(directory)
      } catch (ex: IOException) {
        ex.message?.let {
          directory.addError(it)
        }
      }
    }
  }
}
