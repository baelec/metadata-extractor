@file:JvmName("DateUtil")
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

import java.util.*

/**
 * @author Drew Noakes http://drewnoakes.com
 */
  private val daysInMonth365 = intArrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
  /**
   * The offset (in milliseconds) to add to a MP4 date/time integer value to
   * align with Java's Epoch.
   */
  private const val EPOCH_1_JAN_1904 = -2082844799175L

  fun isValidDate(year: Int, month: Int, day: Int): Boolean {
    if (year !in 1..9999 || month !in 0..11) {
      return false
    }
    var daysInMonth = daysInMonth365[month]
    if (month == 1) {
      val isLeapYear = year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)
      if (isLeapYear) daysInMonth++
    }
    return day in 1..daysInMonth
  }

  fun isValidTime(hours: Int, minutes: Int, seconds: Int): Boolean {
    return hours in 0..23 && minutes in 0..59 && seconds in 0..59
  }

  fun get1Jan1904EpochDate(seconds: Long): Date {
    return Date(seconds * 1000 + EPOCH_1_JAN_1904)
  }
