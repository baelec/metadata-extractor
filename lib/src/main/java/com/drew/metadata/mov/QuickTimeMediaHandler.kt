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
package com.drew.metadata.mov

import com.drew.imaging.quicktime.QuickTimeHandler
import com.drew.lang.SequentialByteArrayReader
import com.drew.lang.SequentialReader
import com.drew.lang.get1Jan1904EpochDate
import com.drew.metadata.Metadata
import com.drew.metadata.mov.atoms.Atom
import com.drew.metadata.mov.media.QuickTimeMediaDirectory
import java.io.IOException

/**
 * Classes that extend this class should be from the media dat atom types:
 * https://developer.apple.com/library/content/documentation/QuickTime/QTFF/QTFFChap3/qtff3.html#//apple_ref/doc/uid/TP40000939-CH205-SW1
 *
 * @author Payton Garland
 */
abstract class QuickTimeMediaHandler<T : QuickTimeDirectory>(metadata: Metadata, context: QuickTimeContext, directory: T) : QuickTimeHandler<T>(metadata, directory) {
  override fun shouldAcceptAtom(atom: Atom): Boolean {
    return atom.type == mediaInformation || atom.type == QuickTimeAtomTypes.ATOM_SAMPLE_DESCRIPTION || atom.type == QuickTimeAtomTypes.ATOM_TIME_TO_SAMPLE
  }

  override fun shouldAcceptContainer(atom: Atom): Boolean {
    return atom.type == QuickTimeContainerTypes.ATOM_SAMPLE_TABLE || atom.type == QuickTimeContainerTypes.ATOM_MEDIA_INFORMATION || atom.type == QuickTimeContainerTypes.ATOM_MEDIA_BASE || atom.type == "tmcd"
  }

  @Throws(IOException::class)
  override fun processAtom(atom: Atom, payload: ByteArray?, context: QuickTimeContext): QuickTimeMediaHandler<*> {
    if (payload != null) {
      val reader: SequentialReader = SequentialByteArrayReader(payload)
      when (atom.type) {
        mediaInformation -> {
          processMediaInformation(reader, atom)
        }
        QuickTimeAtomTypes.ATOM_SAMPLE_DESCRIPTION -> {
          processSampleDescription(reader, atom)
        }
        QuickTimeAtomTypes.ATOM_TIME_TO_SAMPLE -> {
          processTimeToSample(reader, atom, context)
        }
      }
    }
    return this
  }

  protected abstract val mediaInformation: String?
  @Throws(IOException::class)
  protected abstract fun processSampleDescription(reader: SequentialReader, atom: Atom)

  @Throws(IOException::class)
  protected abstract fun processMediaInformation(reader: SequentialReader, atom: Atom)

  @Throws(IOException::class)
  protected abstract fun processTimeToSample(reader: SequentialReader, atom: Atom, context: QuickTimeContext)

  init {
    context.creationTime?.let {
      directory.setDate(
        QuickTimeMediaDirectory.TAG_CREATION_TIME,
        get1Jan1904EpochDate(it)
      )
    }

    context.modificationTime?.let {
      directory.setDate(
        QuickTimeMediaDirectory.TAG_MODIFICATION_TIME,
        get1Jan1904EpochDate(it)
      )
    }
  }
}
