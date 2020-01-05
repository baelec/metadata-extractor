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
package com.drew.metadata.photoshop

import com.drew.lang.SequentialReader
import com.drew.metadata.Metadata
import java.io.IOException

/**
 * Reads metadata stored within PSD file format data.
 *
 * @author Drew Noakes https://drewnoakes.com
 */
class PsdReader {
  fun extract(reader: SequentialReader, metadata: Metadata) {
    val directory = PsdHeaderDirectory()
    metadata.addDirectory(directory)
    // FILE HEADER SECTION
    try {
      val signature = reader.getInt32()
      if (signature != 0x38425053) // "8BPS"
      {
        directory.addError("Invalid PSD file signature")
        return
      }
      val version = reader.getUInt16()
      if (version != 1 && version != 2) {
        directory.addError("Invalid PSD file version (must be 1 or 2)")
        return
      }
      // 6 reserved bytes are skipped here.  They should be zero.
      reader.skip(6)
      val channelCount = reader.getUInt16()
      directory.setInt(PsdHeaderDirectory.TAG_CHANNEL_COUNT, channelCount)
      // even though this is probably an unsigned int, the max height in practice is 300,000
      val imageHeight = reader.getInt32()
      directory.setInt(PsdHeaderDirectory.TAG_IMAGE_HEIGHT, imageHeight)
      // even though this is probably an unsigned int, the max width in practice is 300,000
      val imageWidth = reader.getInt32()
      directory.setInt(PsdHeaderDirectory.TAG_IMAGE_WIDTH, imageWidth)
      val bitsPerChannel = reader.getUInt16()
      directory.setInt(PsdHeaderDirectory.TAG_BITS_PER_CHANNEL, bitsPerChannel)
      val colorMode = reader.getUInt16()
      directory.setInt(PsdHeaderDirectory.TAG_COLOR_MODE, colorMode)
    } catch (e: IOException) {
      directory.addError("Unable to read PSD header")
      return
    }
    // COLOR MODE DATA SECTION
    try {
      val sectionLength = reader.getUInt32()
      /*
       * Only indexed color and duotone (see the mode field in the File header section) have color mode data.
       * For all other modes, this section is just the 4-byte length field, which is set to zero.
       *
       * Indexed color images: length is 768; color data contains the color table for the image,
       *                       in non-interleaved order.
       * Duotone images: color data contains the duotone specification (the format of which is not documented).
       *                 Other applications that read Photoshop files can treat a duotone image as a gray	image,
       *                 and just preserve the contents of the duotone information when reading and writing the
       *                 file.
       */
      reader.skip(sectionLength)
    } catch (e: IOException) {
      return
    }
    // IMAGE RESOURCES SECTION
    try {
      val sectionLength = reader.getUInt32()
      assert(sectionLength <= Int.MAX_VALUE)
      PhotoshopReader().extract(reader, sectionLength.toInt(), metadata)
    } catch (e: IOException) { // ignore
    }
    // LAYER AND MASK INFORMATION SECTION (skipped)
    // IMAGE DATA SECTION (skipped)
  }
}
