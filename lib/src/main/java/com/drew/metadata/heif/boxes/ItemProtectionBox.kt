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
import java.util.*

/**
 * ISO/IEC 14496-12:2015 pg.80, 89-90
 */
class ItemProtectionBox(reader: SequentialReader, box: Box) : FullBox(reader, box) {
  var protectionCount: Int = reader.getUInt16()
  var protectionSchemes: ArrayList<ProtectionSchemeInfoBox>

  class ProtectionSchemeInfoBox(reader: SequentialReader?, box: Box) : Box(box) {
    internal inner class OriginalFormatBox(reader: SequentialReader, box: Box) : Box(reader) {
      var dataFormat: String = reader.getString(4)
    }
  }

  init {
    protectionSchemes = ArrayList(protectionCount)
    for (i in 1..protectionCount) {
      protectionSchemes.add(ProtectionSchemeInfoBox(reader, box))
    }
  }
}
