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
package com.drew.metadata.jpeg

import java.io.Serializable

/**
 * Stores information about a JPEG image component such as the component id, horiz/vert sampling factor and
 * quantization table number.
 *
 * @author Drew Noakes https://drewnoakes.com
 */
data class JpegComponent(val componentId: Int, private val _samplingFactorByte: Int, val quantizationTableNumber: Int) : Serializable {

  /**
   * Returns the component name (one of: Y, Cb, Cr, I, or Q)
   * @return the component name
   */
  val componentName: String
    get() = when (componentId) {
      1 -> "Y"
      2 -> "Cb"
      3 -> "Cr"
      4 -> "I"
      5 -> "Q"
      else -> "Unknown (%s)".format(componentId)
    }

  val horizontalSamplingFactor: Int
    get() = _samplingFactorByte shr 4 and 0x0F

  val verticalSamplingFactor: Int
    get() = _samplingFactorByte and 0x0F

  override fun toString(): String {
    return String.format(
      "Quantization table %d, Sampling factors %d horiz/%d vert",
      quantizationTableNumber,
      horizontalSamplingFactor,
      verticalSamplingFactor
    )
  }

  companion object {
    private const val serialVersionUID = 61121257899091914L
  }
}
