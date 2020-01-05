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
package com.drew.metadata.mp4

import com.drew.imaging.mp4.Mp4Handler
import com.drew.lang.SequentialByteArrayReader
import com.drew.lang.SequentialReader
import com.drew.metadata.Metadata
import com.drew.metadata.mp4.boxes.*
import java.io.IOException

/**
 * @author Payton Garland
 */
class Mp4BoxHandler(metadata: Metadata) : Mp4Handler<Mp4Directory>(metadata) {
  private val handlerFactory = Mp4HandlerFactory(this)
  override val directory = Mp4Directory()

  override fun shouldAcceptBox(box: Box): Boolean {
    return box.type == Mp4BoxTypes.BOX_FILE_TYPE || box.type == Mp4BoxTypes.BOX_MOVIE_HEADER || box.type == Mp4BoxTypes.BOX_HANDLER || box.type == Mp4BoxTypes.BOX_MEDIA_HEADER || box.type == Mp4BoxTypes.BOX_TRACK_HEADER
  }

  override fun shouldAcceptContainer(box: Box): Boolean {
    return box.type == Mp4ContainerTypes.BOX_TRACK || box.type == Mp4ContainerTypes.BOX_METADATA || box.type == Mp4ContainerTypes.BOX_MOVIE || box.type == Mp4ContainerTypes.BOX_MEDIA
  }

  @Throws(IOException::class)
  override fun processBox(box: Box, payload: ByteArray?, context: Mp4Context): Mp4Handler<*> {
    if (payload != null) {
      val reader: SequentialReader = SequentialByteArrayReader(payload)
      when (box.type) {
        Mp4BoxTypes.BOX_MOVIE_HEADER -> {
          processMovieHeader(reader, box)
        }
        Mp4BoxTypes.BOX_FILE_TYPE -> {
          processFileType(reader, box)
        }
        Mp4BoxTypes.BOX_HANDLER -> {
          val handlerBox = HandlerBox(reader, box)
          return handlerFactory.getHandler(handlerBox, metadata, context)
        }
        Mp4BoxTypes.BOX_MEDIA_HEADER -> {
          processMediaHeader(reader, box, context)
        }
        Mp4BoxTypes.BOX_TRACK_HEADER -> {
          processTrackHeader(reader, box)
        }
      }
    } else {
      if (box.type == Mp4ContainerTypes.BOX_COMPRESSED_MOVIE) {
        directory.addError("Compressed MP4 movies not supported")
      }
    }
    return this
  }

  @Throws(IOException::class)
  private fun processFileType(reader: SequentialReader, box: Box) {
    val fileTypeBox = FileTypeBox(reader, box)
    fileTypeBox.addMetadata(directory)
  }

  @Throws(IOException::class)
  private fun processMovieHeader(reader: SequentialReader, box: Box) {
    val movieHeaderBox = MovieHeaderBox(reader, box)
    movieHeaderBox.addMetadata(directory)
  }

  @Throws(IOException::class)
  private fun processMediaHeader(reader: SequentialReader, box: Box, context: Mp4Context) {
    MediaHeaderBox(reader, box, context)
  }

  @Throws(IOException::class)
  private fun processTrackHeader(reader: SequentialReader, box: Box) {
    val trackHeaderBox = TrackHeaderBox(reader, box)
    trackHeaderBox.addMetadata(directory)
  }
}
