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

import com.drew.lang.CompoundException

/**
 * Base class for all metadata specific exceptions.
 *
 * @author Drew Noakes https://drewnoakes.com
 */
class MetadataException : CompoundException {
  constructor(msg: String?) : super(msg) {}
  constructor(exception: Throwable?) : super(exception) {}
  constructor(msg: String?, innerException: Throwable?) : super(msg, innerException) {}

  companion object {
    private const val serialVersionUID = 8612756143363919682L
  }
}
