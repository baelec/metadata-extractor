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
package com.drew.metadata.mov.atoms

import com.drew.lang.SequentialReader
import com.drew.metadata.mov.QuickTimeDirectory
import java.util.*

/**
 * https://developer.apple.com/library/content/documentation/QuickTime/QTFF/QTFFChap1/qtff1.html#//apple_ref/doc/uid/TP40000939-CH203-CJBCBIFF
 *
 * @author Payton Garland
 */
class FileTypeCompatibilityAtom(reader: SequentialReader, atom: Atom) : Atom(atom) {
  var majorBrand: String = reader.getString(4)
  var minorVersion: Long = reader.getUInt32()
  var compatibleBrands: ArrayList<String?> = ArrayList((size / 16 shr 2).toInt())
  fun addMetadata(directory: QuickTimeDirectory) {
    directory.setString(QuickTimeDirectory.TAG_MAJOR_BRAND, majorBrand)
    directory.setLong(QuickTimeDirectory.TAG_MINOR_VERSION, minorVersion)
    directory.setStringArray(QuickTimeDirectory.TAG_COMPATIBLE_BRANDS, compatibleBrands.toTypedArray())
  }

  init {
    var i = 16
    while (i < size) {
      compatibleBrands.add(reader.getString(4))
      i += 4
    }
  }
}
