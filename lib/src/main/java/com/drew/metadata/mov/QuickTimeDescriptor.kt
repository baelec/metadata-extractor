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
package com.drew.metadata.mov

import com.drew.metadata.TagDescriptor
import com.drew.metadata.mov.QuickTimeDictionary.lookup
import java.util.*
import kotlin.math.ceil
import kotlin.math.pow

/**
 * @author Payton Garland
 */
open class QuickTimeDescriptor(directory: QuickTimeDirectory) : TagDescriptor<QuickTimeDirectory>(directory) {
  override fun getDescription(tagType: Int): String? {
    return when (tagType) {
      QuickTimeDirectory.TAG_MAJOR_BRAND -> majorBrandDescription
      QuickTimeDirectory.TAG_COMPATIBLE_BRANDS -> compatibleBrandsDescription
      QuickTimeDirectory.TAG_DURATION_SECONDS -> durationDescription
      else -> super.getDescription(tagType)
    }
  }

  private val majorBrandDescription: String?
    get() {
      val value = _directory.getByteArray(QuickTimeDirectory.TAG_MAJOR_BRAND) ?: return null
      return lookup(QuickTimeDirectory.TAG_MAJOR_BRAND, String(value))
    }

  private val compatibleBrandsDescription: String?
    get() {
      val values = _directory.getStringArray(QuickTimeDirectory.TAG_COMPATIBLE_BRANDS) ?: return null
      val compatibleBrandsValues = ArrayList<String>()
      for (value in values) {
        val compatibleBrandsValue = lookup(QuickTimeDirectory.TAG_MAJOR_BRAND, value)
        compatibleBrandsValues.add(compatibleBrandsValue)
      }
      return compatibleBrandsValues.toTypedArray().contentToString()
    }

  private val durationDescription: String?
    get() {
      val duration = _directory.getRational(QuickTimeDirectory.TAG_DURATION_SECONDS) ?: return null
      val value: Double = duration.toDouble()
      val hours = (value / 60.0.pow(2.0)).toInt()
      val minutes = (value / 60.0.pow(1.0) - hours * 60).toInt()
      val seconds = ceil(value / 60.0.pow(0.0) - minutes * 60).toInt()
      return "%1$02d:%2$02d:%3$02d".format(hours, minutes, seconds)
    }
}
