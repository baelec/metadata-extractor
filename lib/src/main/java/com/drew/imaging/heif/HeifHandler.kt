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
package com.drew.imaging.heif

import com.drew.lang.SequentialReader
import com.drew.metadata.Metadata
import com.drew.metadata.heif.HeifDirectory
import com.drew.metadata.heif.boxes.Box
import java.io.IOException

abstract class HeifHandler<T : HeifDirectory>(protected var metadata: Metadata) {
  protected abstract val directory: T
  abstract fun shouldAcceptBox(box: Box): Boolean
  abstract fun shouldAcceptContainer(box: Box): Boolean
  @Throws(IOException::class)
  abstract fun processBox(box: Box, payload: ByteArray): HeifHandler<*>

  /**
   * There is potential for a box to both contain other boxes and contain information, so this method will
   * handle those occurences.
   */
  @Throws(IOException::class)
  abstract fun processContainer(box: Box, reader: SequentialReader)

  init {
    metadata.addDirectory(directory)
  }
}
