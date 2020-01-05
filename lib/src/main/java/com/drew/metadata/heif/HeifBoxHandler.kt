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
package com.drew.metadata.heif

import com.drew.imaging.heif.HeifHandler
import com.drew.lang.SequentialByteArrayReader
import com.drew.lang.SequentialReader
import com.drew.metadata.Metadata
import com.drew.metadata.heif.boxes.Box
import com.drew.metadata.heif.boxes.FileTypeBox
import com.drew.metadata.heif.boxes.FullBox
import com.drew.metadata.heif.boxes.HandlerBox
import java.io.IOException
import java.util.*

/**
 * @author Payton Garland
 */
class HeifBoxHandler(metadata: Metadata) : HeifHandler<HeifDirectory>(metadata) {
  var handlerBox: HandlerBox? = null
  private val handlerFactory = HeifHandlerFactory(this)
  override val directory = HeifDirectory()

  override fun shouldAcceptBox(box: Box): Boolean {
    val boxes = listOf(HeifBoxTypes.BOX_FILE_TYPE,
      HeifBoxTypes.BOX_HANDLER,
      HeifBoxTypes.BOX_HVC1)
    return boxes.contains(box.type)
  }

  override fun shouldAcceptContainer(box: Box): Boolean {
    return box.type == HeifContainerTypes.BOX_METADATA || box.type == HeifContainerTypes.BOX_IMAGE_PROPERTY || box.type == HeifContainerTypes.BOX_ITEM_PROPERTY
  }

  @Throws(IOException::class)
  override fun processBox(box: Box, payload: ByteArray): HeifHandler<*> {
    val reader: SequentialReader = SequentialByteArrayReader(payload)
    if (box.type == HeifBoxTypes.BOX_FILE_TYPE) {
      processFileType(reader, box)
    } else if (box.type == HeifBoxTypes.BOX_HANDLER) {
      handlerBox = HandlerBox(reader, box)
      return handlerFactory.getHandler(handlerBox, metadata)
    }
    return this
  }

  @Throws(IOException::class)
  override fun processContainer(box: Box, reader: SequentialReader) {
    if (box.type == HeifContainerTypes.BOX_METADATA) {
      FullBox(reader, box)
    }
  }

  @Throws(IOException::class)
  private fun processFileType(reader: SequentialReader, box: Box) {
    val fileTypeBox = FileTypeBox(reader, box)
    fileTypeBox.addMetadata(directory)
    if (!fileTypeBox.compatibleBrands.contains("mif1")) {
      directory.addError("File Type Box does not contain required brand, mif1")
    }
  }
}
