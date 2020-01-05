/*
 * Copyright 2002-2015 Drew Noakes
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

import com.drew.metadata.Directory
import java.util.*

/**
 * The Olympus camera settings makernote is used by many manufacturers (Epson, Konica, Minolta and Agfa...), and as such contains some tags
 * that appear specific to those manufacturers.
 *
 * @author Kevin Mott https://github.com/kwhopper
 * @author Drew Noakes https://drewnoakes.com
 */
class OlympusCameraSettingsMakernoteDirectory : Directory() {
  override val tagNameMap = Companion.tagNameMap

  companion object {
    const val TagCameraSettingsVersion = 0x0000
    const val TagPreviewImageValid = 0x0100
    const val TagPreviewImageStart = 0x0101
    const val TagPreviewImageLength = 0x0102
    const val TagExposureMode = 0x0200
    const val TagAeLock = 0x0201
    const val TagMeteringMode = 0x0202
    const val TagExposureShift = 0x0203
    const val TagNdFilter = 0x0204
    const val TagMacroMode = 0x0300
    const val TagFocusMode = 0x0301
    const val TagFocusProcess = 0x0302
    const val TagAfSearch = 0x0303
    const val TagAfAreas = 0x0304
    const val TagAfPointSelected = 0x0305
    const val TagAfFineTune = 0x0306
    const val TagAfFineTuneAdj = 0x0307
    const val TagFlashMode = 0x400
    const val TagFlashExposureComp = 0x401
    const val TagFlashRemoteControl = 0x403
    const val TagFlashControlMode = 0x404
    const val TagFlashIntensity = 0x405
    const val TagManualFlashStrength = 0x406
    const val TagWhiteBalance2 = 0x500
    const val TagWhiteBalanceTemperature = 0x501
    const val TagWhiteBalanceBracket = 0x502
    const val TagCustomSaturation = 0x503
    const val TagModifiedSaturation = 0x504
    const val TagContrastSetting = 0x505
    const val TagSharpnessSetting = 0x506
    const val TagColorSpace = 0x507
    const val TagSceneMode = 0x509
    const val TagNoiseReduction = 0x50a
    const val TagDistortionCorrection = 0x50b
    const val TagShadingCompensation = 0x50c
    const val TagCompressionFactor = 0x50d
    const val TagGradation = 0x50f
    const val TagPictureMode = 0x520
    const val TagPictureModeSaturation = 0x521
    const val TagPictureModeHue = 0x522
    const val TagPictureModeContrast = 0x523
    const val TagPictureModeSharpness = 0x524
    const val TagPictureModeBWFilter = 0x525
    const val TagPictureModeTone = 0x526
    const val TagNoiseFilter = 0x527
    const val TagArtFilter = 0x529
    const val TagMagicFilter = 0x52c
    const val TagPictureModeEffect = 0x52d
    const val TagToneLevel = 0x52e
    const val TagArtFilterEffect = 0x52f
    const val TagColorCreatorEffect = 0x532
    const val TagDriveMode = 0x600
    const val TagPanoramaMode = 0x601
    const val TagImageQuality2 = 0x603
    const val TagImageStabilization = 0x604
    const val TagStackedImage = 0x804
    const val TagManometerPressure = 0x900
    const val TagManometerReading = 0x901
    const val TagExtendedWBDetect = 0x902
    const val TagRollAngle = 0x903
    const val TagPitchAngle = 0x904
    const val TagDateTimeUtc = 0x908
    protected val tagNameMap = HashMap<Int, String>()

    init {
      tagNameMap[TagCameraSettingsVersion] = "Camera Settings Version"
      tagNameMap[TagPreviewImageValid] = "Preview Image Valid"
      tagNameMap[TagPreviewImageStart] = "Preview Image Start"
      tagNameMap[TagPreviewImageLength] = "Preview Image Length"
      tagNameMap[TagExposureMode] = "Exposure Mode"
      tagNameMap[TagAeLock] = "AE Lock"
      tagNameMap[TagMeteringMode] = "Metering Mode"
      tagNameMap[TagExposureShift] = "Exposure Shift"
      tagNameMap[TagNdFilter] = "ND Filter"
      tagNameMap[TagMacroMode] = "Macro Mode"
      tagNameMap[TagFocusMode] = "Focus Mode"
      tagNameMap[TagFocusProcess] = "Focus Process"
      tagNameMap[TagAfSearch] = "AF Search"
      tagNameMap[TagAfAreas] = "AF Areas"
      tagNameMap[TagAfPointSelected] = "AF Point Selected"
      tagNameMap[TagAfFineTune] = "AF Fine Tune"
      tagNameMap[TagAfFineTuneAdj] = "AF Fine Tune Adj"
      tagNameMap[TagFlashMode] = "Flash Mode"
      tagNameMap[TagFlashExposureComp] = "Flash Exposure Comp"
      tagNameMap[TagFlashRemoteControl] = "Flash Remote Control"
      tagNameMap[TagFlashControlMode] = "Flash Control Mode"
      tagNameMap[TagFlashIntensity] = "Flash Intensity"
      tagNameMap[TagManualFlashStrength] = "Manual Flash Strength"
      tagNameMap[TagWhiteBalance2] = "White Balance 2"
      tagNameMap[TagWhiteBalanceTemperature] = "White Balance Temperature"
      tagNameMap[TagWhiteBalanceBracket] = "White Balance Bracket"
      tagNameMap[TagCustomSaturation] = "Custom Saturation"
      tagNameMap[TagModifiedSaturation] = "Modified Saturation"
      tagNameMap[TagContrastSetting] = "Contrast Setting"
      tagNameMap[TagSharpnessSetting] = "Sharpness Setting"
      tagNameMap[TagColorSpace] = "Color Space"
      tagNameMap[TagSceneMode] = "Scene Mode"
      tagNameMap[TagNoiseReduction] = "Noise Reduction"
      tagNameMap[TagDistortionCorrection] = "Distortion Correction"
      tagNameMap[TagShadingCompensation] = "Shading Compensation"
      tagNameMap[TagCompressionFactor] = "Compression Factor"
      tagNameMap[TagGradation] = "Gradation"
      tagNameMap[TagPictureMode] = "Picture Mode"
      tagNameMap[TagPictureModeSaturation] = "Picture Mode Saturation"
      tagNameMap[TagPictureModeHue] = "Picture Mode Hue"
      tagNameMap[TagPictureModeContrast] = "Picture Mode Contrast"
      tagNameMap[TagPictureModeSharpness] = "Picture Mode Sharpness"
      tagNameMap[TagPictureModeBWFilter] = "Picture Mode BW Filter"
      tagNameMap[TagPictureModeTone] = "Picture Mode Tone"
      tagNameMap[TagNoiseFilter] = "Noise Filter"
      tagNameMap[TagArtFilter] = "Art Filter"
      tagNameMap[TagMagicFilter] = "Magic Filter"
      tagNameMap[TagPictureModeEffect] = "Picture Mode Effect"
      tagNameMap[TagToneLevel] = "Tone Level"
      tagNameMap[TagArtFilterEffect] = "Art Filter Effect"
      tagNameMap[TagColorCreatorEffect] = "Color Creator Effect"
      tagNameMap[TagDriveMode] = "Drive Mode"
      tagNameMap[TagPanoramaMode] = "Panorama Mode"
      tagNameMap[TagImageQuality2] = "Image Quality 2"
      tagNameMap[TagImageStabilization] = "Image Stabilization"
      tagNameMap[TagStackedImage] = "Stacked Image"
      tagNameMap[TagManometerPressure] = "Manometer Pressure"
      tagNameMap[TagManometerReading] = "Manometer Reading"
      tagNameMap[TagExtendedWBDetect] = "Extended WB Detect"
      tagNameMap[TagRollAngle] = "Roll Angle"
      tagNameMap[TagPitchAngle] = "Pitch Angle"
      tagNameMap[TagDateTimeUtc] = "Date Time UTC"
    }
  }

  override val name: String
    get() = "Olympus Camera Settings"

  init {
    setDescriptor(OlympusCameraSettingsMakernoteDescriptor(this))
  }
}
