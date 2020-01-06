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

import java.util.*

/**
 * @author Payton Garland
 */
object HeifBoxTypes {
  const val BOX_FILE_TYPE = "ftyp"
  const val BOX_PRIMARY_ITEM = "pitm"
  const val BOX_ITEM_PROTECTION = "ipro"
  const val BOX_ITEM_INFO = "iinf"
  const val BOX_ITEM_LOCATION = "iloc"
  const val BOX_HANDLER = "hdlr"
  const val BOX_HVC1 = "hvc1"
  const val BOX_IMAGE_SPATIAL_EXTENTS = "ispe"
  const val BOX_AUXILIARY_TYPE_PROPERTY = "auxC"
  const val BOX_IMAGE_ROTATION = "irot"
  const val BOX_COLOUR_INFO = "colr"
  const val BOX_PIXEL_INFORMATION = "pixi"
  private val _boxList = ArrayList<String>()

  init {
    _boxList.add(BOX_FILE_TYPE)
    _boxList.add(BOX_ITEM_PROTECTION)
    _boxList.add(BOX_PRIMARY_ITEM)
    _boxList.add(BOX_ITEM_INFO)
    _boxList.add(BOX_ITEM_LOCATION)
    _boxList.add(BOX_HANDLER)
    _boxList.add(BOX_HVC1)
    _boxList.add(BOX_IMAGE_SPATIAL_EXTENTS)
    _boxList.add(BOX_AUXILIARY_TYPE_PROPERTY)
    _boxList.add(BOX_IMAGE_ROTATION)
    _boxList.add(BOX_COLOUR_INFO)
    _boxList.add(BOX_PIXEL_INFORMATION)
  }
}
