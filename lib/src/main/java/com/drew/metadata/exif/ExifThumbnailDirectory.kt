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
package com.drew.metadata.exif

import java.util.*

/**
 * One of several Exif directories.  Otherwise known as IFD1, this directory holds information about an embedded thumbnail image.
 *
 * @author Drew Noakes https://drewnoakes.com
 */
class ExifThumbnailDirectory : ExifDirectoryBase() {
  override val tagNameMap = Companion.tagNameMap

  companion object {
    /**
     * The offset to thumbnail image bytes.
     */
    const val TAG_THUMBNAIL_OFFSET = 0x0201
    /**
     * The size of the thumbnail image data in bytes.
     */
    const val TAG_THUMBNAIL_LENGTH = 0x0202

    @Deprecated("use {@link com.drew.metadata.exif.ExifDirectoryBase#TAG_COMPRESSION} instead.")
    val TAG_THUMBNAIL_COMPRESSION = 0x0103
    protected val tagNameMap = HashMap<Int, String>()

    init {
      addExifTagNames(tagNameMap)
      tagNameMap[TAG_THUMBNAIL_OFFSET] = "Thumbnail Offset"
      tagNameMap[TAG_THUMBNAIL_LENGTH] = "Thumbnail Length"
    }
  }

  override val name: String
    get() = "Exif Thumbnail"

  init {
    setDescriptor(ExifThumbnailDescriptor(this))
  }
}
