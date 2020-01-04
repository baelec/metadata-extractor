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
package com.drew.metadata.png

import com.drew.imaging.png.PngColorType
import com.drew.lang.KeyValuePair
import com.drew.lang.SequentialByteArrayReader
import com.drew.lang.SequentialReader
import com.drew.metadata.TagDescriptor
import java.io.IOException

/**
 * @author Drew Noakes https://drewnoakes.com
 */
class PngDescriptor(directory: PngDirectory) : TagDescriptor<PngDirectory>(directory) {
  override fun getDescription(tagType: Int): String? {
    return when (tagType) {
      PngDirectory.TAG_COLOR_TYPE -> colorTypeDescription
      PngDirectory.TAG_COMPRESSION_TYPE -> compressionTypeDescription
      PngDirectory.TAG_FILTER_METHOD -> filterMethodDescription
      PngDirectory.TAG_INTERLACE_METHOD -> interlaceMethodDescription
      PngDirectory.TAG_PALETTE_HAS_TRANSPARENCY -> paletteHasTransparencyDescription
      PngDirectory.TAG_SRGB_RENDERING_INTENT -> isSrgbColorSpaceDescription
      PngDirectory.TAG_TEXTUAL_DATA -> textualDataDescription
      PngDirectory.TAG_BACKGROUND_COLOR -> backgroundColorDescription
      PngDirectory.TAG_UNIT_SPECIFIER -> unitSpecifierDescription
      else -> super.getDescription(tagType)
    }
  }

  val colorTypeDescription: String?
    get() {
      val value = _directory.getInteger(PngDirectory.TAG_COLOR_TYPE) ?: return null
      val colorType = PngColorType.fromNumericValue(value) ?: return null
      return colorType.description
    }

  val compressionTypeDescription: String?
    get() = getIndexedDescription(PngDirectory.TAG_COMPRESSION_TYPE, "Deflate")

  val filterMethodDescription: String?
    get() = getIndexedDescription(PngDirectory.TAG_FILTER_METHOD, "Adaptive")

  val interlaceMethodDescription: String?
    get() = getIndexedDescription(PngDirectory.TAG_INTERLACE_METHOD, "No Interlace", "Adam7 Interlace")

  val paletteHasTransparencyDescription: String?
    get() = getIndexedDescription(PngDirectory.TAG_PALETTE_HAS_TRANSPARENCY, null, "Yes")

  val isSrgbColorSpaceDescription: String?
    @JvmName("getIsSrgbColorSpaceDescription")
    get() = getIndexedDescription(
      PngDirectory.TAG_SRGB_RENDERING_INTENT,
      "Perceptual",
      "Relative Colorimetric",
      "Saturation",
      "Absolute Colorimetric"
    )

  val unitSpecifierDescription: String?
    get() = getIndexedDescription(
      PngDirectory.TAG_UNIT_SPECIFIER,
      "Unspecified",
      "Metres"
    )

  val textualDataDescription: String?
    get() {
      val obj = _directory.getObject(PngDirectory.TAG_TEXTUAL_DATA) ?: return null
      val keyValues = obj as List<KeyValuePair>
      val sb = StringBuilder()
      for (keyValue in keyValues) {
        if (sb.isNotEmpty()) sb.append('\n')
        sb.append("%s: %s".format(keyValue.key, keyValue.value))
      }
      return sb.toString()
    }

  // TODO do we need to normalise these based upon the bit depth?
  val backgroundColorDescription: String?
    get() {
      val bytes = _directory.getByteArray(PngDirectory.TAG_BACKGROUND_COLOR) ?: return null
      val reader: SequentialReader = SequentialByteArrayReader(bytes)
      try { // TODO do we need to normalise these based upon the bit depth?
        when (bytes.size) {
          1 -> return "Palette Index %d".format(reader.uInt8)
          2 -> return "Greyscale Level %d".format(reader.uInt16)
          6 -> return "R %d, G %d, B %d".format(reader.uInt16, reader.uInt16, reader.uInt16)
        }
      } catch (ex: IOException) {
        return null
      }
      return null
    }
}
