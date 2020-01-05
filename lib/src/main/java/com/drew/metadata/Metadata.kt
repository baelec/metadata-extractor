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
 * A top-level object that holds the metadata values extracted from an image.
 *
 *
 * Metadata objects may contain zero or more [Directory] objects.  Each directory may contain zero or more tags
 * with corresponding values.
 *
 * @author Drew Noakes https://drewnoakes.com
 */
class Metadata {
  /**
   * The list of [Directory] instances in this container, in the order they were added.
   */
  private val _directories: MutableList<Directory> = ArrayList()

  /**
   * Returns an iterable set of the [Directory] instances contained in this metadata collection.
   *
   * @return an iterable set of directories
   */
  val directories: Iterable<Directory>
    get() = _directories

  fun <T : Directory> getDirectoriesOfType(type: Class<T>): Collection<T> {
    return _directories.filter { type.isAssignableFrom(it.javaClass) }.toList() as List<T>
  }

  /**
   * Returns the count of directories in this metadata collection.
   *
   * @return the number of unique directory types set for this metadata collection
   */
  val directoryCount: Int
    get() = _directories.size

  /**
   * Adds a directory to this metadata collection.
   *
   * @param directory the [Directory] to add into this metadata collection.
   */
  fun <T : Directory> addDirectory(directory: T) {
    _directories.add(directory)
  }

  /**
   * Gets the first [Directory] of the specified type contained within this metadata collection.
   * If no instances of this type are present, `null` is returned.
   *
   * @param type the Directory type
   * @param <T> the Directory type
   * @return the first Directory of type T in this metadata collection, or `null` if none exist
  </T> */
  fun <T : Directory> getFirstDirectoryOfType(type: Class<T>): T? {
    return _directories.firstOrNull { type.isAssignableFrom(it.javaClass) } as T?
  }

  /**
   * Indicates whether an instance of the given directory type exists in this Metadata instance.
   *
   * @param type the [Directory] type
   * @return `true` if a [Directory] of the specified type exists, otherwise `false`
   */
  fun containsDirectoryOfType(type: Class<out Directory>): Boolean {
    for (dir in _directories) {
      if (type.isAssignableFrom(dir.javaClass)) return true
    }
    return false
  }

  /**
   * Indicates whether any errors were reported during the reading of metadata values.
   * This value will be true if Directory.hasErrors() is true for one of the contained [Directory] objects.
   *
   * @return whether one of the contained directories has an error
   */
  fun hasErrors(): Boolean {
    for (directory in directories) {
      if (directory.hasErrors()) return true
    }
    return false
  }

  override fun toString(): String {
    val count = directoryCount
    return String.format("Metadata (%d %s)",
      count,
      if (count == 1) "directory" else "directories")
  }
}
