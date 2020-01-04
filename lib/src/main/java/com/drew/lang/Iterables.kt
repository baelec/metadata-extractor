package com.drew.lang

import java.util.*

/**
 * @author Drew Noakes https://drewnoakes.com
 */
object Iterables {
  fun <E> toList(iterable: Iterable<E>): List<E> {
    val list = ArrayList<E>()
    for (item in iterable) {
      list.add(item)
    }
    return list
  }

  fun <E> toSet(iterable: Iterable<E>): Set<E> {
    val set = HashSet<E>()
    for (item in iterable) {
      set.add(item)
    }
    return set
  }
}
