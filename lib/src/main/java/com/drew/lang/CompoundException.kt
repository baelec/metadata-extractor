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

import java.io.PrintStream
import java.io.PrintWriter

/**
 * Represents a compound exception, as modelled in JDK 1.4, but
 * unavailable in previous versions.  This class allows support
 * of these previous JDK versions.
 *
 * @author Drew Noakes https://drewnoakes.com
 */
open class CompoundException @JvmOverloads constructor(msg: String?, val innerException: Throwable? = null) : Exception(msg) {

  constructor(exception: Throwable?) : this(null, exception) {}

  override fun toString(): String {
    val string = StringBuilder()
    string.append(super.toString())
    if (innerException != null) {
      string.append("\n")
      string.append("--- inner exception ---")
      string.append("\n")
      string.append(innerException.toString())
    }
    return string.toString()
  }

  override fun printStackTrace(s: PrintStream) {
    super.printStackTrace(s)
    if (innerException != null) {
      s.println("--- inner exception ---")
      innerException.printStackTrace(s)
    }
  }

  override fun printStackTrace(s: PrintWriter) {
    super.printStackTrace(s)
    if (innerException != null) {
      s.println("--- inner exception ---")
      innerException.printStackTrace(s)
    }
  }

  override fun printStackTrace() {
    super.printStackTrace()
    if (innerException != null) {
      System.err.println("--- inner exception ---")
      innerException.printStackTrace()
    }
  }

  companion object {
    private const val serialVersionUID = -9207883813472069925L
  }

}
