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
package com.drew.metadata.xmp

import com.adobe.internal.xmp.XMPException
import com.adobe.internal.xmp.XMPMeta
import com.adobe.internal.xmp.impl.XMPMetaImpl
import com.adobe.internal.xmp.options.IteratorOptions
import com.adobe.internal.xmp.properties.XMPPropertyInfo
import com.drew.metadata.Directory
import java.util.*

/**
 * Wraps an instance of Adobe's [XMPMeta] object, which holds XMP data.
 *
 *
 * XMP uses a namespace and path format for identifying values, which does not map to metadata-extractor's
 * integer based tag identifiers. Therefore, XMP data is extracted and exposed via [XmpDirectory.getXMPMeta]
 * which returns an instance of Adobe's [XMPMeta] which exposes the full XMP data set.
 *
 * @author Torsten Skadell
 * @author Drew Noakes https://drewnoakes.com
 */
class XmpDirectory : Directory() {
  override val tagNameMap = Companion.tagNameMap
  companion object {
    const val TAG_XMP_VALUE_COUNT = 0xFFFF
    protected val tagNameMap = HashMap<Int, String>()

    init {
      tagNameMap[TAG_XMP_VALUE_COUNT] = "XMP Value Count"
    }
  }

  private var _xmpMeta: XMPMeta? = null
  override val name: String
    get() = "XMP"

  /**
   * Gets a map of all XMP properties in this directory.
   *
   *
   * This is required because XMP properties are represented as strings, whereas the rest of this library
   * uses integers for keys.
   */
  val xmpProperties: Map<String, String>
    get() {
      val propertyValueByPath: MutableMap<String, String> = HashMap()
      val xmpMeta = _xmpMeta
      if (xmpMeta != null) {
        try {
          val options = IteratorOptions().setJustLeafnodes(true)
          val i: Iterator<*> = xmpMeta.iterator(options)
          while (i.hasNext()) {
            val prop = i.next() as XMPPropertyInfo
            val path = prop.path
            val value = prop.value
            if (path != null && value != null) {
              propertyValueByPath[path] = value
            }
          }
        } catch (ignored: XMPException) {
        }
      }
      return Collections.unmodifiableMap(propertyValueByPath)
    }

  /**
   * Gets the XMPMeta object used to populate this directory. It can be used for more XMP-oriented operations.
   * If one does not exist it will be created.
   */
  var xmpMeta: XMPMeta
    get() {
      var xmpMeta = _xmpMeta
      if (xmpMeta == null) {
        xmpMeta = XMPMetaImpl()
        _xmpMeta = xmpMeta
      }
      return xmpMeta
    }
  set(xmpMeta) {
    _xmpMeta = xmpMeta
    try {
      var valueCount = 0
      val options = IteratorOptions().setJustLeafnodes(true)
      val i: Iterator<*> = xmpMeta.iterator(options)
      while (i.hasNext()) {
        val prop = i.next() as XMPPropertyInfo
        if (prop.path != null) {
          valueCount++
        }
      }
      setInt(TAG_XMP_VALUE_COUNT, valueCount)
    } catch (ignored: XMPException) {
    }
  }

  init {
    setDescriptor(XmpDescriptor(this))
  }
}
