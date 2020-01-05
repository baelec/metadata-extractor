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
package com.drew.metadata.mov.metadata

import com.drew.metadata.mov.QuickTimeDescriptor
import com.drew.metadata.mov.QuickTimeDirectory

/**
 * @author Payton Garland
 */
class QuickTimeMetadataDescriptor(directory: QuickTimeDirectory) : QuickTimeDescriptor(directory) {
  override fun getDescription(tagType: Int): String? {
    return when (tagType) {
      QuickTimeMetadataDirectory.TAG_ARTWORK -> artworkDescription
      QuickTimeMetadataDirectory.TAG_LOCATION_ROLE -> locationRoleDescription
      else -> super.getDescription(tagType)
    }
  }

  private val artworkDescription: String?
    private get() = getByteLengthDescription(QuickTimeMetadataDirectory.TAG_ARTWORK)

  private val locationRoleDescription: String?
    private get() = getIndexedDescription(QuickTimeMetadataDirectory.TAG_LOCATION_ROLE, 0,
      "Shooting location",
      "Real location",
      "Fictional location"
    )
}
