@file:JvmName("Mp4MetadataReader")

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
package com.drew.imaging.mp4

import com.drew.imaging.ImageProcessingException
import com.drew.metadata.Metadata
import com.drew.metadata.file.FileSystemMetadataReader
import com.drew.metadata.mp4.Mp4BoxHandler
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream

/**
 * @author Payton Garland
 */
@Throws(ImageProcessingException::class, IOException::class)
fun readMetadata(file: File): Metadata {
  val inputStream: InputStream = FileInputStream(file)
  val metadata: Metadata
  metadata = inputStream.use { inputStream ->
    readMetadata(inputStream)
  }
  FileSystemMetadataReader().read(file, metadata)
  return metadata
}

@Throws(IOException::class)
fun readMetadata(inputStream: InputStream): Metadata {
  val metadata = Metadata()
  Mp4Reader.extract(inputStream, Mp4BoxHandler(metadata))
  return metadata
}

