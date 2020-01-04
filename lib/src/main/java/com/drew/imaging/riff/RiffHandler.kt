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
package com.drew.imaging.riff

/**
 * Interface of an class capable of handling events raised during the reading of a RIFF file
 * via [RiffReader].
 *
 * @author Drew Noakes https://drewnoakes.com
 */
interface RiffHandler {
  /**
   * Gets whether the specified RIFF identifier is of interest to this handler.
   * Returning `false` causes processing to stop after reading only
   * the first twelve bytes of data.
   *
   * @param identifier The four character code identifying the type of RIFF data
   * @return true if processing should continue, otherwise false
   */
  fun shouldAcceptRiffIdentifier(identifier: String): Boolean

  /**
   * Gets whether this handler is interested in the specific chunk type.
   * Returns `true` if the data should be copied into an array and passed
   * to [RiffHandler.processChunk], or `false` to avoid
   * the copy and skip to the next chunk in the file, if any.
   *
   * @param fourCC the four character code of this chunk
   * @return true if [RiffHandler.processChunk] should be called, otherwise false
   */
  fun shouldAcceptChunk(fourCC: String): Boolean

  /**
   * Gets whether this handler is interested in the specific list type.
   * Returns `true` if the chunks should continue being processed,
   * or `false` to avoid any unknown chunks within the list.
   *
   * @param fourCC the four character code of this chunk
   * @return true if [RiffHandler.processChunk] should be called, otherwise false
   */
  fun shouldAcceptList(fourCC: String): Boolean

  /**
   * Perform whatever processing is necessary for the type of chunk with its
   * payload.
   *
   * This is only called if a previous call to [RiffHandler.shouldAcceptChunk]
   * with the same `fourCC` returned `true`.
   *
   * @param fourCC the four character code of the chunk
   * @param payload they payload of the chunk as a byte array
   */
  fun processChunk(fourCC: String, payload: ByteArray)
}
