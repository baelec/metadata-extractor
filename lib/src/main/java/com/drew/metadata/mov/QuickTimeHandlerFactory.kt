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

import com.drew.imaging.quicktime.QuickTimeHandler
import com.drew.metadata.Metadata
import com.drew.metadata.mov.media.*
import com.drew.metadata.mov.metadata.QuickTimeDataHandler
import com.drew.metadata.mov.metadata.QuickTimeDirectoryHandler

/**
 * @author Payton Garland
 */
class QuickTimeHandlerFactory(private val caller: QuickTimeHandler<*>) {
  fun getHandler(type: String, metadata: Metadata, context: QuickTimeContext): QuickTimeHandler<*> {
    when (type) {
      HANDLER_METADATA_DIRECTORY -> {
        return QuickTimeDirectoryHandler(metadata)
      }
      HANDLER_METADATA_DATA -> {
        return QuickTimeDataHandler(metadata)
      }
      HANDLER_SOUND_MEDIA -> {
        return QuickTimeSoundHandler(metadata, context)
      }
      HANDLER_VIDEO_MEDIA -> {
        return QuickTimeVideoHandler(metadata, context)
      }
      HANDLER_TIMECODE_MEDIA -> {
        return QuickTimeTimecodeHandler(metadata, context)
      }
      HANDLER_TEXT_MEDIA -> {
        return QuickTimeTextHandler(metadata, context)
      }
      HANDLER_SUBTITLE_MEDIA -> {
        return QuickTimeSubtitleHandler(metadata, context)
      }
      HANDLER_MUSIC_MEDIA -> {
        return QuickTimeMusicHandler(metadata, context)
      }
      else -> return caller
    }
  }

  companion object {
    private const val HANDLER_METADATA_DIRECTORY = "mdir"
    private const val HANDLER_METADATA_DATA = "mdta"
    private const val HANDLER_SOUND_MEDIA = "soun"
    private const val HANDLER_VIDEO_MEDIA = "vide"
    private const val HANDLER_TIMECODE_MEDIA = "tmcd"
    private const val HANDLER_TEXT_MEDIA = "text"
    private const val HANDLER_SUBTITLE_MEDIA = "sbtl"
    private const val HANDLER_MUSIC_MEDIA = "musi"
  }
}
