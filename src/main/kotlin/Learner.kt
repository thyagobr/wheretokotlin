package com.whereto

// Challenge 1

//class Duo<T>(
//    val first: T,
//    val second: T,
//) {
//    fun <R> map(mapper: (T) -> R): Duo<R> {
//        val newFirst: R = mapper(first)
//        val newSecond: R = mapper(second)
//        return Duo(
//            newFirst,
//            newSecond
//        )
//    }
//}
//
//fun main() {
//    val duo = Duo(2, 3)
//
//    val mapped = duo.map { it * 10 }
//
//    println(mapped.first)  // 20
//    println(mapped.second) // 30
//}

// Challenge 2

//fun <T: Comparable<T>> customMaxOf(
//    first: T,
//    second: T
//): T {
//    var max = first
//    when(first.compareTo(second)) {
//        in listOf(0, 1) -> max = first
//        -1 -> max = second
//    }
//    return max
//}
//
//fun main() {
//    println(customMaxOf(3, 7))           // 7
//    println(customMaxOf(3.5, 2.1))       // 3.5
//    println(customMaxOf("apple", "zoo")) // zoo
//}

// Challenge 3

fun <T : Comparable<T>> maxInList(list: List<out T>): T? {
    if (list.isEmpty()) return null

    return list.maxOf { it }
}

fun main() {
    println(maxInList(listOf(1, 5, 3)))           // 5
    println(maxInList(listOf(3.5, 2.1, 7.7)))     // 7.7
    println(maxInList(listOf("apple", "zoo")))    // zoo
    println(maxInList(emptyList<Int>()))          // null
}