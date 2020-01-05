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

/**
 * @author Payton Garland
 */
object QuickTimeContainerTypes {
  const val ATOM_MOVIE = "moov"
  const val ATOM_USER_DATA = "udta"
  const val ATOM_TRACK = "trak"
  const val ATOM_MEDIA = "mdia"
  const val ATOM_MEDIA_INFORMATION = "minf"
  const val ATOM_SAMPLE_TABLE = "stbl"
  const val ATOM_METADATA_LIST = "ilst"
  const val ATOM_METADATA = "meta"
  const val ATOM_COMPRESSED_MOVIE = "cmov"
  const val ATOM_MEDIA_TEXT = "text"
  const val ATOM_MEDIA_SUBTITLE = "sbtl"
  const val ATOM_MEDIA_BASE = "gmhd"
  //private val containerList = listOf(ATOM_MOVIE, ATOM_USER_DATA, ATOM_TRACK, ATOM_MEDIA, ATOM_MEDIA_INFORMATION, ATOM_SAMPLE_TABLE, ATOM_METADATA, ATOM_METADATA_LIST, ATOM_COMPRESSED_MOVIE, ATOM_MEDIA_TEXT, ATOM_MEDIA_SUBTITLE, ATOM_MEDIA_BASE)
}
