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
package com.drew.metadata.gif

import com.drew.lang.ASCII
import com.drew.lang.ByteArrayReader
import com.drew.lang.SequentialReader
import com.drew.lang.UTF_8
import com.drew.metadata.*
import com.drew.metadata.gif.GifControlDirectory.DisposalMethod.Companion.typeOf
import com.drew.metadata.icc.IccReader
import com.drew.metadata.xmp.XmpReader
import java.io.ByteArrayOutputStream
import java.io.IOException

/**
 * Reader of GIF encoded data.
 *
 * Resources:
 *
 *  * https://wiki.whatwg.org/wiki/GIF
 *  * https://www.w3.org/Graphics/GIF/spec-gif89a.txt
 *  * http://web.archive.org/web/20100929230301/http://www.etsimo.uniovi.es/gifanim/gif87a.txt
 *
 *
 * @author Drew Noakes https://drewnoakes.com
 * @author Kevin Mott https://github.com/kwhopper
 */
class GifReader {
  fun extract(reader: SequentialReader, metadata: Metadata) {
    reader.isMotorolaByteOrder = false
    val header: GifHeaderDirectory
    try {
      header = readGifHeader(reader)
      metadata.addDirectory(header)
    } catch (ex: IOException) {
      metadata.addDirectory(ErrorDirectory("IOException processing GIF data"))
      return
    }
    if (header.hasErrors()) return
    try { // Skip over any global colour table if GlobalColorTable is present.
      var globalColorTableSize: Int? = null
      try {
        val hasGlobalColorTable = header.getBoolean(GifHeaderDirectory.TAG_HAS_GLOBAL_COLOR_TABLE)
        if (hasGlobalColorTable) {
          globalColorTableSize = header.getInteger(GifHeaderDirectory.TAG_COLOR_TABLE_SIZE)
        }
      } catch (e: MetadataException) { // This exception should never occur here.
        metadata.addDirectory(ErrorDirectory("GIF did not had hasGlobalColorTable bit."))
      }
      if (globalColorTableSize != null) { // Colour table has R/G/B byte triplets
        reader.skip(3 * globalColorTableSize.toLong())
      }
      // After the header comes a sequence of blocks
      while (true) {
        var marker: Byte = try {
          reader.getInt8()
        } catch (ex: IOException) {
          return
        }
        when (marker) {
          '!'.toByte() -> {
            readGifExtensionBlock(reader, metadata)
          }
          ','.toByte() -> {
            metadata.addDirectory(readImageBlock(reader))
            // skip image data blocks
            skipBlocks(reader)
          }
          ';'.toByte() -> {
            // terminator
            return
          }
          else -> {
            // Anything other than these types is unexpected.
// GIF87a spec says to keep reading until a separator is found.
// GIF89a spec says file is corrupt.
            metadata.addDirectory(ErrorDirectory("Unknown gif block marker found."))
            return
          }
        }
      }
    } catch (e: IOException) {
      metadata.addDirectory(ErrorDirectory("IOException processing GIF data"))
    }
  }

