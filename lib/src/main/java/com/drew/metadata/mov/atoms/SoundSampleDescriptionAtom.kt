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
import com.drew.metadata.mov.QuickTimeDictionary.setLookup
import com.drew.metadata.mov.atoms.SoundSampleDescriptionAtom.SoundSampleDescription
import com.drew.metadata.mov.media.QuickTimeSoundDirectory
import java.io.IOException

/**
 * https://developer.apple.com/library/content/documentation/QuickTime/QTFF/QTFFChap3/qtff3.html#//apple_ref/doc/uid/TP40000939-CH205-BBCGGHJH
 *
 * @author Payton Garland
 */
class SoundSampleDescriptionAtom(reader: SequentialReader, atom: Atom) : SampleDescriptionAtom<SoundSampleDescription>(reader, atom) {
  @Throws(IOException::class)
  override fun getSampleDescription(reader: SequentialReader): SoundSampleDescription {
    return SoundSampleDescription(reader)
  }

  fun addMetadata(directory: QuickTimeSoundDirectory) {
    val description = sampleDescriptions[0] ?: return
    setLookup(QuickTimeSoundDirectory.TAG_AUDIO_FORMAT, description.dataFormat, directory)
    directory.setInt(QuickTimeSoundDirectory.TAG_NUMBER_OF_CHANNELS, description.numberOfChannels)
    directory.setInt(QuickTimeSoundDirectory.TAG_AUDIO_SAMPLE_SIZE, description.sampleSize)
  }

  class SoundSampleDescription(reader: SequentialReader) : SampleDescription(reader) {
    var version: Int = reader.getUInt16()
    var revisionLevel: Int = reader.getUInt16()
    var vendor: Int = reader.getInt32()
    var numberOfChannels: Int = reader.getUInt16()
    var sampleSize: Int = reader.getUInt16()
    var compressionID: Int = reader.getUInt16()
    var packetSize: Int = reader.getUInt16()
    var sampleRate: Long = reader.getUInt32()
  }
}
