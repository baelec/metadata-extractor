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
package com.drew.metadata.pcx

import com.drew.imaging.ImageProcessingException
import com.drew.lang.SequentialReader
import com.drew.metadata.Metadata

/**
 * Reads PCX image file metadata.
 *
 *
 *  * https://courses.engr.illinois.edu/ece390/books/labmanual/graphics-pcx.html
 *  * http://www.fileformat.info/format/pcx/egff.htm
 *  * http://fileformats.archiveteam.org/wiki/PCX
 *
 *
 * @author Drew Noakes https://drewnoakes.com
 */
class PcxReader {
  fun extract(reader: SequentialReader, metadata: Metadata) {
    reader.isMotorolaByteOrder = false
    val directory = PcxDirectory()
    metadata.addDirectory(directory)
    try {
      val identifier = reader.getInt8()
      if (identifier.toInt() != 0x0A) throw ImageProcessingException("Invalid PCX identifier byte")
      directory.setInt(PcxDirectory.TAG_VERSION, reader.getInt8().toInt())
      val encoding = reader.getInt8()
      if (encoding.toInt() != 0x01) throw ImageProcessingException("Invalid PCX encoding byte")
      directory.setInt(PcxDirectory.TAG_BITS_PER_PIXEL, reader.getUInt8().toInt())
      directory.setInt(PcxDirectory.TAG_XMIN, reader.getUInt16())
      directory.setInt(PcxDirectory.TAG_YMIN, reader.getUInt16())
      directory.setInt(PcxDirectory.TAG_XMAX, reader.getUInt16())
      directory.setInt(PcxDirectory.TAG_YMAX, reader.getUInt16())
      directory.setInt(PcxDirectory.TAG_HORIZONTAL_DPI, reader.getUInt16())
      directory.setInt(PcxDirectory.TAG_VERTICAL_DPI, reader.getUInt16())
      directory.setByteArray(PcxDirectory.TAG_PALETTE, reader.getBytes(48))
      reader.skip(1)
      directory.setInt(PcxDirectory.TAG_COLOR_PLANES, reader.getUInt8().toInt())
      directory.setInt(PcxDirectory.TAG_BYTES_PER_LINE, reader.getUInt16())
      val paletteType = reader.getUInt16()
      if (paletteType != 0) directory.setInt(PcxDirectory.TAG_PALETTE_TYPE, paletteType)
      val hScrSize = reader.getUInt16()
      if (hScrSize != 0) directory.setInt(PcxDirectory.TAG_HSCR_SIZE, hScrSize)
      val vScrSize = reader.getUInt16()
      if (vScrSize != 0) directory.setInt(PcxDirectory.TAG_VSCR_SIZE, vScrSize)
    } catch (ex: Exception) {
      directory.addError("Exception reading PCX file metadata: ${ex.message}")
    }
  }
}
