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
package com.drew.metadata.mp4.media

import com.drew.lang.SequentialReader
import com.drew.metadata.Metadata
import com.drew.metadata.mp4.Mp4BoxTypes
import com.drew.metadata.mp4.Mp4Context
import com.drew.metadata.mp4.Mp4MediaHandler
import com.drew.metadata.mp4.boxes.Box
import com.drew.metadata.mp4.boxes.HintMediaHeaderBox
import java.io.IOException

class Mp4HintHandler(metadata: Metadata, context: Mp4Context) : Mp4MediaHandler<Mp4HintDirectory>(metadata, context) {
  override val directory: Mp4HintDirectory
    get() = Mp4HintDirectory()

  override val mediaInformation: String
    get() = Mp4BoxTypes.BOX_HINT_MEDIA_INFO

  @Throws(IOException::class)
  override fun processSampleDescription(reader: SequentialReader, box: Box) {
  }

  @Throws(IOException::class)
  override fun processMediaInformation(reader: SequentialReader, box: Box) {
    val hintMediaHeaderBox = HintMediaHeaderBox(reader, box)
    hintMediaHeaderBox.addMetadata(directory)
  }

  @Throws(IOException::class)
  override fun processTimeToSample(reader: SequentialReader, box: Box, context: Mp4Context) {
  }
}
