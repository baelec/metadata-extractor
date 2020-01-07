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
 * Class to hold information about a detected or recognized face in a photo.
 *
 *
 * When a face is *detected*, the camera believes that a face is present at a given location in
 * the image, but is not sure whose face it is.  When a face is *recognised*, then the face is
 * both detected and identified as belonging to a known person.
 *
 * @author Philipp Sandhaus, Drew Noakes
 */
data class Face(val x: Int, val y: Int, val width: Int, val height: Int, val name: String?, val age: Age?) {
  override fun toString(): String {
    var result = "x: $x y: $y width: $width height: $height"
    if (name != null) result = "$result name: $name"
    if (age != null) result = "$result age: ${age.toFriendlyString()}"
    return result
  }
}
