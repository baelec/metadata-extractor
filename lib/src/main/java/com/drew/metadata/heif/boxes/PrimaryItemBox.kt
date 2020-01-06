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

/**
 * ISO/IEC 14496-12:2015 pg.80
 */
class PrimaryItemBox(reader: SequentialReader, box: Box) : FullBox(reader, box) {
  var itemID: Long = 0

  init {
    itemID = if (version == 0) {
      reader.getUInt16().toLong()
    } else {
      reader.getUInt32()
    }
  }
}
