package com.drew.metadata.photoshop

import java.util.*

/**
 * Represents a subpath created by Photoshop:
 *
 *  * Closed Bezier knot, linked
 *  * Closed Bezier knot, unlinked
 *  * Open Bezier knot, linked
 *  * Open Bezier knot, unlinked
 *
 *
 * @author Payton Garland
 */
class Subpath @JvmOverloads constructor(val type: String = "") {
  private val _knots = ArrayList<Knot>()
  /**
   * Appends a knot (set of 3 points) into the list
   */
  fun add(knot: Knot) {
    _knots.add(knot)
  }

  /**
   * Gets size of knots list
   *
   * @return size of knots ArrayList
   */
  fun size(): Int {
    return _knots.size
  }

  val knots: Iterable<Knot>
    get() = _knots

}
