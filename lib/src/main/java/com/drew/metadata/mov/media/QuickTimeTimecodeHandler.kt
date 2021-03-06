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
import com.drew.metadata.mov.atoms.TimecodeInformationMediaAtom
import com.drew.metadata.mov.atoms.TimecodeSampleDescriptionAtom
import java.io.IOException

/**
 * @author Payton Garland
 */
class QuickTimeTimecodeHandler(metadata: Metadata, context: QuickTimeContext) : QuickTimeMediaHandler<QuickTimeTimecodeDirectory>(metadata, context, QuickTimeTimecodeDirectory()) {
  override val mediaInformation: String
    protected get() = QuickTimeAtomTypes.ATOM_TIMECODE_MEDIA_INFO

  @Throws(IOException::class)
  public override fun processSampleDescription(reader: SequentialReader, atom: Atom) {
    val timecodeSampleDescriptionAtom = TimecodeSampleDescriptionAtom(reader, atom)
    timecodeSampleDescriptionAtom.addMetadata(directory)
  }

  @Throws(IOException::class)
  public override fun processMediaInformation(reader: SequentialReader, atom: Atom) {
    val timecodeInformationMediaAtom = TimecodeInformationMediaAtom(reader, atom)
    timecodeInformationMediaAtom.addMetadata(directory)
  }

  @Throws(IOException::class)
  override fun processTimeToSample(reader: SequentialReader, atom: Atom, context: QuickTimeContext) { // Do nothing
  }
}
