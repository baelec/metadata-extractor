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

import com.drew.imaging.mp4.Mp4Handler
import com.drew.metadata.Metadata
import com.drew.metadata.mp4.boxes.HandlerBox
import com.drew.metadata.mp4.media.*

class Mp4HandlerFactory(private val caller: Mp4Handler<*>) {
  fun getHandler(box: HandlerBox, metadata: Metadata, context: Mp4Context): Mp4Handler<*> {
    val type = box.handlerType
    when (type) {
      HANDLER_SOUND_MEDIA -> {
        return Mp4SoundHandler(metadata, context)
      }
      HANDLER_VIDEO_MEDIA -> {
        return Mp4VideoHandler(metadata, context)
      }
      HANDLER_HINT_MEDIA -> {
        return Mp4HintHandler(metadata, context)
      }
      HANDLER_TEXT_MEDIA -> {
        return Mp4TextHandler(metadata, context)
      }
      HANDLER_META_MEDIA -> {
        return Mp4MetaHandler(metadata, context)
      }
      else -> return caller
    }
  }

  companion object {
    private const val HANDLER_SOUND_MEDIA = "soun"
    private const val HANDLER_VIDEO_MEDIA = "vide"
    private const val HANDLER_HINT_MEDIA = "hint"
    private const val HANDLER_TEXT_MEDIA = "text"
    private const val HANDLER_META_MEDIA = "meta"
  }
}
