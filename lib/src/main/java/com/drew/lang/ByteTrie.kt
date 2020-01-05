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

import java.util.*

/**
 * Stores values using a prefix tree (aka 'trie', i.e. reTRIEval data structure).
 *
 * @param <T> the type of value to store for byte sequences
</T> */
class ByteTrie<T> {
  /** A node in the trie. Has children and may have an associated value.  */
  internal class ByteTrieNode<T> {
    internal val _children: MutableMap<Byte, ByteTrieNode<T>> = HashMap()
    internal var _value: T? = null
    fun setValue(value: T) {
      if (_value != null) throw RuntimeException("Value already set for this trie node")
      _value = value
    }
  }

  private val _root = ByteTrieNode<T?>()
  /** Gets the maximum depth stored in this trie.  */
  var maxDepth = 0
    private set

  /**
   * Return the most specific value stored for this byte sequence.
   * If not found, returns `null` or a default values as specified by
   * calling [ByteTrie.setDefaultValue].
   */
  fun find(bytes: ByteArray): T? {
    var node = _root
    var value = node._value
    for (b in bytes) {
      val child = node._children[b] ?: break
      node = child
      if (node._value != null) value = node._value
    }
    return value
  }

  /** Store the given value at the specified path.  */
  fun addPath(value: T, vararg parts: ByteArray) {
    var depth = 0
    var node = _root
    for (part in parts) {
      for (b in part) {
        var child = node._children[b]
        if (child == null) {
          child = ByteTrieNode()
          node._children[b] = child
        }
        node = child
        depth++
      }
    }
    require(depth != 0) { "Parts must contain at least one byte." }
    node.setValue(value)
    maxDepth = Math.max(maxDepth, depth)
  }

  /** Sets the default value to use in [ByteTrie.find] when no path matches.  */
  fun setDefaultValue(defaultValue: T) {
    _root.setValue(defaultValue)
  }
}
