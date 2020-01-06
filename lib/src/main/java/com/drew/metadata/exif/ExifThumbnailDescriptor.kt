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

/**
 * Provides human-readable string representations of tag values stored in a [ExifThumbnailDirectory].
 *
 * @author Drew Noakes https://drewnoakes.com
 */
class ExifThumbnailDescriptor(directory: ExifThumbnailDirectory) : ExifDescriptorBase<ExifThumbnailDirectory>(directory) {
  override fun getDescription(tagType: Int): String? {
    return when (tagType) {
      ExifThumbnailDirectory.TAG_THUMBNAIL_OFFSET -> thumbnailOffsetDescription
      ExifThumbnailDirectory.TAG_THUMBNAIL_LENGTH -> thumbnailLengthDescription
      else -> super.getDescription(tagType)
    }
  }

  val thumbnailLengthDescription: String?
    get() {
      val value = _directory!!.getString(ExifThumbnailDirectory.TAG_THUMBNAIL_LENGTH)
      return if (value == null) null else "$value bytes"
    }

  val thumbnailOffsetDescription: String?
    get() {
      val value = _directory!!.getString(ExifThumbnailDirectory.TAG_THUMBNAIL_OFFSET)
      return if (value == null) null else "$value bytes"
    }
}
