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

import java.io.Serializable

/**
 * Immutable class for holding a rational number without loss of precision.  Provides
 * a familiar representation via [Rational.toString] in form `numerator/denominator`.
 *
 * Note that any value with a numerator of zero will be treated as zero, even if the
 * denominator is also zero.
 *
 * @author Drew Noakes https://drewnoakes.com
 */
data class Rational
/**
 * Creates a new instance of Rational.  Rational objects are immutable, so
 * once you've set your numerator and denominator values here, you're stuck
 * with them!
 */(
  /** Holds the numerator.  */
  val numerator: Long,
  /** Holds the denominator.  */
  val denominator: Long) : Number(), Comparable<Rational>, Serializable {
  /** Returns the numerator.  */
  /** Returns the denominator.  */

  /**
   * Returns the value of the specified number as a `double`.
   * This may involve rounding.
   *
   * @return the numeric value represented by this object after conversion
   * to type `double`.
   */
  override fun toDouble(): Double {
    return if (numerator == 0L) 0.0 else numerator.toDouble() / denominator.toDouble()
  }

  /**
   * Returns the value of the specified number as a `float`.
   * This may involve rounding.
   *
   * @return the numeric value represented by this object after conversion
   * to type `float`.
   */
  override fun toFloat(): Float {
    return if (numerator == 0L) 0.0f else numerator.toFloat() / denominator.toFloat()
  }

  /**
   * Returns the value of the specified number as a `byte`.
   * This may involve rounding or truncation.  This implementation simply
   * casts the result of [Rational.toDouble] to `byte`.
   *
   * @return the numeric value represented by this object after conversion
   * to type `byte`.
   */
  override fun toByte(): Byte {
    return toDouble().toByte()
  }

  override fun toChar(): Char {
    return toByte().toChar()
  }

  /**
   * Returns the value of the specified number as an `int`.
   * This may involve rounding or truncation.  This implementation simply
   * casts the result of [Rational.toDouble] to `int`.
   *
   * @return the numeric value represented by this object after conversion
   * to type `int`.
   */
  override fun toInt(): Int {
    return toDouble().toInt()
  }

  /**
   * Returns the value of the specified number as a `long`.
   * This may involve rounding or truncation.  This implementation simply
   * casts the result of [Rational.toDouble] to `long`.
   *
   * @return the numeric value represented by this object after conversion
   * to type `long`.
   */
  override fun toLong(): Long {
    return toDouble().toLong()
  }

  /**
   * Returns the value of the specified number as a `short`.
   * This may involve rounding or truncation.  This implementation simply
   * casts the result of [Rational.toDouble] to `short`.
   *
   * @return the numeric value represented by this object after conversion
   * to type `short`.
   */
  override fun toShort(): Short {
    return toDouble().toShort()
  }

  /**
   * Returns the reciprocal value of this object as a new Rational.
   *
   * @return the reciprocal in a new object
   */
  val reciprocal: Rational
    get() = Rational(denominator, numerator)

  /** Checks if this [Rational] number is an Integer, either positive or negative.  */
  val isInteger: Boolean
    get() = denominator == 1L ||
      denominator != 0L && numerator % denominator == 0L ||
      denominator == 0L && numerator == 0L

  /** Checks if either the numerator or denominator are zero.  */
  val isZero: Boolean
    get() = numerator == 0L || denominator == 0L

  /**
   * Returns a string representation of the object of form `numerator/denominator`.
   *
   * @return a string representation of the object.
   */
  override fun toString(): String {
    return "$numerator/$denominator"
  }

  /** Returns the simplest representation of this [Rational]'s value possible.  */
  fun toSimpleString(allowDecimal: Boolean): String {
    return if (denominator == 0L && numerator != 0L) {
      toString()
    } else if (isInteger) {
      toInt().toString()
    } else if (numerator != 1L && denominator % numerator == 0L) { // common factor between denominator and numerator
      val newDenominator = denominator / numerator
      Rational(1, newDenominator).toSimpleString(allowDecimal)
    } else {
      val simplifiedInstance = simplifiedInstance
      if (allowDecimal) {
        val doubleString = simplifiedInstance.toDouble().toString()
        if (doubleString.length < 5) {
          return doubleString
        }
      }
      simplifiedInstance.toString()
    }
  }

  /**
   * Compares two [Rational] instances, returning true if they are mathematically
   * equivalent (in consistence with [Rational.equals] method).
   *
   * @param that the [Rational] to compare this instance to.
   * @return the value `0` if this [Rational] is
   * equal to the argument [Rational] mathematically; a value less
   * than `0` if this [Rational] is less
   * than the argument [Rational]; and a value greater
   * than `0` if this [Rational] is greater than the argument
   * [Rational].
   */
  override fun compareTo(that: Rational): Int {
    return toDouble().compareTo(that.toDouble())
  }

  /**
   * Indicates whether this instance and `other` are numerically equal,
   * even if their representations differ.
   *
   * For example, 1/2 is equal to 10/20 by this method.
   * Similarly, 1/0 is equal to 100/0 by this method.
   * To test equal representations, use EqualsExact.
   *
   * @param other The rational value to compare with
   */
  fun equals(other: Rational): Boolean {
    return other.toDouble() == toDouble()
  }

  /**
   * Indicates whether this instance and `other` have identical
   * Numerator and Denominator.
   *
   *
   * For example, 1/2 is not equal to 10/20 by this method.
   * Similarly, 1/0 is not equal to 100/0 by this method.
   * To test numerically equivalence, use Equals(Rational).
   *
   * @param other The rational value to compare with
   */
  fun equalsExact(other: Rational): Boolean {
    return denominator == other.denominator && numerator == other.numerator
  }

  /**
   * Compares two [Rational] instances, returning true if they are mathematically
   * equivalent.
   *
   * @param obj the [Rational] to compare this instance to.
   * @return true if instances are mathematically equivalent, otherwise false.  Will also
   * return false if `obj` is not an instance of [Rational].
   */
  override fun equals(obj: Any?): Boolean {
    if (obj == null || obj !is Rational) return false
    return toDouble() == obj.toDouble()
  }

  /**
   *
   *
   * Simplifies the representation of this [Rational] number.
   *
   *
   * For example, 5/10 simplifies to 1/2 because both Numerator
   * and Denominator share a common factor of 5.
   *
   *
   * Uses the Euclidean Algorithm to find the greatest common divisor.
   *
   * @return A simplified instance if one exists, otherwise a copy of the original value.
   */
  val simplifiedInstance: Rational
    get() {
      val gcd = GCD(numerator, denominator)
      return Rational(numerator / gcd, denominator / gcd)
    }

  companion object {
    private const val serialVersionUID = 510688928138848770L
    private fun GCD(a: Long, b: Long): Long {
      var a = a
      var b = b
      if (a < 0) a = -a
      if (b < 0) b = -b
      while (a != 0L && b != 0L) {
        if (a > b) a %= b else b %= a
      }
      return if (a == 0L) b else a
    }
  }

}
