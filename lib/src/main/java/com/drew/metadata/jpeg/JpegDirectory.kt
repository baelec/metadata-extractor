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

import com.drew.metadata.Directory
import com.drew.metadata.MetadataException
import java.util.*

/**
 * Directory of tags and values for the SOF0 JPEG segment.  This segment holds basic metadata about the image.
 *
 * @author Darrell Silver http://www.darrellsilver.com and Drew Noakes https://drewnoakes.com
 */
class JpegDirectory : Directory() {
  override val tagNameMap = Companion.tagNameMap
  companion object {
    const val TAG_COMPRESSION_TYPE = -3
    /** This is in bits/sample, usually 8 (12 and 16 not supported by most software).  */
    const val TAG_DATA_PRECISION = 0
    /** The image's height.  Necessary for decoding the image, so it should always be there.  */
    const val TAG_IMAGE_HEIGHT = 1
    /** The image's width.  Necessary for decoding the image, so it should always be there.  */
    const val TAG_IMAGE_WIDTH = 3
    /**
     * Usually 1 = grey scaled, 3 = color YcbCr or YIQ, 4 = color CMYK
     * Each component TAG_COMPONENT_DATA_[1-4], has the following meaning:
     * component Id(1byte)(1 = Y, 2 = Cb, 3 = Cr, 4 = I, 5 = Q),
     * sampling factors (1byte) (bit 0-3 vertical., 4-7 horizontal.),
     * quantization table number (1 byte).
     *
     *
     * This info is from http://www.funducode.com/freec/Fileformats/format3/format3b.htm
     */
    const val TAG_NUMBER_OF_COMPONENTS = 5
    // NOTE!  Component tag type int values must increment in steps of 1
    /** the first of a possible 4 color components.  Number of components specified in TAG_NUMBER_OF_COMPONENTS.  */
    const val TAG_COMPONENT_DATA_1 = 6
    /** the second of a possible 4 color components.  Number of components specified in TAG_NUMBER_OF_COMPONENTS.  */
    const val TAG_COMPONENT_DATA_2 = 7
    /** the third of a possible 4 color components.  Number of components specified in TAG_NUMBER_OF_COMPONENTS.  */
    const val TAG_COMPONENT_DATA_3 = 8
    /** the fourth of a possible 4 color components.  Number of components specified in TAG_NUMBER_OF_COMPONENTS.  */
    const val TAG_COMPONENT_DATA_4 = 9
    protected val tagNameMap = HashMap<Int, String>()

    init {
      tagNameMap[TAG_COMPRESSION_TYPE] = "Compression Type"
      tagNameMap[TAG_DATA_PRECISION] = "Data Precision"
      tagNameMap[TAG_IMAGE_WIDTH] = "Image Width"
      tagNameMap[TAG_IMAGE_HEIGHT] = "Image Height"
      tagNameMap[TAG_NUMBER_OF_COMPONENTS] = "Number of Components"
      tagNameMap[TAG_COMPONENT_DATA_1] = "Component 1"
      tagNameMap[TAG_COMPONENT_DATA_2] = "Component 2"
      tagNameMap[TAG_COMPONENT_DATA_3] = "Component 3"
      tagNameMap[TAG_COMPONENT_DATA_4] = "Component 4"
    }
  }

  override val name: String
    get() = "JPEG"

  /**
   * @param componentNumber The zero-based index of the component.  This number is normally between 0 and 3.
   * Use getNumberOfComponents for bounds-checking.
   * @return the JpegComponent having the specified number.
   */
  fun getComponent(componentNumber: Int): JpegComponent? {
    val tagType = TAG_COMPONENT_DATA_1 + componentNumber
    return getObject(tagType) as JpegComponent?
  }

  @get:Throws(MetadataException::class)
  val imageWidth: Int
    get() = getInt(TAG_IMAGE_WIDTH)

  @get:Throws(MetadataException::class)
  val imageHeight: Int
    get() = getInt(TAG_IMAGE_HEIGHT)

  @get:Throws(MetadataException::class)
  val numberOfComponents: Int
    get() = getInt(TAG_NUMBER_OF_COMPONENTS)

  init {
    setDescriptor(JpegDescriptor(this))
  }
}
