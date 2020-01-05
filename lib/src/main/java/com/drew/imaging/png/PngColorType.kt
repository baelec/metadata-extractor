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
package com.drew.imaging.png

/**
 * @author Drew Noakes https://drewnoakes.com
 */
enum class PngColorType(val numericValue: Int, val description: String, vararg val allowedBitDepths: Int) {
  /**
   * Each pixel is a greyscale sample.
   */
  Greyscale(0, "Greyscale", 1, 2, 4, 8, 16),
  /**
   * Each pixel is an R,G,B triple.
   */
  TrueColor(2, "True Color", 8, 16),
  /**
   * Each pixel is a palette index. Seeing this value indicates that a `PLTE` chunk shall appear.
   */
  IndexedColor(3, "Indexed Color", 1, 2, 4, 8),
  /**
   * Each pixel is a greyscale sample followed by an alpha sample.
   */
  GreyscaleWithAlpha(4, "Greyscale with Alpha", 8, 16),
  /**
   * Each pixel is an R,G,B triple followed by an alpha sample.
   */
  TrueColorWithAlpha(6, "True Color with Alpha", 8, 16);

  companion object {
    @JvmStatic
    fun fromNumericValue(numericValue: Int): PngColorType? {
      val colorTypes = PngColorType::class.java.enumConstants
      for (colorType in colorTypes) {
        if (colorType.numericValue == numericValue) {
          return colorType
        }
      }
      return null
    }
  }

}
