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
package com.drew.metadata.tiff

import com.drew.imaging.tiff.TiffHandler
import com.drew.lang.Rational
import com.drew.metadata.Directory
import com.drew.metadata.ErrorDirectory
import com.drew.metadata.Metadata
import com.drew.metadata.StringValue
import java.util.Stack

/**
 * Adapter between the [TiffHandler] interface and the [Metadata]/[Directory] object model.
 *
 * @author Drew Noakes https://drewnoakes.com
 */
abstract class DirectoryTiffHandler protected constructor(protected val _metadata: Metadata, private var _rootParentDirectory: Directory?) : TiffHandler {
  private val _directoryStack = Stack<Directory>()
  @JvmField
  protected var _currentDirectory: Directory? = null
  override fun endingIFD() {
    _currentDirectory = if (_directoryStack.empty()) null else _directoryStack.pop()
  }

  protected fun pushDirectory(directoryClass: Class<out Directory>): Directory {
    val newDirectory: Directory
    newDirectory = try {
      directoryClass.newInstance()
    } catch (e: InstantiationException) {
      throw RuntimeException(e)
    } catch (e: IllegalAccessException) {
      throw RuntimeException(e)
    }
    var currentDirectory = _currentDirectory
    // If this is the first directory, don't add to the stack
    if (currentDirectory == null) { // Apply any pending root parent to this new directory
      val rootParentDirectory = _rootParentDirectory
      if (rootParentDirectory != null) {
        newDirectory.setParent(rootParentDirectory)
        _rootParentDirectory = null
      }
    } else { // The current directory is pushed onto the stack, and set as the new directory's parent
      _directoryStack.push(currentDirectory)
      newDirectory.setParent(currentDirectory)
    }
    _currentDirectory = newDirectory
    currentDirectory = newDirectory
    _metadata.addDirectory(currentDirectory)
    return currentDirectory
  }

  override fun warn(message: String) {
    currentOrErrorDirectory.addError(message)
  }

  override fun error(message: String) {
    currentOrErrorDirectory.addError(message)
  }

  private val currentOrErrorDirectory: Directory
    get() {
      val currentDirectory = _currentDirectory
      if (currentDirectory != null) return currentDirectory
      val error = _metadata.getFirstDirectoryOfType(ErrorDirectory::class.java)
      if (error != null) {
        return error
      }
      return pushDirectory(ErrorDirectory::class.java)
    }

  override fun setByteArray(tagId: Int, bytes: ByteArray) {
    _currentDirectory?.setByteArray(tagId, bytes)
  }

  override fun setString(tagId: Int, string: StringValue) {
    _currentDirectory?.setStringValue(tagId, string)
  }

  override fun setRational(tagId: Int, rational: Rational) {
    _currentDirectory?.setRational(tagId, rational)
  }

  override fun setRationalArray(tagId: Int, array: Array<Rational?>) {
    _currentDirectory?.setRationalArray(tagId, array)
  }

  override fun setFloat(tagId: Int, float32: Float) {
    _currentDirectory?.setFloat(tagId, float32)
  }

  override fun setFloatArray(tagId: Int, array: FloatArray) {
    assert(_currentDirectory != null)
    _currentDirectory?.setFloatArray(tagId, array)
  }

  override fun setDouble(tagId: Int, double64: Double) {
    _currentDirectory?.setDouble(tagId, double64)
  }

  override fun setDoubleArray(tagId: Int, array: DoubleArray) {
    _currentDirectory?.setDoubleArray(tagId, array)
  }

  override fun setInt8s(tagId: Int, int8s: Byte) {
    // NOTE Directory stores all integral types as int32s, except for int32u and long
    _currentDirectory?.setInt(tagId, int8s.toInt())
  }

  override fun setInt8sArray(tagId: Int, array: ByteArray) {
    // NOTE Directory stores all integral types as int32s, except for int32u and long
    _currentDirectory?.setByteArray(tagId, array)
  }

  override fun setInt8u(tagId: Int, int8u: Short) {
    // NOTE Directory stores all integral types as int32s, except for int32u and long
    _currentDirectory?.setInt(tagId, int8u.toInt())
  }

  override fun setInt8uArray(tagId: Int, array: ShortArray) {
    // TODO create and use a proper setter for short[]
    _currentDirectory?.setObjectArray(tagId, array)
  }

  override fun setInt16s(tagId: Int, int16s: Int) {
    // TODO create and use a proper setter for int16u?
    _currentDirectory?.setInt(tagId, int16s)
  }

  override fun setInt16sArray(tagId: Int, array: ShortArray) {
    // TODO create and use a proper setter for short[]
    _currentDirectory?.setObjectArray(tagId, array)
  }

  override fun setInt16u(tagId: Int, int16u: Int) {
    // TODO create and use a proper setter for
    _currentDirectory?.setInt(tagId, int16u)
  }

  override fun setInt16uArray(tagId: Int, array: IntArray) {
    // TODO create and use a proper setter for short[]
    _currentDirectory?.setObjectArray(tagId, array)
  }

  override fun setInt32s(tagId: Int, int32s: Int) {
    _currentDirectory?.setInt(tagId, int32s)
  }

  override fun setInt32sArray(tagId: Int, array: IntArray) {
    _currentDirectory?.setIntArray(tagId, array)
  }

  override fun setInt32u(tagId: Int, int32u: Long) {
    _currentDirectory?.setLong(tagId, int32u)
  }

  override fun setInt32uArray(tagId: Int, array: LongArray) {
    // TODO create and use a proper setter for short[]
    _currentDirectory?.setObjectArray(tagId, array)
  }
}
