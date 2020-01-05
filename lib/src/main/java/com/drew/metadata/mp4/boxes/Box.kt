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
package com.drew.metadata.mp4.boxes

import com.drew.lang.SequentialReader

/**
 * ISO/IED 14496-12:2015 pg.6
 */
open class Box {
  @JvmField
  var size: Long
  var type: String
  var usertype: String? = null

  constructor(reader: SequentialReader) {
    size = reader.getUInt32()
    type = reader.getString(4)
    if (size == 1L) {
      size = reader.getInt64()
    } else if (size == 0L) {
      size = -1
    }
    if (type == "uuid") {
      usertype = reader.getString(16)
    }
  }

  constructor(box: Box) {
    size = box.size
    type = box.type
    usertype = box.usertype
  }
}
