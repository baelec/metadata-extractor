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
package com.drew.metadata.icc

import com.drew.metadata.Directory
import java.util.*

/**
 * @author Yuri Binev
 * @author Drew Noakes https://drewnoakes.com
 */
class IccDirectory : Directory() {
  override val tagNameMap: HashMap<Int, String> = Companion.tagNameMap
  companion object {
    // These (smaller valued) tags have an integer value that's equal to their offset within the ICC data buffer.
    const val TAG_PROFILE_BYTE_COUNT = 0
    const val TAG_CMM_TYPE = 4
    const val TAG_PROFILE_VERSION = 8
    const val TAG_PROFILE_CLASS = 12
    const val TAG_COLOR_SPACE = 16
    const val TAG_PROFILE_CONNECTION_SPACE = 20
    const val TAG_PROFILE_DATETIME = 24
    const val TAG_SIGNATURE = 36
    const val TAG_PLATFORM = 40
    const val TAG_CMM_FLAGS = 44
    const val TAG_DEVICE_MAKE = 48
    const val TAG_DEVICE_MODEL = 52
    const val TAG_DEVICE_ATTR = 56
    const val TAG_RENDERING_INTENT = 64
    const val TAG_XYZ_VALUES = 68
    const val TAG_PROFILE_CREATOR = 80
    const val TAG_TAG_COUNT = 128
    // These tag values
    const val TAG_TAG_A2B0 = 0x41324230
    const val TAG_TAG_A2B1 = 0x41324231
    const val TAG_TAG_A2B2 = 0x41324232
    const val TAG_TAG_bXYZ = 0x6258595A
    const val TAG_TAG_bTRC = 0x62545243
    const val TAG_TAG_B2A0 = 0x42324130
    const val TAG_TAG_B2A1 = 0x42324131
    const val TAG_TAG_B2A2 = 0x42324132
    const val TAG_TAG_calt = 0x63616C74
    const val TAG_TAG_targ = 0x74617267
    const val TAG_TAG_chad = 0x63686164
    const val TAG_TAG_chrm = 0x6368726D
    const val TAG_TAG_cprt = 0x63707274
    const val TAG_TAG_crdi = 0x63726469
    const val TAG_TAG_dmnd = 0x646D6E64
    const val TAG_TAG_dmdd = 0x646D6464
    const val TAG_TAG_devs = 0x64657673
    const val TAG_TAG_gamt = 0x67616D74
    const val TAG_TAG_kTRC = 0x6B545243
    const val TAG_TAG_gXYZ = 0x6758595A
    const val TAG_TAG_gTRC = 0x67545243
    const val TAG_TAG_lumi = 0x6C756D69
    const val TAG_TAG_meas = 0x6D656173
    const val TAG_TAG_bkpt = 0x626B7074
    const val TAG_TAG_wtpt = 0x77747074
    const val TAG_TAG_ncol = 0x6E636F6C
    const val TAG_TAG_ncl2 = 0x6E636C32
    const val TAG_TAG_resp = 0x72657370
    const val TAG_TAG_pre0 = 0x70726530
    const val TAG_TAG_pre1 = 0x70726531
    const val TAG_TAG_pre2 = 0x70726532
    const val TAG_TAG_desc = 0x64657363
    const val TAG_TAG_pseq = 0x70736571
    const val TAG_TAG_psd0 = 0x70736430
    const val TAG_TAG_psd1 = 0x70736431
    const val TAG_TAG_psd2 = 0x70736432
    const val TAG_TAG_psd3 = 0x70736433
    const val TAG_TAG_ps2s = 0x70733273
    const val TAG_TAG_ps2i = 0x70733269
    const val TAG_TAG_rXYZ = 0x7258595A
    const val TAG_TAG_rTRC = 0x72545243
    const val TAG_TAG_scrd = 0x73637264
    const val TAG_TAG_scrn = 0x7363726E
    const val TAG_TAG_tech = 0x74656368
    const val TAG_TAG_bfd = 0x62666420
    const val TAG_TAG_vued = 0x76756564
    const val TAG_TAG_view = 0x76696577
    const val TAG_TAG_aabg = 0x61616267
    const val TAG_TAG_aagg = 0x61616767
    const val TAG_TAG_aarg = 0x61617267
    const val TAG_TAG_mmod = 0x6D6D6F64
    const val TAG_TAG_ndin = 0x6E64696E
    const val TAG_TAG_vcgt = 0x76636774
    const val TAG_APPLE_MULTI_LANGUAGE_PROFILE_NAME = 0x6473636d
    protected val tagNameMap = HashMap<Int, String>()

    init {
      tagNameMap[TAG_PROFILE_BYTE_COUNT] = "Profile Size"
      tagNameMap[TAG_CMM_TYPE] = "CMM Type"
      tagNameMap[TAG_PROFILE_VERSION] = "Version"
      tagNameMap[TAG_PROFILE_CLASS] = "Class"
      tagNameMap[TAG_COLOR_SPACE] = "Color space"
      tagNameMap[TAG_PROFILE_CONNECTION_SPACE] = "Profile Connection Space"
      tagNameMap[TAG_PROFILE_DATETIME] = "Profile Date/Time"
      tagNameMap[TAG_SIGNATURE] = "Signature"
      tagNameMap[TAG_PLATFORM] = "Primary Platform"
      tagNameMap[TAG_CMM_FLAGS] = "CMM Flags"
      tagNameMap[TAG_DEVICE_MAKE] = "Device manufacturer"
      tagNameMap[TAG_DEVICE_MODEL] = "Device model"
      tagNameMap[TAG_DEVICE_ATTR] = "Device attributes"
      tagNameMap[TAG_RENDERING_INTENT] = "Rendering Intent"
      tagNameMap[TAG_XYZ_VALUES] = "XYZ values"
      tagNameMap[TAG_PROFILE_CREATOR] = "Profile Creator"
      tagNameMap[TAG_TAG_COUNT] = "Tag Count"
      tagNameMap[TAG_TAG_A2B0] = "AToB 0"
      tagNameMap[TAG_TAG_A2B1] = "AToB 1"
      tagNameMap[TAG_TAG_A2B2] = "AToB 2"
      tagNameMap[TAG_TAG_bXYZ] = "Blue Colorant"
      tagNameMap[TAG_TAG_bTRC] = "Blue TRC"
      tagNameMap[TAG_TAG_B2A0] = "BToA 0"
      tagNameMap[TAG_TAG_B2A1] = "BToA 1"
      tagNameMap[TAG_TAG_B2A2] = "BToA 2"
      tagNameMap[TAG_TAG_calt] = "Calibration Date/Time"
      tagNameMap[TAG_TAG_targ] = "Char Target"
      tagNameMap[TAG_TAG_chad] = "Chromatic Adaptation"
      tagNameMap[TAG_TAG_chrm] = "Chromaticity"
      tagNameMap[TAG_TAG_cprt] = "Copyright"
      tagNameMap[TAG_TAG_crdi] = "CrdInfo"
      tagNameMap[TAG_TAG_dmnd] = "Device Mfg Description"
      tagNameMap[TAG_TAG_dmdd] = "Device Model Description"
      tagNameMap[TAG_TAG_devs] = "Device Settings"
      tagNameMap[TAG_TAG_gamt] = "Gamut"
      tagNameMap[TAG_TAG_kTRC] = "Gray TRC"
      tagNameMap[TAG_TAG_gXYZ] = "Green Colorant"
      tagNameMap[TAG_TAG_gTRC] = "Green TRC"
      tagNameMap[TAG_TAG_lumi] = "Luminance"
      tagNameMap[TAG_TAG_meas] = "Measurement"
      tagNameMap[TAG_TAG_bkpt] = "Media Black Point"
      tagNameMap[TAG_TAG_wtpt] = "Media White Point"
      tagNameMap[TAG_TAG_ncol] = "Named Color"
      tagNameMap[TAG_TAG_ncl2] = "Named Color 2"
      tagNameMap[TAG_TAG_resp] = "Output Response"
      tagNameMap[TAG_TAG_pre0] = "Preview 0"
      tagNameMap[TAG_TAG_pre1] = "Preview 1"
      tagNameMap[TAG_TAG_pre2] = "Preview 2"
      tagNameMap[TAG_TAG_desc] = "Profile Description"
      tagNameMap[TAG_TAG_pseq] = "Profile Sequence Description"
      tagNameMap[TAG_TAG_psd0] = "Ps2 CRD 0"
      tagNameMap[TAG_TAG_psd1] = "Ps2 CRD 1"
      tagNameMap[TAG_TAG_psd2] = "Ps2 CRD 2"
      tagNameMap[TAG_TAG_psd3] = "Ps2 CRD 3"
      tagNameMap[TAG_TAG_ps2s] = "Ps2 CSA"
      tagNameMap[TAG_TAG_ps2i] = "Ps2 Rendering Intent"
      tagNameMap[TAG_TAG_rXYZ] = "Red Colorant"
      tagNameMap[TAG_TAG_rTRC] = "Red TRC"
      tagNameMap[TAG_TAG_scrd] = "Screening Desc"
      tagNameMap[TAG_TAG_scrn] = "Screening"
      tagNameMap[TAG_TAG_tech] = "Technology"
      tagNameMap[TAG_TAG_bfd] = "Ucrbg"
      tagNameMap[TAG_TAG_vued] = "Viewing Conditions Description"
      tagNameMap[TAG_TAG_view] = "Viewing Conditions"
      tagNameMap[TAG_TAG_aabg] = "Blue Parametric TRC"
      tagNameMap[TAG_TAG_aagg] = "Green Parametric TRC"
      tagNameMap[TAG_TAG_aarg] = "Red Parametric TRC"
      tagNameMap[TAG_TAG_mmod] = "Make And Model"
      tagNameMap[TAG_TAG_ndin] = "Native Display Information"
      tagNameMap[TAG_TAG_vcgt] = "Video Card Gamma"
      tagNameMap[TAG_APPLE_MULTI_LANGUAGE_PROFILE_NAME] = "Apple Multi-language Profile Name"
    }
  }

  override val name: String
    get() = "ICC Profile"

  init {
    setDescriptor(IccDescriptor(this))
  }
}
