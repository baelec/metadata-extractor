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

import com.drew.lang.SequentialByteArrayReader
import java.io.IOException

/**
 * @author Drew Noakes https://drewnoakes.com
 */
class PngChromaticities(bytes: ByteArray) {
  var whitePointX = 0
  var whitePointY = 0
  var redX = 0
  var redY = 0
  var greenX = 0
  var greenY = 0
  var blueX = 0
  var blueY = 0

  init {
    if (bytes.size != 8 * 4) {
      throw PngProcessingException("Invalid number of bytes")
    }
    val reader = SequentialByteArrayReader(bytes)
    try {
      whitePointX = reader.getInt32()
      whitePointY = reader.getInt32()
      redX = reader.getInt32()
      redY = reader.getInt32()
      greenX = reader.getInt32()
      greenY = reader.getInt32()
      blueX = reader.getInt32()
      blueY = reader.getInt32()
    } catch (ex: IOException) {
      throw PngProcessingException(ex)
    }
  }
}
