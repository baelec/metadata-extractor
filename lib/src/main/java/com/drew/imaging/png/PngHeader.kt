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

import com.drew.imaging.png.PngColorType.Companion.fromNumericValue
import com.drew.lang.SequentialByteArrayReader
import com.drew.lang.SequentialReader
import java.io.IOException

/**
 * @author Drew Noakes https://drewnoakes.com
 */
class PngHeader(bytes: ByteArray) {
  var imageWidth = 0
  var imageHeight = 0
  var bitsPerSample: Byte = 0
  val colorType: PngColorType
  var compressionType: Byte = 0
  var filterMethod: Byte = 0
  var interlaceMethod: Byte = 0

  init {
    if (bytes.size != 13) {
      throw PngProcessingException("PNG header chunk must have 13 data bytes")
    }
    val reader: SequentialReader = SequentialByteArrayReader(bytes)
    try {
      imageWidth = reader.getInt32()
      imageHeight = reader.getInt32()
      bitsPerSample = reader.getInt8()
      val colorTypeNumber = reader.getInt8()
      val colorType = fromNumericValue(colorTypeNumber.toInt())
        ?: throw PngProcessingException("Unexpected PNG color type: $colorTypeNumber")
      this.colorType = colorType
      compressionType = reader.getInt8()
      filterMethod = reader.getInt8()
      interlaceMethod = reader.getInt8()
    } catch (e: IOException) {
      // Should never happen
      throw PngProcessingException(e)
    }
  }
}
