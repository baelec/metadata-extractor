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
import com.drew.lang.get1Jan1904EpochDate
import com.drew.metadata.Metadata
import com.drew.metadata.mp4.boxes.Box
import com.drew.metadata.mp4.media.Mp4MediaDirectory
import java.io.IOException

abstract class Mp4MediaHandler<T : Mp4MediaDirectory>(metadata: Metadata, context: Mp4Context) : Mp4Handler<T>(metadata) {
  override fun shouldAcceptBox(box: Box): Boolean {
    return box.type == mediaInformation || box.type == Mp4BoxTypes.BOX_SAMPLE_DESCRIPTION || box.type == Mp4BoxTypes.BOX_TIME_TO_SAMPLE
  }

  override fun shouldAcceptContainer(box: Box): Boolean {
    return box.type == Mp4ContainerTypes.BOX_SAMPLE_TABLE || box.type == Mp4ContainerTypes.BOX_MEDIA_INFORMATION
  }

  @Throws(IOException::class)
  override fun processBox(box: Box, payload: ByteArray?, context: Mp4Context): Mp4Handler<*> {
    if (payload != null) {
      val reader: SequentialReader = SequentialByteArrayReader(payload)
      when (box.type) {
        mediaInformation -> {
          processMediaInformation(reader, box)
        }
        Mp4BoxTypes.BOX_SAMPLE_DESCRIPTION -> {
          processSampleDescription(reader, box)
        }
        Mp4BoxTypes.BOX_TIME_TO_SAMPLE -> {
          processTimeToSample(reader, box, context)
        }
      }
    }
    return this
  }

  protected abstract val mediaInformation: String
  @Throws(IOException::class)
  protected abstract fun processSampleDescription(reader: SequentialReader, box: Box)

  @Throws(IOException::class)
  protected abstract fun processMediaInformation(reader: SequentialReader, box: Box)

  @Throws(IOException::class)
  protected abstract fun processTimeToSample(reader: SequentialReader, box: Box, context: Mp4Context)

  init {
    context.creationTime?.let {
      directory.setDate(
        Mp4MediaDirectory.TAG_CREATION_TIME,
        get1Jan1904EpochDate(it)
      )
    }
    context.modificationTime?.let {
      directory.setDate(
        Mp4MediaDirectory.TAG_MODIFICATION_TIME,
        get1Jan1904EpochDate(it)
      )
    }
    context.language?.let {
      directory.setString(Mp4MediaDirectory.TAG_LANGUAGE_CODE, it)
    }
  }
}
