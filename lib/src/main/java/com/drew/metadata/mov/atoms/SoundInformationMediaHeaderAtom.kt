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
import com.drew.metadata.mov.media.QuickTimeSoundDirectory
import kotlin.math.pow

/**
 * https://developer.apple.com/library/content/documentation/QuickTime/QTFF/QTFFChap2/qtff2.html#//apple_ref/doc/uid/TP40000939-CH204-25647
 *
 * @author Payton Garland
 */
class SoundInformationMediaHeaderAtom(reader: SequentialReader, atom: Atom) : FullAtom(reader, atom) {
  var balance: Int = reader.getInt16().toInt()
  fun addMetadata(directory: QuickTimeSoundDirectory) {
    val integerPortion = (balance and ((-0x10000).toDouble()).toInt()).toDouble()
    val fractionPortion = (balance and 0x0000FFFF) / 2.0.pow(4.0)
    directory.setDouble(QuickTimeSoundDirectory.TAG_SOUND_BALANCE, integerPortion + fractionPortion)
  }

  init {
    reader.skip(2) // Reserved
  }
}
