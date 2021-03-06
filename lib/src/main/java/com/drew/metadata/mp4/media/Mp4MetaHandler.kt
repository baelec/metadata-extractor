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
import com.drew.metadata.mp4.Mp4ContainerTypes
import com.drew.metadata.mp4.Mp4Context
import com.drew.metadata.mp4.Mp4MediaHandler
import com.drew.metadata.mp4.boxes.Box
import java.io.IOException

class Mp4MetaHandler(metadata: Metadata, context: Mp4Context) : Mp4MediaHandler<Mp4MetaDirectory>(metadata, context) {
  override val directory: Mp4MetaDirectory
    get() = Mp4MetaDirectory()

  override val mediaInformation: String
    get() = Mp4ContainerTypes.BOX_MEDIA_NULL

  @Throws(IOException::class)
  override fun processSampleDescription(reader: SequentialReader, box: Box) {
  }

  @Throws(IOException::class)
  override fun processMediaInformation(reader: SequentialReader, box: Box) {
  }

  @Throws(IOException::class)
  override fun processTimeToSample(reader: SequentialReader, box: Box, context: Mp4Context) {
  }
}
