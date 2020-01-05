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
package com.drew.metadata

/**
 * Models a particular tag within a [com.drew.metadata.Directory] and provides methods for obtaining its value.
 * Immutable.
 *
 * @author Drew Noakes https://drewnoakes.com
 */
class Tag(
  /**
   * Gets the tag type as an int
   *
   * @return the tag type as an int
   */
  val tagType: Int, private val _directory: Directory) {

  /**
   * Gets the tag type in hex notation as a String with padded leading
   * zeroes if necessary (i.e. `0x100e`).
   *
   * @return the tag type as a string in hexadecimal notation
   */
  val tagTypeHex: String
    get() = "0x%04x".format(tagType)

  /**
   * Get a description of the tag's value, considering enumerated values
   * and units.
   *
   * @return a description of the tag's value
   */
  val description: String?
    get() = _directory.getDescription(tagType)

  /**
   * Get whether this tag has a name.
   *
   * If `true`, it may be accessed via [.getTagName].
   * If `false`, [.getTagName] will return a string resembling `"Unknown tag (0x1234)"`.
   *
   * @return whether this tag has a name
   */
  fun hasTagName(): Boolean {
    return _directory.hasTagName(tagType)
  }

  /**
   * Get the name of the tag, such as `Aperture`, or
   * `InteropVersion`.
   *
   * @return the tag's name
   */
  val tagName: String
    get() = _directory.getTagName(tagType)

  /**
   * Get the name of the [com.drew.metadata.Directory] in which the tag exists, such as
   * `Exif`, `GPS` or `Interoperability`.
   *
   * @return name of the [com.drew.metadata.Directory] in which this tag exists
   */
  val directoryName: String
    get() = _directory.name

  /**
   * A basic representation of the tag's type and value.  EG: `[Exif IFD0] FNumber - f/2.8`.
   *
   * @return the tag's type and value
   */
  override fun toString(): String {
    val description = description ?: "${_directory.getString(tagType)} (unable to formulate description)"
    return "[${_directory.name}] $tagName - $description"
  }
}
