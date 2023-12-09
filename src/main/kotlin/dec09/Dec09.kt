package dec09

import getLines

fun main() {
    val solver = Dec09()
    solver.partOne()
    solver.partTwo()
}

class Dec09 {

    fun partOne() {
        val lines = getLines("dec09")
        val numbers: List<List<Long>> = lines.map {
            it.split(" ").map { it.toLong() }
        }

        println(numbers.sumOf { extrapolateForwards(it) })
    }

    fun partTwo() {
        val lines = getLines("dec09")
        val numbers: List<List<Long>> = lines.map {
            it.split(" ").map { it.toLong() }
        }

        println(numbers.sumOf { extrapolateBackwards(it) })
    }

    fun extrapolateForwards(longs: List<Long>): Long {
        var diffs = longs
        val lists = mutableListOf<MutableList<Long>>()
        lists.add(longs.toMutableList())
        while (!diffs.all { it == 0L }) {
            diffs = diffs.windowed(2, 1).map {
                it.last() - it.first()
            }
            lists.add(diffs.toMutableList())
        }

        val reversed = lists.reversed()
        for (i in reversed.indices) {
            if (i == 0) {
                reversed[i].addLast(0)
            } else {
                reversed[i].addLast(reversed[i - 1].last() + reversed[i].last())
            }
        }

        return reversed.last().last()
    }

    fun extrapolateBackwards(longs: List<Long>): Long {
        var diffs = longs
        val lists = mutableListOf<MutableList<Long>>()
        lists.add(longs.toMutableList())
        while (!diffs.all { it == 0L }) {
            diffs = diffs.windowed(2, 1).map {
                it.last() - it.first()
            }
            lists.add(diffs.toMutableList())
        }

        val reversed = lists.reversed()
        for (i in reversed.indices) {
            if (i == 0) {
                reversed[i].add(0, 0)
            } else {
                reversed[i].add(0, reversed[i].first() - reversed[i - 1].first())
            }
        }

        return reversed.last().first()
    }
}

private fun <E> MutableList<E>.addLast(thing: E) {
    add(size, thing)
}