  companion object {
    private const val GIF_87A_VERSION_IDENTIFIER = "87a"
    private const val GIF_89A_VERSION_IDENTIFIER = "89a"
    @Throws(IOException::class)
    private fun readGifHeader(reader: SequentialReader): GifHeaderDirectory { // FILE HEADER
//
// 3 - signature: "GIF"
// 3 - version: either "87a" or "89a"
//
// LOGICAL SCREEN DESCRIPTOR
//
// 2 - pixel width
// 2 - pixel height
// 1 - screen and color map information flags (0 is LSB)
//       0-2  Size of the global color table
//       3    Color table sort flag (89a only)
//       4-6  Color resolution
//       7    Global color table flag
// 1 - background color index
// 1 - pixel aspect ratio
      val headerDirectory = GifHeaderDirectory()
      val signature = reader.getString(3)
      if (signature != "GIF") {
        headerDirectory.addError("Invalid GIF file signature")
        return headerDirectory
      }
      val version = reader.getString(3)
      if (version != GIF_87A_VERSION_IDENTIFIER && version != GIF_89A_VERSION_IDENTIFIER) {
        headerDirectory.addError("Unexpected GIF version")
        return headerDirectory
      }
      headerDirectory.setString(GifHeaderDirectory.TAG_GIF_FORMAT_VERSION, version)
      // LOGICAL SCREEN DESCRIPTOR
      headerDirectory.setInt(GifHeaderDirectory.TAG_IMAGE_WIDTH, reader.getUInt16())
      headerDirectory.setInt(GifHeaderDirectory.TAG_IMAGE_HEIGHT, reader.getUInt16())
      val flags = reader.getUInt8().toInt()
      // First three bits = (BPP - 1)
      val colorTableSize = 1 shl (flags and 7) + 1
      val bitsPerPixel: Int = (flags and 0x70 shr 4) + 1
      val hasGlobalColorTable = flags shr 7 != 0
      headerDirectory.setInt(GifHeaderDirectory.TAG_COLOR_TABLE_SIZE, colorTableSize)
      if (version == GIF_89A_VERSION_IDENTIFIER) {
        val isColorTableSorted = flags and 8 != 0
        headerDirectory.setBoolean(GifHeaderDirectory.TAG_IS_COLOR_TABLE_SORTED, isColorTableSorted)
      }
      headerDirectory.setInt(GifHeaderDirectory.TAG_BITS_PER_PIXEL, bitsPerPixel)
      headerDirectory.setBoolean(GifHeaderDirectory.TAG_HAS_GLOBAL_COLOR_TABLE, hasGlobalColorTable)
      headerDirectory.setInt(GifHeaderDirectory.TAG_BACKGROUND_COLOR_INDEX, reader.getUInt8().toInt())
      val aspectRatioByte = reader.getUInt8().toInt()
      if (aspectRatioByte != 0) {
        val pixelAspectRatio = ((aspectRatioByte + 15.0) / 64.0).toFloat()
        headerDirectory.setFloat(GifHeaderDirectory.TAG_PIXEL_ASPECT_RATIO, pixelAspectRatio)
      }
      return headerDirectory
    }

    @Throws(IOException::class)
    private fun readGifExtensionBlock(reader: SequentialReader, metadata: Metadata) {
      val extensionLabel = reader.getInt8()
      val blockSizeBytes = reader.getUInt8()
      val blockStartPos = reader.position
      when (extensionLabel) {
        0x01.toByte() -> {
          val plainTextBlock = readPlainTextBlock(reader, blockSizeBytes.toInt())
          if (plainTextBlock != null) metadata.addDirectory(plainTextBlock)
        }
        0xf9.toByte() -> metadata.addDirectory(readControlBlock(reader, blockSizeBytes.toInt()))
        0xfe.toByte() -> metadata.addDirectory(readCommentBlock(reader, blockSizeBytes.toInt()))
        0xff.toByte() -> readApplicationExtensionBlock(reader, blockSizeBytes.toInt(), metadata)
        else -> metadata.addDirectory(ErrorDirectory(String.format("Unsupported GIF extension block with type 0x%02X.", extensionLabel)))
      }
      val skipCount = blockStartPos + blockSizeBytes - reader.position
      if (skipCount > 0) reader.skip(skipCount)
    }

    @Throws(IOException::class)
    private fun readPlainTextBlock(reader: SequentialReader, blockSizeBytes: Int): Directory? { // It seems this extension is deprecated. If somebody finds an image with this in it, could implement here.
// Just skip the entire block for now.
      if (blockSizeBytes != 12) return ErrorDirectory(String.format("Invalid GIF plain text block size. Expected 12, got %d.", blockSizeBytes))
      // skip 'blockSizeBytes' bytes
      reader.skip(12)
      // keep reading and skipping until a 0 byte is reached
      skipBlocks(reader)
      return null
    }

    @Throws(IOException::class)
    private fun readCommentBlock(reader: SequentialReader, blockSizeBytes: Int): GifCommentDirectory {
      val buffer = gatherBytes(reader, blockSizeBytes)
      return GifCommentDirectory(StringValue(buffer, ASCII))
    }

    @Throws(IOException::class)
    private fun readApplicationExtensionBlock(reader: SequentialReader, blockSizeBytes: Int, metadata: Metadata) {
      if (blockSizeBytes != 11) {
        metadata.addDirectory(ErrorDirectory(String.format("Invalid GIF application extension block size. Expected 11, got %d.", blockSizeBytes)))
        return
      }
      val extensionType: String = reader.getString(blockSizeBytes, UTF_8)
      if (extensionType == "XMP DataXMP") { // XMP data extension
        val xmpBytes = gatherBytes(reader)
        val xmpLengh = xmpBytes.size - 257 // Exclude the "magic trailer", see XMP Specification Part 3, 1.1.2 GIF
        if (xmpLengh > 0) { // Only extract valid blocks
          XmpReader().extract(xmpBytes, 0, xmpBytes.size - 257, metadata, null)
        }
      } else if (extensionType == "ICCRGBG1012") { // ICC profile extension
        val iccBytes = gatherBytes(reader, reader.getByte().toInt() and 0xff)
        if (iccBytes.size != 0) IccReader().extract(ByteArrayReader(iccBytes), metadata)
      } else if (extensionType == "NETSCAPE2.0") {
        reader.skip(2)
        // Netscape's animated GIF extension
// Iteration count (0 means infinite)
        val iterationCount = reader.getUInt16()
        // Skip terminator
        reader.skip(1)
        val animationDirectory = GifAnimationDirectory()
        animationDirectory.setInt(GifAnimationDirectory.TAG_ITERATION_COUNT, iterationCount)
        metadata.addDirectory(animationDirectory)
      } else {
        skipBlocks(reader)
      }
    }

    @Throws(IOException::class)
    private fun readControlBlock(reader: SequentialReader, blockSizeBytes: Int): GifControlDirectory {
      var blockSizeBytes = blockSizeBytes
      if (blockSizeBytes < 4) {
        blockSizeBytes = 4
      }
      val directory = GifControlDirectory()
      val packedFields = reader.getUInt8().toInt()
      directory.setObject(GifControlDirectory.TAG_DISPOSAL_METHOD, typeOf(packedFields shr 2 and 7))
      directory.setBoolean(GifControlDirectory.TAG_USER_INPUT_FLAG, packedFields and 2 shr 1 == 1)
      directory.setBoolean(GifControlDirectory.TAG_TRANSPARENT_COLOR_FLAG, packedFields and 1 == 1)
      directory.setInt(GifControlDirectory.TAG_DELAY, reader.getUInt16())
      directory.setInt(GifControlDirectory.TAG_TRANSPARENT_COLOR_INDEX, reader.getUInt8().toInt())
      // skip 0x0 block terminator
      reader.skip(1)
      return directory
    }

    @Throws(IOException::class)
    private fun readImageBlock(reader: SequentialReader): GifImageDirectory {
      val imageDirectory = GifImageDirectory()
      imageDirectory.setInt(GifImageDirectory.TAG_LEFT, reader.getUInt16())
      imageDirectory.setInt(GifImageDirectory.TAG_TOP, reader.getUInt16())
      imageDirectory.setInt(GifImageDirectory.TAG_WIDTH, reader.getUInt16())
      imageDirectory.setInt(GifImageDirectory.TAG_HEIGHT, reader.getUInt16())
      val flags = reader.getByte().toInt()
      val hasColorTable = flags shr 7 != 0
      val isInterlaced = flags and 0x40 != 0
      imageDirectory.setBoolean(GifImageDirectory.TAG_HAS_LOCAL_COLOUR_TABLE, hasColorTable)
      imageDirectory.setBoolean(GifImageDirectory.TAG_IS_INTERLACED, isInterlaced)
      if (hasColorTable) {
        val isColorTableSorted = flags and 0x20 != 0
        imageDirectory.setBoolean(GifImageDirectory.TAG_IS_COLOR_TABLE_SORTED, isColorTableSorted)
        val bitsPerPixel: Int = (flags and 0x7) + 1
        imageDirectory.setInt(GifImageDirectory.TAG_LOCAL_COLOUR_TABLE_BITS_PER_PIXEL, bitsPerPixel)
        // skip color table
        reader.skip(3 * (2 shl (flags and 0x7)).toLong())
      }
      // skip "LZW Minimum Code Size" byte
      reader.getByte()
      return imageDirectory
    }

    @Throws(IOException::class)
    private fun gatherBytes(reader: SequentialReader): ByteArray {
      val bytes = ByteArrayOutputStream()
      val buffer = ByteArray(257)
      while (true) {
        val b = reader.getByte()
        if (b.toInt() == 0) return bytes.toByteArray()
        val bInt: Int = b.toInt() and 0xFF
        buffer[0] = b
        reader.getBytes(buffer, 1, bInt)
        bytes.write(buffer, 0, bInt + 1)
      }
    }

    @Throws(IOException::class)
    private fun gatherBytes(reader: SequentialReader, firstLength: Int): ByteArray {
      val buffer = ByteArrayOutputStream()
      var length = firstLength
      while (length > 0) {
        buffer.write(reader.getBytes(length), 0, length)
        length = reader.getByte().toInt() and 0xff
      }
      return buffer.toByteArray()
    }

    @Throws(IOException::class)
    private fun skipBlocks(reader: SequentialReader) {
      while (true) {
        val length = reader.getUInt8()
        if (length.toInt() == 0) return
        reader.skip(length.toLong())
      }
    }
  }
}
