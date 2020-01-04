@file:JvmName("Schema")
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
package com.drew.metadata

/**
 * XMP tag namespace. TODO the older "xap", "xapBJ", "xapMM" or "xapRights" namespace prefixes should be translated to the newer "xmp", "xmpBJ",
 * "xmpMM" and "xmpRights" prefixes for use in family 1 group names
 */
const val XMP_PROPERTIES = "http://ns.adobe.com/xap/1.0/"
const val EXIF_SPECIFIC_PROPERTIES = "http://ns.adobe.com/exif/1.0/"
const val EXIF_ADDITIONAL_PROPERTIES = "http://ns.adobe.com/exif/1.0/aux/"
const val EXIF_TIFF_PROPERTIES = "http://ns.adobe.com/tiff/1.0/"
const val DUBLIN_CORE_SPECIFIC_PROPERTIES = "http://purl.org/dc/elements/1.1/"
