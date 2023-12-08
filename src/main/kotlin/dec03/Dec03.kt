package dec03

import getLines

fun main() {
    val solver = Dec03()
    solver.partOne()
    solver.partTwo()
}

class Dec03 {
    fun partOne() {
        val lines = getLines("dec03")
        val matrix: List<CharArray> = lines.map { it.toCharArray() }
        val partNumbers = getPartNumbers(lines)
            .filter { it.hasSymbolNeighbor(matrix) }
            .fold(0) { acc, part -> acc + part.value }
        println(partNumbers)
    }

    private fun getPartNumbers(lines: MutableList<String>) = lines
        .flatMap { line ->
            line
                .splitToNumbers()
                .filter {
                    try {
                        it.value.toInt()
                        true
                    } catch (e: NumberFormatException) {
                        false
                    }
                }
                .map {
                    PartNumber(
                        value = it.value.toInt(),
                        row = lines.indexOf(line),
                        startIndex = it.startIndex,
                        endIndex = it.endIndex
                    )
                }
        }.also { println("${it.size} parts") }

    fun partTwo() {
        val lines = getLines("dec03")

        val partNumbers = getPartNumbers(lines)
        println(partNumbers)
        val sum = lines.splitToGears().also { println("${it.size} gears") }.map { it.getGearProduct(partNumbers) }.fold(0) { acc, gear -> acc + gear }
        println(sum)
    }

    data class Gear(val row: Int, val column: Int) {
        fun getGearProduct(partNumbers: List<PartNumber>): Int {
            val neighbors = mutableSetOf<PartNumber>()
            for (r in row-1..row+1) {
                for (c in column - 1 .. column + 1) {
                    val elements =
                        partNumbers.filter { it.contains(r, c) }
                    neighbors.addAll(elements)
                }
            }
            return if (neighbors.size == 2) neighbors.fold(1) { acc, neighbor -> acc * neighbor.value } else 0
        }
    }

    data class PartNumber(val value: Int, val startIndex: Int, val endIndex: Int, val row: Int) {
        fun hasSymbolNeighbor(matrix: List<CharArray>): Boolean {
            return matrix.hasSymbol(row, startIndex - 1) || matrix.hasSymbol(
                row,
                endIndex
            ) || hasSymbolAbove(matrix) || hasSymbolBelow(matrix)
        }


        private fun hasSymbolAbove(matrix: List<CharArray>): Boolean {
            for (i in startIndex - 1..endIndex) {
                if (matrix.hasSymbol(row - 1, i)) {
                    return true
                }
            }
            return false
        }

        private fun hasSymbolBelow(matrix: List<CharArray>): Boolean {
            for (i in startIndex - 1..endIndex) {
                if (matrix.hasSymbol(row + 1, i)) {
                    return true
                }
            }
            return false
        }

        private fun List<CharArray>.hasSymbol(row: Int, column: Int): Boolean {
            return try {
                val c = this[row][column]
                val b = !c.isDigit() && c != '.'
                b
            } catch (e: IndexOutOfBoundsException) {
                false
            }
        }

        fun contains(row: Int, column: Int): Boolean {
            return this.row == row && startIndex <= column && endIndex > column
        }
    }

    data class StringWithIndices(val value: String, val startIndex: Int, val endIndex: Int)

}

private fun String.splitToNumbers(): List<Dec03.StringWithIndices> {
    val ret = mutableListOf<Dec03.StringWithIndices>()
    Regex("\\d+").findAll(this).iterator().forEach {
        ret.add(Dec03.StringWithIndices(it.value, it.range.start, it.range.endInclusive + 1))
    }

    return ret
}

private fun List<String>.splitToGears(): List<Dec03.Gear> {
    val ret = mutableListOf<Dec03.Gear>()
    this.forEach { line ->

        Regex("\\*").findAll(line).iterator().forEach {
            ret.add(Dec03.Gear(this.indexOf(line), it.range.first))
        }
    }

    return ret
}