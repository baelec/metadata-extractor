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
package com.drew.metadata.jfif

import com.drew.metadata.Directory
import com.drew.metadata.MetadataException
import java.util.*

/**
 * Directory of tags and values for the SOF0 Jfif segment.  This segment holds basic metadata about the image.
 *
 * @author Yuri Binev, Drew Noakes
 */
class JfifDirectory : Directory() {
  override val tagNameMap = Companion.tagNameMap
  companion object {
    const val TAG_VERSION = 5
    /** Units for pixel density fields.  One of None, Pixels per Inch, Pixels per Centimetre.  */
    const val TAG_UNITS = 7
    const val TAG_RESX = 8
    const val TAG_RESY = 10
    const val TAG_THUMB_WIDTH = 12
    const val TAG_THUMB_HEIGHT = 13
    protected val tagNameMap = HashMap<Int, String>()

    init {
      tagNameMap[TAG_VERSION] = "Version"
      tagNameMap[TAG_UNITS] = "Resolution Units"
      tagNameMap[TAG_RESY] = "Y Resolution"
      tagNameMap[TAG_RESX] = "X Resolution"
      tagNameMap[TAG_THUMB_WIDTH] = "Thumbnail Width Pixels"
      tagNameMap[TAG_THUMB_HEIGHT] = "Thumbnail Height Pixels"
    }
  }

  override val name: String
    get() = "JFIF"

  @get:Throws(MetadataException::class)
  val version: Int
    get() = getInt(TAG_VERSION)

  @get:Throws(MetadataException::class)
  val resUnits: Int
    get() = getInt(TAG_UNITS)

  @get:Throws(MetadataException::class)
  @get:Deprecated("use {@link #getResY} instead.")
  val imageWidth: Int
    get() = getInt(TAG_RESY)

  @get:Throws(MetadataException::class)
  val resY: Int
    get() = getInt(TAG_RESY)

  @get:Throws(MetadataException::class)
  @get:Deprecated("use {@link #getResX} instead.")
  val imageHeight: Int
    get() = getInt(TAG_RESX)

  @get:Throws(MetadataException::class)
  val resX: Int
    get() = getInt(TAG_RESX)

  init {
    setDescriptor(JfifDescriptor(this))
  }
}
