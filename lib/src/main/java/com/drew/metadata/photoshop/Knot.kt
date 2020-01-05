package com.drew.metadata.photoshop

/**
 * Represents a knot created by Photoshop:
 *
 *
 *  * Linked knot
 *  * Unlinked knot
 *
 *
 * @author Payton Garland
 */
class Knot(val type: String) {
  private val _points = DoubleArray(6)
  /**
   * Get the type of knot (linked or unlinked)
   *
   * @return the type of knot
   */
  /**
   * Add an individual coordinate value (x or y) to
   * points array (6 points per knot)
   *
   * @param index location of point to be added in points
   * @param point coordinate value to be added to points
   */
  fun setPoint(index: Int, point: Double) {
    _points[index] = point
  }

  /**
   * Get an individual coordinate value (x or y)
   *
   * @return an individual coordinate value
   */
  fun getPoint(index: Int): Double {
    return _points[index]
  }
}
