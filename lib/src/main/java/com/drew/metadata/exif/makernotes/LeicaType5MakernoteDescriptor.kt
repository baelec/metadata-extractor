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
package com.drew.metadata.exif.makernotes

import com.drew.metadata.TagDescriptor

/**
 * Provides human-readable string representations of tag values stored in a [LeicaType5MakernoteDirectory].
 *
 *
 * Tag reference from: http://www.sno.phy.queensu.ca/~phil/exiftool/TagNames/Panasonic.html
 *
 * @author Kevin Mott https://github.com/kwhopper
 * @author Drew Noakes https://drewnoakes.com
 */
class LeicaType5MakernoteDescriptor(directory: LeicaType5MakernoteDirectory) : TagDescriptor<LeicaType5MakernoteDirectory>(directory) {
  override fun getDescription(tagType: Int): String? {
    return when (tagType) {
      LeicaType5MakernoteDirectory.TagExposureMode -> exposureModeDescription
      else -> super.getDescription(tagType)
    }
  }

  // guess
  val exposureModeDescription: String?
    get() {
      val values = _directory.getByteArray(LeicaType5MakernoteDirectory.TagExposureMode)
      if (values == null || values.size < 4) return null
      return when (val join = "%d %d %d %d".format(values[0], values[1], values[2], values[3])) {
        "0 0 0 0" -> "Program AE"
        "1 0 0 0" -> "Aperture-priority AE"
        "1 1 0 0" -> "Aperture-priority AE (1)"
        "2 0 0 0" -> "Shutter speed priority AE" // guess
        "3 0 0 0" -> "Manual"
        else -> String.format("Unknown (%s)", join)
      }
    }
}
