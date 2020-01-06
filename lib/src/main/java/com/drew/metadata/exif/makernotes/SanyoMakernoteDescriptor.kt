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
package com.drew.metadata.exif.makernotes

import com.drew.metadata.TagDescriptor

/**
 * Provides human-readable string representations of tag values stored in a [com.drew.metadata.exif.makernotes.SonyType6MakernoteDirectory].
 *
 * @author Drew Noakes https://drewnoakes.com
 */
class SanyoMakernoteDescriptor(directory: SanyoMakernoteDirectory) : TagDescriptor<SanyoMakernoteDirectory>(directory) {
  override fun getDescription(tagType: Int): String? {
    return when (tagType) {
      SanyoMakernoteDirectory.TAG_SANYO_QUALITY -> sanyoQualityDescription
      SanyoMakernoteDirectory.TAG_MACRO -> macroDescription
      SanyoMakernoteDirectory.TAG_DIGITAL_ZOOM -> digitalZoomDescription
      SanyoMakernoteDirectory.TAG_SEQUENTIAL_SHOT -> sequentialShotDescription
      SanyoMakernoteDirectory.TAG_WIDE_RANGE -> wideRangeDescription
      SanyoMakernoteDirectory.TAG_COLOR_ADJUSTMENT_MODE -> colorAdjustmentModeDescription
      SanyoMakernoteDirectory.TAG_QUICK_SHOT -> quickShotDescription
      SanyoMakernoteDirectory.TAG_SELF_TIMER -> selfTimerDescription
      SanyoMakernoteDirectory.TAG_VOICE_MEMO -> voiceMemoDescription
      SanyoMakernoteDirectory.TAG_RECORD_SHUTTER_RELEASE -> recordShutterDescription
      SanyoMakernoteDirectory.TAG_FLICKER_REDUCE -> flickerReduceDescription
      SanyoMakernoteDirectory.TAG_OPTICAL_ZOOM_ON -> optimalZoomOnDescription
      SanyoMakernoteDirectory.TAG_DIGITAL_ZOOM_ON -> digitalZoomOnDescription
      SanyoMakernoteDirectory.TAG_LIGHT_SOURCE_SPECIAL -> lightSourceSpecialDescription
      SanyoMakernoteDirectory.TAG_RESAVED -> resavedDescription
      SanyoMakernoteDirectory.TAG_SCENE_SELECT -> sceneSelectDescription
      SanyoMakernoteDirectory.TAG_SEQUENCE_SHOT_INTERVAL -> sequenceShotIntervalDescription
      SanyoMakernoteDirectory.TAG_FLASH_MODE -> flashModeDescription
      else -> super.getDescription(tagType)
    }
  }

  val sanyoQualityDescription: String?
    get() {
      val value = _directory!!.getInteger(SanyoMakernoteDirectory.TAG_SANYO_QUALITY) ?: return null
      return when (value) {
        0x0 -> "Normal/Very Low"
        0x1 -> "Normal/Low"
        0x2 -> "Normal/Medium Low"
        0x3 -> "Normal/Medium"
        0x4 -> "Normal/Medium High"
        0x5 -> "Normal/High"
        0x6 -> "Normal/Very High"
        0x7 -> "Normal/Super High"
        0x100 -> "Fine/Very Low"
        0x101 -> "Fine/Low"
        0x102 -> "Fine/Medium Low"
        0x103 -> "Fine/Medium"
        0x104 -> "Fine/Medium High"
        0x105 -> "Fine/High"
        0x106 -> "Fine/Very High"
        0x107 -> "Fine/Super High"
        0x200 -> "Super Fine/Very Low"
        0x201 -> "Super Fine/Low"
        0x202 -> "Super Fine/Medium Low"
        0x203 -> "Super Fine/Medium"
        0x204 -> "Super Fine/Medium High"
        0x205 -> "Super Fine/High"
        0x206 -> "Super Fine/Very High"
        0x207 -> "Super Fine/Super High"
        else -> "Unknown ($value)"
      }
    }

  private val macroDescription: String?
    private get() = getIndexedDescription(SanyoMakernoteDirectory.TAG_MACRO, "Normal", "Macro", "View", "Manual")

  private val digitalZoomDescription: String?
    private get() = getDecimalRational(SanyoMakernoteDirectory.TAG_DIGITAL_ZOOM, 3)

  private val sequentialShotDescription: String?
    private get() = getIndexedDescription(SanyoMakernoteDirectory.TAG_SEQUENTIAL_SHOT, "None", "Standard", "Best", "Adjust Exposure")

  private val wideRangeDescription: String?
    private get() = getIndexedDescription(SanyoMakernoteDirectory.TAG_WIDE_RANGE, "Off", "On")

  private val colorAdjustmentModeDescription: String?
    private get() = getIndexedDescription(SanyoMakernoteDirectory.TAG_COLOR_ADJUSTMENT_MODE, "Off", "On")

  private val quickShotDescription: String?
    private get() = getIndexedDescription(SanyoMakernoteDirectory.TAG_QUICK_SHOT, "Off", "On")

  private val selfTimerDescription: String?
    private get() = getIndexedDescription(SanyoMakernoteDirectory.TAG_SELF_TIMER, "Off", "On")

  private val voiceMemoDescription: String?
    private get() = getIndexedDescription(SanyoMakernoteDirectory.TAG_VOICE_MEMO, "Off", "On")

  private val recordShutterDescription: String?
    private get() = getIndexedDescription(SanyoMakernoteDirectory.TAG_RECORD_SHUTTER_RELEASE, "Record while down", "Press start, press stop")

  private val flickerReduceDescription: String?
    private get() = getIndexedDescription(SanyoMakernoteDirectory.TAG_FLICKER_REDUCE, "Off", "On")

  private val optimalZoomOnDescription: String?
    private get() = getIndexedDescription(SanyoMakernoteDirectory.TAG_OPTICAL_ZOOM_ON, "Off", "On")

  private val digitalZoomOnDescription: String?
    private get() = getIndexedDescription(SanyoMakernoteDirectory.TAG_DIGITAL_ZOOM_ON, "Off", "On")

  private val lightSourceSpecialDescription: String?
    private get() = getIndexedDescription(SanyoMakernoteDirectory.TAG_LIGHT_SOURCE_SPECIAL, "Off", "On")

  private val resavedDescription: String?
    private get() = getIndexedDescription(SanyoMakernoteDirectory.TAG_RESAVED, "No", "Yes")

  private val sceneSelectDescription: String?
    private get() = getIndexedDescription(SanyoMakernoteDirectory.TAG_SCENE_SELECT,
      "Off", "Sport", "TV", "Night", "User 1", "User 2", "Lamp")

  private val sequenceShotIntervalDescription: String?
    private get() = getIndexedDescription(SanyoMakernoteDirectory.TAG_SEQUENCE_SHOT_INTERVAL,
      "5 frames/sec", "10 frames/sec", "15 frames/sec", "20 frames/sec")

  private val flashModeDescription: String?
    private get() = getIndexedDescription(SanyoMakernoteDirectory.TAG_FLASH_MODE,
      "Auto", "Force", "Disabled", "Red eye")
}
