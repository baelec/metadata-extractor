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
package com.drew.lang

import java.io.IOException

/**
 * A checked replacement for [IndexOutOfBoundsException].  Used by [RandomAccessReader].
 *
 * @author Drew Noakes https://drewnoakes.com
 */
class BufferBoundsException : IOException {
  constructor(index: Int, bytesRequested: Int, bufferLength: Long) : super(getMessage(index, bytesRequested, bufferLength)) {}
  constructor(message: String?) : super(message) {}

  companion object {
    private const val serialVersionUID = 2911102837808946396L
    private fun getMessage(index: Int, bytesRequested: Int, bufferLength: Long): String {
      if (index < 0) return String.format("Attempt to read from buffer using a negative index (%d)", index)
      if (bytesRequested < 0) return String.format("Number of requested bytes cannot be negative (%d)", bytesRequested)
      return if (index.toLong() + bytesRequested.toLong() - 1L > Int.MAX_VALUE.toLong()) String.format("Number of requested bytes summed with starting index exceed maximum range of signed 32 bit integers (requested index: %d, requested count: %d)", index, bytesRequested) else String.format("Attempt to read from beyond end of underlying data source (requested index: %d, requested count: %d, max index: %d)",
        index, bytesRequested, bufferLength - 1)
    }
  }
}
