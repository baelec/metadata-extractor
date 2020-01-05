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
import com.drew.metadata.heif.boxes.*
import java.io.IOException
import java.util.*

/**
 * @author Payton Garland
 */
class HeifPictureHandler(metadata: Metadata) : HeifHandler<HeifDirectory>(metadata) {
  var itemProtectionBox: ItemProtectionBox? = null
  var primaryItemBox: PrimaryItemBox? = null
  var itemInfoBox: ItemInfoBox? = null
  var itemLocationBox: ItemLocationBox? = null
  override fun shouldAcceptBox(box: Box): Boolean {
    val boxes = Arrays.asList(HeifBoxTypes.BOX_ITEM_PROTECTION,
      HeifBoxTypes.BOX_PRIMARY_ITEM,
      HeifBoxTypes.BOX_ITEM_INFO,
      HeifBoxTypes.BOX_ITEM_LOCATION,
      HeifBoxTypes.BOX_IMAGE_SPATIAL_EXTENTS,
      HeifBoxTypes.BOX_AUXILIARY_TYPE_PROPERTY,
      HeifBoxTypes.BOX_IMAGE_ROTATION,
      HeifBoxTypes.BOX_COLOUR_INFO,
      HeifBoxTypes.BOX_PIXEL_INFORMATION)
    return boxes.contains(box.type)
  }

  override fun shouldAcceptContainer(box: Box): Boolean {
    return box.type == HeifContainerTypes.BOX_IMAGE_PROPERTY || box.type == HeifContainerTypes.BOX_ITEM_PROPERTY
  }

  @Throws(IOException::class)
  override fun processBox(box: Box, payload: ByteArray): HeifHandler<*> {
    val reader: SequentialReader = SequentialByteArrayReader(payload)
    when (box.type) {
      HeifBoxTypes.BOX_ITEM_PROTECTION -> {
        itemProtectionBox = ItemProtectionBox(reader, box)
      }
      HeifBoxTypes.BOX_PRIMARY_ITEM -> {
        primaryItemBox = PrimaryItemBox(reader, box)
      }
      HeifBoxTypes.BOX_ITEM_INFO -> {
        itemInfoBox = ItemInfoBox(reader, box).also {
          it.addMetadata(directory)
        }
      }
      HeifBoxTypes.BOX_ITEM_LOCATION -> {
        itemLocationBox = ItemLocationBox(reader, box)
      }
      HeifBoxTypes.BOX_IMAGE_SPATIAL_EXTENTS -> {
        val imageSpatialExtentsProperty = ImageSpatialExtentsProperty(reader, box)
        imageSpatialExtentsProperty.addMetadata(directory)
      }
      HeifBoxTypes.BOX_AUXILIARY_TYPE_PROPERTY -> {
        val auxiliaryTypeProperty = AuxiliaryTypeProperty(reader, box)
      }
      HeifBoxTypes.BOX_IMAGE_ROTATION -> {
        val imageRotationBox = ImageRotationBox(reader, box)
        imageRotationBox.addMetadata(directory)
      }
      HeifBoxTypes.BOX_COLOUR_INFO -> {
        val colourInformationBox = ColourInformationBox(reader, box, metadata)
        colourInformationBox.addMetadata(directory)
      }
      HeifBoxTypes.BOX_PIXEL_INFORMATION -> {
        val pixelInformationBox = PixelInformationBox(reader, box)
        pixelInformationBox.addMetadata(directory)
      }
    }
    return this
  }

  @Throws(IOException::class)
  override fun processContainer(box: Box, reader: SequentialReader) {
  }

  public override val directory: HeifDirectory =  HeifDirectory()
}
