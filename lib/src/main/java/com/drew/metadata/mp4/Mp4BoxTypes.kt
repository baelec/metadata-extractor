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
object Mp4BoxTypes {
  const val BOX_FILE_TYPE = "ftyp"
  const val BOX_MOVIE_HEADER = "mvhd"
  const val BOX_VIDEO_MEDIA_INFO = "vmhd"
  const val BOX_SOUND_MEDIA_INFO = "smhd"
  const val BOX_HINT_MEDIA_INFO = "hmhd"
  const val BOX_NULL_MEDIA_INFO = "nmhd"
  const val BOX_HANDLER = "hdlr"
  const val BOX_SAMPLE_DESCRIPTION = "stsd"
  const val BOX_TIME_TO_SAMPLE = "stts"
  const val BOX_MEDIA_HEADER = "mdhd"
  const val BOX_TRACK_HEADER = "tkhd"
  //private val _boxList = listOf(BOX_FILE_TYPE, BOX_MOVIE_HEADER, BOX_VIDEO_MEDIA_INFO, BOX_SOUND_MEDIA_INFO, BOX_HINT_MEDIA_INFO, BOX_NULL_MEDIA_INFO, BOX_HANDLER, BOX_SAMPLE_DESCRIPTION, BOX_TIME_TO_SAMPLE, BOX_MEDIA_HEADER, BOX_TRACK_HEADER)
}
