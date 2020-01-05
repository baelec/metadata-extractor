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
import com.drew.metadata.mov.media.QuickTimeVideoDirectory

/**
 * https://developer.apple.com/library/content/documentation/QuickTime/QTFF/QTFFChap2/qtff2.html#//apple_ref/doc/uid/TP40000939-CH204-25638
 *
 * @author Payton Garland
 */
class VideoInformationMediaHeaderAtom(reader: SequentialReader, atom: Atom) : FullAtom(reader, atom) {
  var graphicsMode: Int = reader.getUInt16()
  var opcolor: IntArray = intArrayOf(reader.getUInt16(), reader.getUInt16(), reader.getUInt16())
  fun addMetadata(directory: QuickTimeVideoDirectory) {
    directory.setIntArray(QuickTimeVideoDirectory.TAG_OPCOLOR, opcolor)
    directory.setInt(QuickTimeVideoDirectory.TAG_GRAPHICS_MODE, graphicsMode)
  }
}
