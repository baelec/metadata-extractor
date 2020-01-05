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
package com.drew.metadata.mp4

import java.util.*

/**
 * @author Payton Garland
 */
object Mp4ContainerTypes {
  const val BOX_MOVIE = "moov"
  const val BOX_USER_DATA = "udta"
  const val BOX_TRACK = "trak"
  const val BOX_MEDIA = "mdia"
  const val BOX_MEDIA_INFORMATION = "minf"
  const val BOX_SAMPLE_TABLE = "stbl"
  const val BOX_METADATA_LIST = "ilst"
  const val BOX_METADATA = "meta"
  const val BOX_COMPRESSED_MOVIE = "cmov"
  const val BOX_MEDIA_TEXT = "text"
  const val BOX_MEDIA_SUBTITLE = "sbtl"
  const val BOX_MEDIA_NULL = "nmhd"
  //private val _containerList = listOf(BOX_MOVIE, BOX_USER_DATA, BOX_TRACK, BOX_MEDIA, BOX_MEDIA_INFORMATION, BOX_SAMPLE_TABLE, BOX_METADATA, BOX_METADATA_LIST, BOX_COMPRESSED_MOVIE, BOX_MEDIA_TEXT, BOX_MEDIA_SUBTITLE, BOX_MEDIA_NULL)
}
