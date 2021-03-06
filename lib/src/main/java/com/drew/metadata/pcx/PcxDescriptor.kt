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
package com.drew.metadata.pcx

import com.drew.metadata.TagDescriptor

/**
 * @author Drew Noakes https://drewnoakes.com
 */
class PcxDescriptor(directory: PcxDirectory) : TagDescriptor<PcxDirectory>(directory) {
  override fun getDescription(tagType: Int): String? {
    return when (tagType) {
      PcxDirectory.TAG_VERSION -> versionDescription
      PcxDirectory.TAG_COLOR_PLANES -> colorPlanesDescription
      PcxDirectory.TAG_PALETTE_TYPE -> paletteTypeDescription
      else -> super.getDescription(tagType)
    }
  }

  // Prior to v2.5 of PC Paintbrush, the PCX image file format was considered proprietary information
  // by ZSoft Corporation
  val versionDescription: String?
    get() =// Prior to v2.5 of PC Paintbrush, the PCX image file format was considered proprietary information
      // by ZSoft Corporation
      getIndexedDescription(PcxDirectory.TAG_VERSION,
        "2.5 with fixed EGA palette information",
        null,
        "2.8 with modifiable EGA palette information",
        "2.8 without palette information (default palette)",
        "PC Paintbrush for Windows",
        "3.0 or better")

  val colorPlanesDescription: String?
    get() = getIndexedDescription(PcxDirectory.TAG_COLOR_PLANES, 3,
      "24-bit color",
      "16 colors")

  val paletteTypeDescription: String?
    get() = getIndexedDescription(PcxDirectory.TAG_PALETTE_TYPE, 1,
      "Color or B&W",
      "Grayscale")
}
