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
package com.drew.metadata.ico

import com.drew.lang.SequentialReader
import com.drew.metadata.Metadata
import java.io.IOException

/**
 * Reads ICO (Windows Icon) file metadata.
 *
 *  * https://en.wikipedia.org/wiki/ICO_(file_format)
 *
 *
 * @author Drew Noakes https://drewnoakes.com
 */
class IcoReader {
  fun extract(reader: SequentialReader, metadata: Metadata) {
    reader.isMotorolaByteOrder = false
    val type: Int
    val imageCount: Int
    // Read header (ICONDIR structure)
    try {
      val reserved = reader.getUInt16()
      if (reserved != 0) {
        val directory = IcoDirectory()
        directory.addError("Invalid header bytes")
        metadata.addDirectory(directory)
        return
      }
      type = reader.getUInt16()
      if (type != 1 && type != 2) {
        val directory = IcoDirectory()
        directory.addError("Invalid type $type -- expecting 1 or 2")
        metadata.addDirectory(directory)
        return
      }
      imageCount = reader.getUInt16()
      if (imageCount == 0) {
        val directory = IcoDirectory()
        directory.addError("Image count cannot be zero")
        metadata.addDirectory(directory)
        return
      }
    } catch (ex: IOException) {
      val directory = IcoDirectory()
      directory.addError("Exception reading ICO file metadata: ${ex.message}")
      metadata.addDirectory(directory)
      return
    }
    // Read each embedded image
    for (imageIndex in 0 until imageCount) {
      val directory = IcoDirectory()
      try {
        directory.setInt(IcoDirectory.TAG_IMAGE_TYPE, type)
        directory.setInt(IcoDirectory.TAG_IMAGE_WIDTH, reader.getUInt8().toInt())
        directory.setInt(IcoDirectory.TAG_IMAGE_HEIGHT, reader.getUInt8().toInt())
        directory.setInt(IcoDirectory.TAG_COLOUR_PALETTE_SIZE, reader.getUInt8().toInt())
        // Ignore this byte (normally zero, though .NET's System.Drawing.Icon.Save method writes 255)
        reader.getUInt8()
        if (type == 1) { // Icon
          directory.setInt(IcoDirectory.TAG_COLOUR_PLANES, reader.getUInt16())
          directory.setInt(IcoDirectory.TAG_BITS_PER_PIXEL, reader.getUInt16())
        } else { // Cursor
          directory.setInt(IcoDirectory.TAG_CURSOR_HOTSPOT_X, reader.getUInt16())
          directory.setInt(IcoDirectory.TAG_CURSOR_HOTSPOT_Y, reader.getUInt16())
        }
        directory.setLong(IcoDirectory.TAG_IMAGE_SIZE_BYTES, reader.getUInt32())
        directory.setLong(IcoDirectory.TAG_IMAGE_OFFSET_BYTES, reader.getUInt32())
      } catch (ex: IOException) {
        directory.addError("Exception reading ICO file metadata: " + ex.message)
      }
      metadata.addDirectory(directory)
    }
  }
}
