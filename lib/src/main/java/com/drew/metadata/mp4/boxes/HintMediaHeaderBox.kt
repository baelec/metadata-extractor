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
import com.drew.metadata.mp4.media.Mp4HintDirectory

/**
 * ISO/IED 14496-12:2015 pg.169
 */
class HintMediaHeaderBox(reader: SequentialReader, box: Box) : FullBox(reader, box) {
  var maxPDUsize: Int = reader.getUInt16()
  var avgPDUsize: Int = reader.getUInt16()
  var maxbitrate: Long = reader.getUInt32()
  var avgbitrate: Long = reader.getUInt32()
  fun addMetadata(directory: Mp4HintDirectory) {
    directory.setInt(Mp4HintDirectory.TAG_MAX_PDU_SIZE, maxPDUsize)
    directory.setInt(Mp4HintDirectory.TAG_AVERAGE_PDU_SIZE, avgPDUsize)
    directory.setLong(Mp4HintDirectory.TAG_MAX_BITRATE, maxbitrate)
    directory.setLong(Mp4HintDirectory.TAG_AVERAGE_BITRATE, avgbitrate)
  }
}
