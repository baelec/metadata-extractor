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
package com.drew.metadata.photoshop

import com.drew.metadata.Directory
import java.util.*

/**
 * Holds the metadata found in the APPD segment of a JPEG file saved by Photoshop.
 *
 * @author Drew Noakes https://drewnoakes.com
 * @author Yuri Binev
 * @author Payton Garland
 */
class PhotoshopDirectory : Directory() {
  override val tagNameMap = Companion.tagNameMap
  companion object {
    const val TAG_CHANNELS_ROWS_COLUMNS_DEPTH_MODE = 0x03E8
    const val TAG_MAC_PRINT_INFO = 0x03E9
    const val TAG_XML = 0x03EA
    const val TAG_INDEXED_COLOR_TABLE = 0x03EB
    const val TAG_RESOLUTION_INFO = 0x03ED
    const val TAG_ALPHA_CHANNELS = 0x03EE
    const val TAG_DISPLAY_INFO_OBSOLETE = 0x03EF
    const val TAG_CAPTION = 0x03F0
    const val TAG_BORDER_INFORMATION = 0x03F1
    const val TAG_BACKGROUND_COLOR = 0x03F2
    const val TAG_PRINT_FLAGS = 0x03F3
    const val TAG_GRAYSCALE_AND_MULTICHANNEL_HALFTONING_INFORMATION = 0x03F4
    const val TAG_COLOR_HALFTONING_INFORMATION = 0x03F5
    const val TAG_DUOTONE_HALFTONING_INFORMATION = 0x03F6
    const val TAG_GRAYSCALE_AND_MULTICHANNEL_TRANSFER_FUNCTION = 0x03F7
    const val TAG_COLOR_TRANSFER_FUNCTIONS = 0x03F8
    const val TAG_DUOTONE_TRANSFER_FUNCTIONS = 0x03F9
    const val TAG_DUOTONE_IMAGE_INFORMATION = 0x03FA
    const val TAG_EFFECTIVE_BLACK_AND_WHITE_VALUES = 0x03FB
    // OBSOLETE                                                                     0x03FC
    const val TAG_EPS_OPTIONS = 0x03FD
    const val TAG_QUICK_MASK_INFORMATION = 0x03FE
    // OBSOLETE                                                                     0x03FF
    const val TAG_LAYER_STATE_INFORMATION = 0x0400
    // Working path (not saved)                                                     0x0401
    const val TAG_LAYERS_GROUP_INFORMATION = 0x0402
    // OBSOLETE                                                                     0x0403
    const val TAG_IPTC = 0x0404
    const val TAG_IMAGE_MODE_FOR_RAW_FORMAT_FILES = 0x0405
    const val TAG_JPEG_QUALITY = 0x0406
    const val TAG_GRID_AND_GUIDES_INFORMATION = 0x0408
    const val TAG_THUMBNAIL_OLD = 0x0409
    const val TAG_COPYRIGHT = 0x040A
    const val TAG_URL = 0x040B
    const val TAG_THUMBNAIL = 0x040C
    const val TAG_GLOBAL_ANGLE = 0x040D
    // OBSOLETE                                                                     0x040E
    const val TAG_ICC_PROFILE_BYTES = 0x040F
    const val TAG_WATERMARK = 0x0410
    const val TAG_ICC_UNTAGGED_PROFILE = 0x0411
    const val TAG_EFFECTS_VISIBLE = 0x0412
    const val TAG_SPOT_HALFTONE = 0x0413
    const val TAG_SEED_NUMBER = 0x0414
    const val TAG_UNICODE_ALPHA_NAMES = 0x0415
    const val TAG_INDEXED_COLOR_TABLE_COUNT = 0x0416
    const val TAG_TRANSPARENCY_INDEX = 0x0417
    const val TAG_GLOBAL_ALTITUDE = 0x0419
    const val TAG_SLICES = 0x041A
    const val TAG_WORKFLOW_URL = 0x041B
    const val TAG_JUMP_TO_XPEP = 0x041C
    const val TAG_ALPHA_IDENTIFIERS = 0x041D
    const val TAG_URL_LIST = 0x041E
    const val TAG_VERSION = 0x0421
    const val TAG_EXIF_DATA_1 = 0x0422
    const val TAG_EXIF_DATA_3 = 0x0423
    const val TAG_XMP_DATA = 0x0424
    const val TAG_CAPTION_DIGEST = 0x0425
    const val TAG_PRINT_SCALE = 0x0426
    const val TAG_PIXEL_ASPECT_RATIO = 0x0428
    const val TAG_LAYER_COMPS = 0x0429
    const val TAG_ALTERNATE_DUOTONE_COLORS = 0x042A
    const val TAG_ALTERNATE_SPOT_COLORS = 0x042B
    const val TAG_LAYER_SELECTION_IDS = 0x042D
    const val TAG_HDR_TONING_INFO = 0x042E
    const val TAG_PRINT_INFO = 0x042F
    const val TAG_LAYER_GROUPS_ENABLED_ID = 0x0430
    const val TAG_COLOR_SAMPLERS = 0x0431
    const val TAG_MEASUREMENT_SCALE = 0x0432
    const val TAG_TIMELINE_INFORMATION = 0x0433
    const val TAG_SHEET_DISCLOSURE = 0x0434
    const val TAG_DISPLAY_INFO = 0x0435
    const val TAG_ONION_SKINS = 0x0436
    const val TAG_COUNT_INFORMATION = 0x0438
    const val TAG_PRINT_INFO_2 = 0x043A
    const val TAG_PRINT_STYLE = 0x043B
    const val TAG_MAC_NSPRINTINFO = 0x043C
    const val TAG_WIN_DEVMODE = 0x043D
    const val TAG_AUTO_SAVE_FILE_PATH = 0x043E
    const val TAG_AUTO_SAVE_FORMAT = 0x043F
    const val TAG_PATH_SELECTION_STATE = 0x0440
    // PATH INFO                                                                    0x07D0 -> 0x0BB6
    const val TAG_CLIPPING_PATH_NAME = 0x0BB7
    const val TAG_ORIGIN_PATH_INFO = 0x0BB8
    // PLUG IN RESOURCES                                                            0x0FA0 -> 0x1387
    const val TAG_IMAGE_READY_VARIABLES_XML = 0x1B58
    const val TAG_IMAGE_READY_DATA_SETS = 0x1B59
    const val TAG_IMAGE_READY_SELECTED_STATE = 0x1B5A
    const val TAG_IMAGE_READY_7_ROLLOVER = 0x1B5B
    const val TAG_IMAGE_READY_ROLLOVER = 0x1B5C
    const val TAG_IMAGE_READY_SAVE_LAYER_SETTINGS = 0x1B5D
    const val TAG_IMAGE_READY_VERSION = 0x1B5E
    const val TAG_LIGHTROOM_WORKFLOW = 0x1F40
    const val TAG_PRINT_FLAGS_INFO = 0x2710
    val tagNameMap = HashMap<Int, String>()

    init {
      tagNameMap[TAG_CHANNELS_ROWS_COLUMNS_DEPTH_MODE] = "Channels, Rows, Columns, Depth, Mode"
      tagNameMap[TAG_MAC_PRINT_INFO] = "Mac Print Info"
      tagNameMap[TAG_XML] = "XML Data"
      tagNameMap[TAG_INDEXED_COLOR_TABLE] = "Indexed Color Table"
      tagNameMap[TAG_RESOLUTION_INFO] = "Resolution Info"
      tagNameMap[TAG_ALPHA_CHANNELS] = "Alpha Channels"
      tagNameMap[TAG_DISPLAY_INFO_OBSOLETE] = "Display Info (Obsolete)"
      tagNameMap[TAG_CAPTION] = "Caption"
      tagNameMap[TAG_BORDER_INFORMATION] = "Border Information"
      tagNameMap[TAG_BACKGROUND_COLOR] = "Background Color"
      tagNameMap[TAG_PRINT_FLAGS] = "Print Flags"
      tagNameMap[TAG_GRAYSCALE_AND_MULTICHANNEL_HALFTONING_INFORMATION] = "Grayscale and Multichannel Halftoning Information"
      tagNameMap[TAG_COLOR_HALFTONING_INFORMATION] = "Color Halftoning Information"
      tagNameMap[TAG_DUOTONE_HALFTONING_INFORMATION] = "Duotone Halftoning Information"
      tagNameMap[TAG_GRAYSCALE_AND_MULTICHANNEL_TRANSFER_FUNCTION] = "Grayscale and Multichannel Transfer Function"
      tagNameMap[TAG_COLOR_TRANSFER_FUNCTIONS] = "Color Transfer Functions"
      tagNameMap[TAG_DUOTONE_TRANSFER_FUNCTIONS] = "Duotone Transfer Functions"
      tagNameMap[TAG_DUOTONE_IMAGE_INFORMATION] = "Duotone Image Information"
      tagNameMap[TAG_EFFECTIVE_BLACK_AND_WHITE_VALUES] = "Effective Black and White Values"
      tagNameMap[TAG_EPS_OPTIONS] = "EPS Options"
      tagNameMap[TAG_QUICK_MASK_INFORMATION] = "Quick Mask Information"
      tagNameMap[TAG_LAYER_STATE_INFORMATION] = "Layer State Information"
      tagNameMap[TAG_LAYERS_GROUP_INFORMATION] = "Layers Group Information"
      tagNameMap[TAG_IPTC] = "IPTC-NAA Record"
      tagNameMap[TAG_IMAGE_MODE_FOR_RAW_FORMAT_FILES] = "Image Mode for Raw Format Files"
      tagNameMap[TAG_JPEG_QUALITY] = "JPEG Quality"
      tagNameMap[TAG_GRID_AND_GUIDES_INFORMATION] = "Grid and Guides Information"
      tagNameMap[TAG_THUMBNAIL_OLD] = "Photoshop 4.0 Thumbnail"
      tagNameMap[TAG_COPYRIGHT] = "Copyright Flag"
      tagNameMap[TAG_URL] = "URL"
      tagNameMap[TAG_THUMBNAIL] = "Thumbnail Data"
      tagNameMap[TAG_GLOBAL_ANGLE] = "Global Angle"
      tagNameMap[TAG_ICC_PROFILE_BYTES] = "ICC Profile Bytes"
      tagNameMap[TAG_WATERMARK] = "Watermark"
      tagNameMap[TAG_ICC_UNTAGGED_PROFILE] = "ICC Untagged Profile"
      tagNameMap[TAG_EFFECTS_VISIBLE] = "Effects Visible"
      tagNameMap[TAG_SPOT_HALFTONE] = "Spot Halftone"
      tagNameMap[TAG_SEED_NUMBER] = "Seed Number"
      tagNameMap[TAG_UNICODE_ALPHA_NAMES] = "Unicode Alpha Names"
      tagNameMap[TAG_INDEXED_COLOR_TABLE_COUNT] = "Indexed Color Table Count"
      tagNameMap[TAG_TRANSPARENCY_INDEX] = "Transparency Index"
      tagNameMap[TAG_GLOBAL_ALTITUDE] = "Global Altitude"
      tagNameMap[TAG_SLICES] = "Slices"
      tagNameMap[TAG_WORKFLOW_URL] = "Workflow URL"
      tagNameMap[TAG_JUMP_TO_XPEP] = "Jump To XPEP"
      tagNameMap[TAG_ALPHA_IDENTIFIERS] = "Alpha Identifiers"
      tagNameMap[TAG_URL_LIST] = "URL List"
      tagNameMap[TAG_VERSION] = "Version Info"
      tagNameMap[TAG_EXIF_DATA_1] = "EXIF Data 1"
      tagNameMap[TAG_EXIF_DATA_3] = "EXIF Data 3"
      tagNameMap[TAG_XMP_DATA] = "XMP Data"
      tagNameMap[TAG_CAPTION_DIGEST] = "Caption Digest"
      tagNameMap[TAG_PRINT_SCALE] = "Print Scale"
      tagNameMap[TAG_PIXEL_ASPECT_RATIO] = "Pixel Aspect Ratio"
      tagNameMap[TAG_LAYER_COMPS] = "Layer Comps"
      tagNameMap[TAG_ALTERNATE_DUOTONE_COLORS] = "Alternate Duotone Colors"
      tagNameMap[TAG_ALTERNATE_SPOT_COLORS] = "Alternate Spot Colors"
      tagNameMap[TAG_LAYER_SELECTION_IDS] = "Layer Selection IDs"
      tagNameMap[TAG_HDR_TONING_INFO] = "HDR Toning Info"
      tagNameMap[TAG_PRINT_INFO] = "Print Info"
      tagNameMap[TAG_LAYER_GROUPS_ENABLED_ID] = "Layer Groups Enabled ID"
      tagNameMap[TAG_COLOR_SAMPLERS] = "Color Samplers"
      tagNameMap[TAG_MEASUREMENT_SCALE] = "Measurement Scale"
      tagNameMap[TAG_TIMELINE_INFORMATION] = "Timeline Information"
      tagNameMap[TAG_SHEET_DISCLOSURE] = "Sheet Disclosure"
      tagNameMap[TAG_DISPLAY_INFO] = "Display Info"
      tagNameMap[TAG_ONION_SKINS] = "Onion Skins"
      tagNameMap[TAG_COUNT_INFORMATION] = "Count information"
      tagNameMap[TAG_PRINT_INFO_2] = "Print Info 2"
      tagNameMap[TAG_PRINT_STYLE] = "Print Style"
      tagNameMap[TAG_MAC_NSPRINTINFO] = "Mac NSPrintInfo"
      tagNameMap[TAG_WIN_DEVMODE] = "Win DEVMODE"
      tagNameMap[TAG_AUTO_SAVE_FILE_PATH] = "Auto Save File Subpath"
      tagNameMap[TAG_AUTO_SAVE_FORMAT] = "Auto Save Format"
      tagNameMap[TAG_PATH_SELECTION_STATE] = "Subpath Selection State"
      tagNameMap[TAG_CLIPPING_PATH_NAME] = "Clipping Path Name"
      tagNameMap[TAG_ORIGIN_PATH_INFO] = "Origin Subpath Info"
      tagNameMap[TAG_IMAGE_READY_VARIABLES_XML] = "Image Ready Variables XML"
      tagNameMap[TAG_IMAGE_READY_DATA_SETS] = "Image Ready Data Sets"
      tagNameMap[TAG_IMAGE_READY_SELECTED_STATE] = "Image Ready Selected State"
      tagNameMap[TAG_IMAGE_READY_7_ROLLOVER] = "Image Ready 7 Rollover Expanded State"
      tagNameMap[TAG_IMAGE_READY_ROLLOVER] = "Image Ready Rollover Expanded State"
      tagNameMap[TAG_IMAGE_READY_SAVE_LAYER_SETTINGS] = "Image Ready Save Layer Settings"
      tagNameMap[TAG_IMAGE_READY_VERSION] = "Image Ready Version"
      tagNameMap[TAG_LIGHTROOM_WORKFLOW] = "Lightroom Workflow"
      tagNameMap[TAG_PRINT_FLAGS_INFO] = "Print Flags Information"
    }
  }

  override val name: String
    get() = "Photoshop"

  val thumbnailBytes: ByteArray?
    get() {
      var storedBytes = getByteArray(TAG_THUMBNAIL)
      if (storedBytes == null) storedBytes = getByteArray(TAG_THUMBNAIL_OLD)
      if (storedBytes == null || storedBytes.size <= 28) return null
      val thumbSize = storedBytes.size - 28
      val thumbBytes = ByteArray(thumbSize)
      System.arraycopy(storedBytes, 28, thumbBytes, 0, thumbSize)
      return thumbBytes
    }

  init {
    setDescriptor(PhotoshopDescriptor(this))
  }
}
