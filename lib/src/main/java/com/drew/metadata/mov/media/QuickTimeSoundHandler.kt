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
package com.drew.metadata.mov.media

import com.drew.lang.SequentialReader
import com.drew.metadata.Metadata
import com.drew.metadata.mov.QuickTimeAtomTypes
import com.drew.metadata.mov.QuickTimeContext
import com.drew.metadata.mov.QuickTimeMediaHandler
import com.drew.metadata.mov.atoms.Atom
import com.drew.metadata.mov.atoms.SoundInformationMediaHeaderAtom
import com.drew.metadata.mov.atoms.SoundSampleDescriptionAtom
import java.io.IOException

/**
 * @author Payton Garland
 */
class QuickTimeSoundHandler(metadata: Metadata, context: QuickTimeContext) : QuickTimeMediaHandler<QuickTimeSoundDirectory>(metadata, context, QuickTimeSoundDirectory()) {
  override val mediaInformation: String
    protected get() = QuickTimeAtomTypes.ATOM_SOUND_MEDIA_INFO

  @Throws(IOException::class)
  public override fun processSampleDescription(reader: SequentialReader, atom: Atom) {
    val soundSampleDescriptionAtom = SoundSampleDescriptionAtom(reader, atom)
    soundSampleDescriptionAtom.addMetadata(directory)
  }

  @Throws(IOException::class)
  public override fun processMediaInformation(reader: SequentialReader, atom: Atom) {
    val soundInformationMediaHeaderAtom = SoundInformationMediaHeaderAtom(reader, atom)
    soundInformationMediaHeaderAtom.addMetadata(directory)
  }

  @Throws(IOException::class)
  override fun processTimeToSample(reader: SequentialReader, atom: Atom, context: QuickTimeContext) {
    context.timeScale?.let {
      directory.setDouble(QuickTimeSoundDirectory.TAG_AUDIO_SAMPLE_RATE, it.toDouble())
    }
  }
}
