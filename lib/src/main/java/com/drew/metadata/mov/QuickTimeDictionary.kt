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

import com.drew.metadata.Directory
import com.drew.metadata.mov.media.QuickTimeSoundDirectory
import com.drew.metadata.mov.media.QuickTimeVideoDirectory

/**
 * @author Payton Garland
 */
object QuickTimeDictionary {
  private val dictionary = HashMap<Int, HashMap<String, String>>()
  private val majorBrands = HashMap<String, String>()
  private val videoCompressionTypes = HashMap<String, String>()
  private val soundAudioFormats = HashMap<String, String>()
  private val vendorIds = HashMap<String, String>()
  @JvmStatic
  fun lookup(scope: Int, lookup: String): String {
    return  dictionary[scope]?.get(lookup) ?: "Unknown"
  }

  @JvmStatic
  fun setLookup(scope: Int, lookup: String, directory: Directory) {
    val results = lookup(scope, lookup)
    directory.setString(scope, results)
  }

  init {
    dictionary[QuickTimeDirectory.TAG_MAJOR_BRAND] = majorBrands
    dictionary[QuickTimeVideoDirectory.TAG_COMPRESSION_TYPE] = videoCompressionTypes
    dictionary[QuickTimeSoundDirectory.TAG_AUDIO_FORMAT] = soundAudioFormats
    dictionary[QuickTimeVideoDirectory.TAG_VENDOR] = vendorIds
    // Video Compression Types
    videoCompressionTypes["3IVX"] = "3ivx MPEG-4"
    videoCompressionTypes["3IV1"] = "3ivx MPEG-4 v1"
    videoCompressionTypes["3IV2"] = "3ivx MPEG-4 v2"
    videoCompressionTypes["avr "] = "AVR-JPEG"
    videoCompressionTypes["base"] = "Base"
    videoCompressionTypes["WRLE"] = "BMP"
    videoCompressionTypes["cvid"] = "Cinepak"
    videoCompressionTypes["clou"] = "Cloud"
    videoCompressionTypes["cmyk"] = "CMYK"
    videoCompressionTypes["yuv2"] = "ComponentVideo"
    videoCompressionTypes["yuvu"] = "ComponentVideoSigned"
    videoCompressionTypes["yuvs"] = "ComponentVideoUnsigned"
    videoCompressionTypes["dvc "] = "DVC-NTSC"
    videoCompressionTypes["dvcp"] = "DVC-PAL"
    videoCompressionTypes["dvpn"] = "DVCPro-NTSC"
    videoCompressionTypes["dvpp"] = "DVCPro-PAL"
    videoCompressionTypes["fire"] = "Fire"
    videoCompressionTypes["flic"] = "FLC"
    videoCompressionTypes["b48r"] = "48RGB"
    videoCompressionTypes["gif "] = "GIF"
    videoCompressionTypes["smc "] = "Graphics"
    videoCompressionTypes["h261"] = "Apple H261"
    videoCompressionTypes["h263"] = "Apple VC H.263"
    videoCompressionTypes["IV41"] = "Indeo4"
    videoCompressionTypes["jpeg"] = "JPEG"
    videoCompressionTypes["PNTG"] = "MacPaint"
    videoCompressionTypes["msvc"] = "Microsoft Video1"
    videoCompressionTypes["mjpa"] = "Apple Motion JPEG-A"
    videoCompressionTypes["mjpb"] = "Apple Motion JPEG-B"
    videoCompressionTypes["myuv"] = "MPEG YUV420"
    videoCompressionTypes["dmb1"] = "OpenDML JPEG"
    videoCompressionTypes["kpcd"] = "PhotoCD"
    videoCompressionTypes["8BPS"] = "Planar RGB"
    videoCompressionTypes["png "] = "PNG"
    videoCompressionTypes["qdrw"] = "QuickDraw"
    videoCompressionTypes["qdgx"] = "QuickDrawGX"
    videoCompressionTypes["raw "] = "RAW"
    videoCompressionTypes[".SGI"] = "SGI"
    videoCompressionTypes["b16g"] = "16Gray"
    videoCompressionTypes["b64a"] = "64ARGB"
    videoCompressionTypes["SVQ1"] = "Sorenson Video 1"
    videoCompressionTypes["SVQ3"] = "Sorenson Video 3"
    videoCompressionTypes["syv9"] = "Sorenson YUV9"
    videoCompressionTypes["tga "] = "Targa"
    videoCompressionTypes["b32a"] = "32AlphaGray"
    videoCompressionTypes["tiff"] = "TIFF"
    videoCompressionTypes["path"] = "Vector"
    videoCompressionTypes["rpza"] = "Video (Road Pizza)"
    videoCompressionTypes["ripl"] = "WaterRipple"
    videoCompressionTypes["WRAW"] = "Windows RAW"
    videoCompressionTypes["y420"] = "YUV420"
    videoCompressionTypes["avc1"] = "H.264"
    videoCompressionTypes["mp4v"] = "MPEG-4"
    videoCompressionTypes["MP4V"] = "MPEG-4"
    videoCompressionTypes["dvhp"] = "DVCPRO HD 720p60"
    videoCompressionTypes["hdv2"] = "HDV 1080i60"
    videoCompressionTypes["dvc+"] = "DV/DVCPRO - NTSC"
    videoCompressionTypes["mx5p"] = "MPEG2 IMX 635/50 50mb/s"
    videoCompressionTypes["mx3n"] = "MPEG2 IMX 635/50 30mb/s"
    videoCompressionTypes["dv5p"] = "DVCPRO50"
    videoCompressionTypes["hdv3"] = "HDV Final Cut Pro"
    videoCompressionTypes["rle "] = "Animation"
    videoCompressionTypes["rle "] = "Animation"
    videoCompressionTypes["2vuY"] = "Uncompressed Y'CbCr, 8-bit-per-component 4:2:2"
    videoCompressionTypes["v308"] = "Uncompressed Y'CbCr, 8-bit-per-component 4:4:4"
    videoCompressionTypes["v408"] = "Uncompressed Y'CbCr, 8-bit-per-component 4:4:4:4"
    videoCompressionTypes["v216"] = "Uncompressed Y'CbCr, 10, 12, 14, or 16-bit-per-component 4:2:2"
    videoCompressionTypes["v410"] = "Uncompressed Y'CbCr, 10-bit-per-component 4:4:4"
    videoCompressionTypes["v210"] = "Uncompressed Y'CbCr, 10-bit-per-component 4:2:2"
    // Sound Audio Formats
    soundAudioFormats["NONE"] = ""
    soundAudioFormats["raw "] = "Uncompressed in offset-binary format"
    soundAudioFormats["twos"] = "Uncompressed in two's-complement format"
    soundAudioFormats["sowt"] = "16-bit little-endian, twos-complement"
    soundAudioFormats["MAC3"] = "MACE 3:1"
    soundAudioFormats["MAC6"] = "MACE 6:1"
    soundAudioFormats["ima4"] = "IMA 4:1"
    soundAudioFormats["fl32"] = "32-bit floating point"
    soundAudioFormats["fl64"] = "64-bit floating point"
    soundAudioFormats["in24"] = "24-bit integer"
    soundAudioFormats["in32"] = "32-bit integer"
    soundAudioFormats["ulaw"] = "uLaw 2:1"
    soundAudioFormats["alaw"] = "uLaw 2:1"
    soundAudioFormats[String(byteArrayOf(0x6D, 0x73, 0x00, 0x02))] = "Microsoft ADPCM-ACM code 2"
    soundAudioFormats[String(byteArrayOf(0x6D, 0x73, 0x00, 0x11))] = "DVI/Intel IMAADPCM-ACM code 17"
    soundAudioFormats["dvca"] = "DV Audio"
    soundAudioFormats["QDMC"] = "QDesign music"
    soundAudioFormats["QDM2"] = "QDesign music version 2"
    soundAudioFormats["Qclp"] = "QUALCOMM PureVoice"
    soundAudioFormats[String(byteArrayOf(0x6D, 0x73, 0x00, 0x55))] = "MPEG-1 layer 3, CBR only (pre-QT4.1)"
    soundAudioFormats[".mp3"] = "MPEG-1 layer 3, CBR & VBR (QT4.1 and later)"
    soundAudioFormats["mp4a"] = "MPEG-4, Advanced Audio Coding (AAC)"
    soundAudioFormats["ac-3"] = "Digital Audio Compression Standard (AC-3, Enhanced AC-3)"
    soundAudioFormats["aac "] = "ISO/IEC 144963-3 AAC"
    soundAudioFormats["agsm"] = "Apple GSM 10:1"
    soundAudioFormats["alac"] = "Apple Lossless Audio Codec"
    soundAudioFormats["conv"] = "Sample Format"
    soundAudioFormats["dvi "] = "DV 4:1"
    soundAudioFormats["eqal"] = "Frequency Equalizer"
    soundAudioFormats["lpc "] = "LPC 23:1"
    soundAudioFormats["mixb"] = "8-bit Mixer"
    soundAudioFormats["mixw"] = "16-bit Mixer"
    soundAudioFormats[String(byteArrayOf(0x4d, 0x53, 0x00, 0x02))] = "Microsoft ADPCM"
    soundAudioFormats[String(byteArrayOf(0x4d, 0x53, 0x00, 0x11))] = "DV IMA"
    soundAudioFormats[String(byteArrayOf(0x4d, 0x53, 0x00, 0x55))] = "MPEG3"
    soundAudioFormats["ratb"] = "8-bit Rate"
    soundAudioFormats["ratw"] = "16-bit Rate"
    soundAudioFormats["sour"] = "Sound Source"
    soundAudioFormats["str1"] = "Iomega MPEG layer II"
    soundAudioFormats["str2"] = "Iomega MPEG *layer II"
    soundAudioFormats["str3"] = "Iomega MPEG **layer II"
    soundAudioFormats["str4"] = "Iomega MPEG ***layer II"
    soundAudioFormats["lpcm"] = "Linear Pulse Code Modulation"
    // Major Brands
    majorBrands["3g2a"] = "3GPP2 Media (.3G2) compliant with 3GPP2 C.S0050-0 V1.0"
    majorBrands["3g2b"] = "3GPP2 Media (.3G2) compliant with 3GPP2 C.S0050-A V1.0.0"
    majorBrands["3g2c"] = "3GPP2 Media (.3G2) compliant with 3GPP2 C.S0050-B v1.0"
    majorBrands["3ge6"] = "3GPP (.3GP) Release 6 MBMS Extended Presentations"
    majorBrands["3ge7"] = "3GPP (.3GP) Release 7 MBMS Extended Presentations"
    majorBrands["3gg6"] = "3GPP Release 6 General Profile"
    majorBrands["3gp1"] = "3GPP Media (.3GP) Release 1 (probably non-existent)"
    majorBrands["3gp2"] = "3GPP Media (.3GP) Release 2 (probably non-existent)"
    majorBrands["3gp3"] = "3GPP Media (.3GP) Release 3 (probably non-existent)"
    majorBrands["3gp4"] = "3GPP Media (.3GP) Release 4"
    majorBrands["3gp5"] = "3GPP Media (.3GP) Release 5"
    majorBrands["3gp6"] = "3GPP Media (.3GP) Release 6 Basic Profile"
    majorBrands["3gp6"] = "3GPP Media (.3GP) Release 6 Progressive Download"
    majorBrands["3gp6"] = "3GPP Media (.3GP) Release 6 Streaming Servers"
    majorBrands["3gs7"] = "3GPP Media (.3GP) Release 7 Streaming Servers"
    majorBrands["avc1"] = "MP4 Base w/ AVC ext [ISO 14496-12:2005]"
    majorBrands["CAEP"] = "Canon Digital Camera"
    majorBrands["caqv"] = "Casio Digital Camera"
    majorBrands["CDes"] = "Convergent Design"
    majorBrands["da0a"] = "DMB MAF w/ MPEG Layer II aud, MOT slides, DLS, JPG/PNG/MNG images"
    majorBrands["da0b"] = "DMB MAF, extending DA0A, with 3GPP timed text, DID, TVA, REL, IPMP"
    majorBrands["da1a"] = "DMB MAF audio with ER-BSAC audio, JPG/PNG/MNG images"
    majorBrands["da1b"] = "DMB MAF, extending da1a, with 3GPP timed text, DID, TVA, REL, IPMP"
    majorBrands["da2a"] = "DMB MAF aud w/ HE-AAC v2 aud, MOT slides, DLS, JPG/PNG/MNG images"
    majorBrands["da2b"] = "DMB MAF, extending da2a, with 3GPP timed text, DID, TVA, REL, IPMP"
    majorBrands["da3a"] = "DMB MAF aud with HE-AAC aud, JPG/PNG/MNG images"
    majorBrands["da3b"] = "DMB MAF, extending da3a w/ BIFS, 3GPP timed text, DID, TVA, REL, IPMP"
    majorBrands["dmb1"] = "DMB MAF supporting all the components defined in the specification"
    majorBrands["dmpf"] = "Digital Media Project"
    majorBrands["drc1"] = "Dirac (wavelet compression), encapsulated in ISO base media (MP4)"
    majorBrands["dv1a"] = "DMB MAF vid w/ AVC vid, ER-BSAC aud, BIFS, JPG/PNG/MNG images, TS"
    majorBrands["dv1b"] = "DMB MAF, extending dv1a, with 3GPP timed text, DID, TVA, REL, IPMP"
    majorBrands["dv2a"] = "DMB MAF vid w/ AVC vid, HE-AAC v2 aud, BIFS, JPG/PNG/MNG images, TS"
    majorBrands["dv2b"] = "DMB MAF, extending dv2a, with 3GPP timed text, DID, TVA, REL, IPMP"
    majorBrands["dv3a"] = "DMB MAF vid w/ AVC vid, HE-AAC aud, BIFS, JPG/PNG/MNG images, TS"
    majorBrands["dv3b"] = "DMB MAF, extending dv3a, with 3GPP timed text, DID, TVA, REL, IPMP"
    majorBrands["dvr1"] = "DVB (.DVB) over RTP"
    majorBrands["dvt1"] = "DVB (.DVB) over MPEG-2 Transport Stream"
    majorBrands["F4V "] = "Video for Adobe Flash Player 9+ (.F4V)"
    majorBrands["F4P "] = "Protected Video for Adobe Flash Player 9+ (.F4P)"
    majorBrands["F4A "] = "Audio for Adobe Flash Player 9+ (.F4A)"
    majorBrands["F4B "] = "Audio Book for Adobe Flash Player 9+ (.F4B)"
    majorBrands["isc2"] = "ISMACryp 2.0 Encrypted File"
    majorBrands["iso2"] = "MP4 Base Media v2 [ISO 14496-12:2005]"
    majorBrands["isom"] = "MP4  Base Media v1 [IS0 14496-12:2003]"
    majorBrands["JP2 "] = "JPEG 2000 Image (.JP2) [ISO 15444-1 ?]"
    majorBrands["JP20"] = "Unknown, from GPAC samples (prob non-existent)"
    majorBrands["jpm "] = "JPEG 2000 Compound Image (.JPM) [ISO 15444-6]"
    majorBrands["jpx "] = "JPEG 2000 w/ extensions (.JPX) [ISO 15444-2]"
    majorBrands["KDDI"] = "3GPP2 EZmovie for KDDI 3G cellphones"
    majorBrands["M4A "] = "Apple iTunes AAC-LC (.M4A) Audio"
    majorBrands["M4B "] = "Apple iTunes AAC-LC (.M4B) Audio Book"
    majorBrands["M4P "] = "Apple iTunes AAC-LC (.M4P) AES Protected Audio"
    majorBrands["M4V "] = "Apple iTunes Video (.M4V) Video"
    majorBrands["M4VH"] = "Apple TV (.M4V)"
    majorBrands["M4VP"] = "Apple iPhone (.M4V)"
    majorBrands["mj2s"] = "Motion JPEG 2000 [ISO 15444-3] Simple Profile"
    majorBrands["mjp2"] = "Motion JPEG 2000 [ISO 15444-3] General Profile"
    majorBrands["mmp4"] = "MPEG-4/3GPP Mobile Profile (.MP4 / .3GP) (for NTT)"
    majorBrands["mp21"] = "MPEG-21 [ISO/IEC 21000-9]"
    majorBrands["mp41"] = "MP4 v1 [ISO 14496-1:ch13]"
    majorBrands["mp42"] = "MP4 v2 [ISO 14496-14]"
    majorBrands["mp71"] = "MP4 w/ MPEG-7 Metadata [per ISO 14496-12]"
    majorBrands["MPPI"] = "Photo Player, MAF [ISO/IEC 23000-3]"
    majorBrands["mqt "] = "Sony / Mobile QuickTime (.MQV)  US Patent 7,477,830 (Sony Corp)"
    majorBrands["MSNV"] = "MPEG-4 (.MP4) for SonyPSP"
    majorBrands["NDAS"] = "MP4 v2 [ISO 14496-14] Nero Digital AAC Audio"
    majorBrands["NDSC"] = "MPEG-4 (.MP4) Nero Cinema Profile"
    majorBrands["NDSH"] = "MPEG-4 (.MP4) Nero HDTV Profile"
    majorBrands["NDSM"] = "MPEG-4 (.MP4) Nero Mobile Profile"
    majorBrands["NDSP"] = "MPEG-4 (.MP4) Nero Portable Profile"
    majorBrands["NDSS"] = "MPEG-4 (.MP4) Nero Standard Profile"
    majorBrands["NDXC"] = "H.264/MPEG-4 AVC (.MP4) Nero Cinema Profile"
    majorBrands["NDXH"] = "H.264/MPEG-4 AVC (.MP4) Nero HDTV Profile"
    majorBrands["NDXM"] = "H.264/MPEG-4 AVC (.MP4) Nero Mobile Profile"
    majorBrands["NDXP"] = "H.264/MPEG-4 AVC (.MP4) Nero Portable Profile"
    majorBrands["NDXS"] = "H.264/MPEG-4 AVC (.MP4) Nero Standard Profile"
    majorBrands["odcf"] = "OMA DCF DRM Format 2.0 (OMA-TS-DRM-DCF-V2_0-20060303-A)"
    majorBrands["opf2"] = "OMA PDCF DRM Format 2.1 (OMA-TS-DRM-DCF-V2_1-20070724-C)"
    majorBrands["opx2"] = "OMA PDCF DRM + XBS extensions (OMA-TS-DRM_XBS-V1_0-20070529-C)"
    majorBrands["pana"] = "Panasonic Digital Camera"
    majorBrands["qt  "] = "Apple QuickTime (.MOV/QT)"
    majorBrands["ROSS"] = "Ross Video"
    majorBrands["sdv "] = "SD Memory Card Video"
    majorBrands["ssc1"] = "Samsung stereoscopic, single stream (patent pending, see notes)"
    majorBrands["ssc2"] = "Samsung stereoscopic, dual stream (patent pending, see notes)"
    // Vendor ID's https://sno.phy.queensu.ca/~phil/exiftool/TagNames/QuickTime.html#Meta
    vendorIds[" KD "] = "Kodak"
    vendorIds["AR.D"] = "Parrot AR.Drone"
    vendorIds["FFMP"] = "FFmpeg"
    vendorIds["GIC "] = "General Imaging Co."
    vendorIds["KMPI"] = "Konica-Minolta"
    vendorIds["NIKO"] = "Nikon"
    vendorIds["SMI "] = "Sorenson Media Inc."
    vendorIds["ZORA"] = "Zoran Corporation"
    vendorIds["appl"] = "Apple"
    vendorIds["fe20"] = "Olympus (fe20)"
    vendorIds["kdak"] = "Kodak"
    vendorIds["leic"] = "Leica"
    vendorIds["mino"] = "Minolta"
    vendorIds["niko"] = "Nikon"
    vendorIds["olym"] = "Olympus"
    vendorIds["pana"] = "Panasonic"
    vendorIds["pent"] = "Pentax"
    vendorIds["pr01"] = "Olympus (pr01)"
    vendorIds["sany"] = "Sanyo"
  }
}
