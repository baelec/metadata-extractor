@file:JvmName("Charsets")

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
package com.drew.lang

import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

/**
 * Holds a set of commonly used character encodings.
 *
 * Newer JDKs include java.nio.charset.StandardCharsets, but we cannot use that in this library.
 *
 * @author Drew Noakes https://drewnoakes.com
 */
@JvmField
val UTF_8 = StandardCharsets.UTF_8
val UTF_16 = StandardCharsets.UTF_16
@JvmField
val ISO_8859_1 = StandardCharsets.ISO_8859_1
@JvmField
val ASCII = StandardCharsets.US_ASCII
@JvmField
val UTF_16BE = StandardCharsets.UTF_16BE
@JvmField
val UTF_16LE = StandardCharsets.UTF_16LE
@JvmField
val WINDOWS_1252 = Charset.forName("Cp1252")
