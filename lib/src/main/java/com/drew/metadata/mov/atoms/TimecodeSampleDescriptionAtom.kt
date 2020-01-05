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
import com.drew.metadata.mov.atoms.TimecodeSampleDescriptionAtom.TimecodeSampleDescription
import com.drew.metadata.mov.media.QuickTimeTimecodeDirectory
import java.io.IOException

/**
 * https://developer.apple.com/library/content/documentation/QuickTime/QTFF/QTFFChap3/qtff3.html#//apple_ref/doc/uid/TP40000939-CH205-57409
 *
 * @author Payton Garland
 */
class TimecodeSampleDescriptionAtom(reader: SequentialReader, atom: Atom) : SampleDescriptionAtom<TimecodeSampleDescription>(reader, atom) {
  @Throws(IOException::class)
  override fun getSampleDescription(reader: SequentialReader): TimecodeSampleDescription? {
    return TimecodeSampleDescription(reader)
  }

  fun addMetadata(directory: QuickTimeTimecodeDirectory) {
    val description = sampleDescriptions[0] ?: return
    directory.setBoolean(QuickTimeTimecodeDirectory.TAG_DROP_FRAME, description.flags and 0x0001 == 0x0001)
    directory.setBoolean(QuickTimeTimecodeDirectory.TAG_24_HOUR_MAX, description.flags and 0x0002 == 0x0002)
    directory.setBoolean(QuickTimeTimecodeDirectory.TAG_NEGATIVE_TIMES_OK, description.flags and 0x0004 == 0x0004)
    directory.setBoolean(QuickTimeTimecodeDirectory.TAG_COUNTER, description.flags and 0x0008 == 0x0008)
  }

  class TimecodeSampleDescription(reader: SequentialReader) : SampleDescription(reader) {
    var flags: Int
    var timeScale: Int
    var frameDuration: Int
    var numberOfFrames: Int

    init {
      reader.skip(4) // Reserved
      flags = reader.getInt32()
      timeScale = reader.getInt32()
      frameDuration = reader.getInt32()
      numberOfFrames = reader.getInt8().toInt()
      reader.skip(1) // Reserved
      // Source reference...
    }
  }
}
