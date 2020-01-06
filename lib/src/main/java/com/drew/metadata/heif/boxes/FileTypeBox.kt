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
package com.drew.metadata.heif.boxes

import com.drew.lang.SequentialReader
import com.drew.metadata.heif.HeifDirectory
import java.util.*

/**
 * ISO/IEC 14496-12:2015 pg.8
 */
class FileTypeBox(reader: SequentialReader, box: Box) : Box(box) {
  var majorBrand: String
  var minorVersion: Long
  var compatibleBrands: ArrayList<String?>
  fun addMetadata(directory: HeifDirectory) {
    directory.setString(HeifDirectory.TAG_MAJOR_BRAND, majorBrand)
    directory.setLong(HeifDirectory.TAG_MINOR_VERSION, minorVersion)
    directory.setStringArray(HeifDirectory.TAG_COMPATIBLE_BRANDS, compatibleBrands.toTypedArray())
  }

  init {
    majorBrand = reader.getString(4)
    minorVersion = reader.getUInt32()
    compatibleBrands = ArrayList()
    var i = 16
    while (i < size) {
      compatibleBrands.add(reader.getString(4))
      i += 4
    }
  }
}
