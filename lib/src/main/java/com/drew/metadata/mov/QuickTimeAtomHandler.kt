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
import com.drew.metadata.Metadata
import com.drew.metadata.mov.atoms.*
import com.drew.metadata.mov.atoms.canon.CanonThumbnailAtom
import java.io.IOException

/**
 * @author Payton Garland
 */
class QuickTimeAtomHandler(metadata: Metadata) : QuickTimeHandler<QuickTimeDirectory>(metadata, QuickTimeDirectory()) {
  private val handlerFactory = QuickTimeHandlerFactory(this)
  override fun shouldAcceptAtom(atom: Atom): Boolean {
    return atom.type == QuickTimeAtomTypes.ATOM_FILE_TYPE || atom.type == QuickTimeAtomTypes.ATOM_MOVIE_HEADER || atom.type == QuickTimeAtomTypes.ATOM_HANDLER || atom.type == QuickTimeAtomTypes.ATOM_MEDIA_HEADER || atom.type == QuickTimeAtomTypes.ATOM_CANON_THUMBNAIL
  }

  override fun shouldAcceptContainer(atom: Atom): Boolean {
    return atom.type == QuickTimeContainerTypes.ATOM_TRACK || atom.type == QuickTimeContainerTypes.ATOM_USER_DATA || atom.type == QuickTimeContainerTypes.ATOM_METADATA || atom.type == QuickTimeContainerTypes.ATOM_MOVIE || atom.type == QuickTimeContainerTypes.ATOM_MEDIA
  }

  @Throws(IOException::class)
  override fun processAtom(atom: Atom, payload: ByteArray?, context: QuickTimeContext): QuickTimeHandler<*> {
    if (payload == null) {
      if (atom.type == QuickTimeContainerTypes.ATOM_COMPRESSED_MOVIE) {
        directory.addError("Compressed QuickTime movies not supported")
      }
      return this
    }
    val reader: SequentialReader = SequentialByteArrayReader(payload)
    when (atom.type) {
      QuickTimeAtomTypes.ATOM_MOVIE_HEADER -> {
        val movieHeaderAtom = MovieHeaderAtom(reader, atom)
        movieHeaderAtom.addMetadata(directory)
      }
      QuickTimeAtomTypes.ATOM_FILE_TYPE -> {
        val fileTypeCompatibilityAtom = FileTypeCompatibilityAtom(reader, atom)
        fileTypeCompatibilityAtom.addMetadata(directory)
      }
      QuickTimeAtomTypes.ATOM_HANDLER -> {
        val handlerReferenceAtom = HandlerReferenceAtom(reader, atom)
        return handlerFactory.getHandler(handlerReferenceAtom.componentType, metadata, context)
      }
      QuickTimeAtomTypes.ATOM_MEDIA_HEADER -> {
        MediaHeaderAtom(reader, atom, context)
      }
      QuickTimeAtomTypes.ATOM_CANON_THUMBNAIL -> {
        val canonThumbnailAtom = CanonThumbnailAtom(reader)
        canonThumbnailAtom.addMetadata(directory)
      }
    }
    return this
  }
}
