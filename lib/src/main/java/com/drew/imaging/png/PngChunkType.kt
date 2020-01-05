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

import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import java.util.*

/**
 * @author Drew Noakes https://drewnoakes.com
 */
class PngChunkType {
  companion object {
    private val _identifiersAllowingMultiples: Set<String> = HashSet(listOf("IDAT", "sPLT", "iTXt", "tEXt", "zTXt"))
    //
// Standard critical chunks
//
    /**
     * Denotes a critical [PngChunk] that contains basic information about the PNG image.
     * This must be the first chunk in the data sequence, and may only occur once.
     *
     *
     * The format is:
     *
     *  * **pixel width** 4 bytes, unsigned and greater than zero
     *  * **pixel height** 4 bytes, unsigned and greater than zero
     *  * **bit depth** 1 byte, number of bits per sample or per palette index (not per pixel)
     *  * **color type** 1 byte, maps to [PngColorType] enum
     *  * **compression method** 1 byte, currently only a value of zero (deflate/inflate) is in the standard
     *  * **filter method** 1 byte, currently only a value of zero (adaptive filtering with five basic filter types) is in the standard
     *  * **interlace method** 1 byte, indicates the transmission order of image data, currently only 0 (no interlace) and 1 (Adam7 interlace) are in the standard
     *
     */
    @JvmField
    val IHDR: PngChunkType
    /**
     * Denotes a critical [PngChunk] that contains palette entries.
     * This chunk should only appear for a [PngColorType] of `IndexedColor`,
     * and may only occur once in the PNG data sequence.
     *
     *
     * The chunk contains between one and 256 entries, each of three bytes:
     *
     *  * **red** 1 byte
     *  * **green** 1 byte
     *  * **blue** 1 byte
     *
     * The number of entries is determined by the chunk length. A chunk length indivisible by three is an error.
     */
    @JvmField
    val PLTE: PngChunkType
    val IDAT: PngChunkType
    val IEND: PngChunkType
    //
    // Standard ancillary chunks
    //
    @JvmField
    val cHRM: PngChunkType
    @JvmField
    val gAMA: PngChunkType
    @JvmField
    val iCCP: PngChunkType
    @JvmField
    val sBIT: PngChunkType
    @JvmField
    val sRGB: PngChunkType
    @JvmField
    val bKGD: PngChunkType
    val hIST: PngChunkType
    @JvmField
    val tRNS: PngChunkType
    @JvmField
    val pHYs: PngChunkType
    val sPLT: PngChunkType
    @JvmField
    val tIME: PngChunkType
    @JvmField
    val iTXt: PngChunkType
    /**
     * Denotes an ancillary [PngChunk] that contains textual data, having first a keyword and then a value.
     * If multiple text data keywords are needed, then multiple chunks are included in the PNG data stream.
     *
     *
     * The format is:
     *
     *  * **keyword** 1-79 bytes
     *  * **null separator** 1 byte (\0)
     *  * **text string** 0 or more bytes
     *
     * Text is interpreted according to the Latin-1 character set [ISO-8859-1].
     * Newlines should be represented by a single linefeed character (0x9).
     */
    @JvmField
    val tEXt: PngChunkType
    @JvmField
    val zTXt: PngChunkType
    @Throws(PngProcessingException::class)
    private fun validateBytes(bytes: ByteArray) {
      if (bytes.size != 4) {
        throw PngProcessingException("PNG chunk type identifier must be four bytes in length")
      }
      for (b in bytes) {
        if (!isValidByte(b)) {
          throw PngProcessingException("PNG chunk type identifier may only contain alphabet characters")
        }
      }
    }

    private fun isLowerCase(b: Byte): Boolean {
      return b.toInt() and (1 shl 5) != 0
    }

    private fun isUpperCase(b: Byte): Boolean {
      return b.toInt() and (1 shl 5) == 0
    }

    private fun isValidByte(b: Byte): Boolean {
      return b in 65..90 || b in 97..122
    }

    init {
      try {
        IHDR = PngChunkType("IHDR")
        PLTE = PngChunkType("PLTE")
        IDAT = PngChunkType("IDAT", true)
        IEND = PngChunkType("IEND")
        cHRM = PngChunkType("cHRM")
        gAMA = PngChunkType("gAMA")
        iCCP = PngChunkType("iCCP")
        sBIT = PngChunkType("sBIT")
        sRGB = PngChunkType("sRGB")
        bKGD = PngChunkType("bKGD")
        hIST = PngChunkType("hIST")
        tRNS = PngChunkType("tRNS")
        pHYs = PngChunkType("pHYs")
        sPLT = PngChunkType("sPLT", true)
        tIME = PngChunkType("tIME")
        iTXt = PngChunkType("iTXt", true)
        tEXt = PngChunkType("tEXt", true)
        zTXt = PngChunkType("zTXt", true)
      } catch (e: PngProcessingException) {
        throw IllegalArgumentException(e)
      }
    }
  }

  private val _bytes: ByteArray
  private val _multipleAllowed: Boolean

  @JvmOverloads
  constructor(identifier: String, multipleAllowed: Boolean = false) {
    _multipleAllowed = multipleAllowed
    _bytes = try {
      val bytes = identifier.toByteArray(charset("ASCII"))
      validateBytes(bytes)
      bytes
    } catch (e: UnsupportedEncodingException) {
      throw IllegalArgumentException("Unable to convert string code to bytes.")
    }
  }

  constructor(bytes: ByteArray) {
    validateBytes(bytes)
    _bytes = bytes
    _multipleAllowed = _identifiersAllowingMultiples.contains(identifier)
  }

  val isCritical: Boolean
    get() = isUpperCase(_bytes[0])

  val isAncillary: Boolean
    get() = !isCritical

  val isPrivate: Boolean
    get() = isUpperCase(_bytes[1])

  val isSafeToCopy: Boolean
    get() = isLowerCase(_bytes[3])

  fun areMultipleAllowed(): Boolean {
    return _multipleAllowed
  }

  // The constructor should ensure that we're always able to encode the bytes in ASCII.
// noinspection ConstantConditions
  val identifier: String
    get() = try {
      String(_bytes, Charset.forName("ASCII"))
    } catch (e: UnsupportedEncodingException) { // The constructor should ensure that we're always able to encode the bytes in ASCII.
// noinspection ConstantConditions
      assert(false)
      "Invalid object instance"
    }

  override fun toString(): String {
    return identifier
  }

  override fun equals(o: Any?): Boolean {
    if (this === o) return true
    if (o == null || javaClass != o.javaClass) return false
    val that = o as PngChunkType
    return _bytes.contentEquals(that._bytes)
  }

  override fun hashCode(): Int {
    return _bytes.contentHashCode()
  }
}
