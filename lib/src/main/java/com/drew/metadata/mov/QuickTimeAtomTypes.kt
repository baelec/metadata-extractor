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
object QuickTimeAtomTypes {
  const val ATOM_FILE_TYPE = "ftyp"
  const val ATOM_MOVIE_HEADER = "mvhd"
  const val ATOM_VIDEO_MEDIA_INFO = "vmhd"
  const val ATOM_SOUND_MEDIA_INFO = "smhd"
  const val ATOM_BASE_MEDIA_INFO = "gmhd"
  const val ATOM_TIMECODE_MEDIA_INFO = "tcmi"
  const val ATOM_HANDLER = "hdlr"
  const val ATOM_KEYS = "keys"
  const val ATOM_DATA = "data"
  const val ATOM_SAMPLE_DESCRIPTION = "stsd"
  const val ATOM_TIME_TO_SAMPLE = "stts"
  const val ATOM_MEDIA_HEADER = "mdhd"
  const val ATOM_CANON_THUMBNAIL = "CNTH"
  //private val atomList = listOf(ATOM_FILE_TYPE, ATOM_MOVIE_HEADER, ATOM_VIDEO_MEDIA_INFO, ATOM_SOUND_MEDIA_INFO, ATOM_BASE_MEDIA_INFO, ATOM_TIMECODE_MEDIA_INFO, ATOM_HANDLER, ATOM_KEYS, ATOM_DATA, ATOM_SAMPLE_DESCRIPTION, ATOM_TIME_TO_SAMPLE, ATOM_MEDIA_HEADER, ATOM_CANON_THUMBNAIL)
}
