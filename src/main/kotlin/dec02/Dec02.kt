package dec02

import getLines

fun main() {
    val solver = Dec02()
    solver.partOne()
    solver.partTwo()
}

class Dec02 {

    fun partOne() {
        val lines = getLines("dec02")
        println(getPossibleGames(lines).sum())
    }

    fun partTwo() {
        val lines = getLines("dec02")
        println(getMaxGames(lines).map {
            val valueForColor = it.value
            println(valueForColor)
            valueForColor.values.fold(1) { acc, item -> acc * item }
        }
            .sum())
    }

    private fun getPossibleGames(lines: List<String>): List<Int> {
        val map = getMaxGames(lines)
        return map.filter { it.value.isPossibleGame() }.map { it.key }
    }

    private fun getMaxGames(lines: List<String>): MutableMap<Int, Map<String, Int>> {
        val map = mutableMapOf<Int, Map<String, Int>>()
        for ((index, s) in lines.withIndex()) {
            map[index + 1] = getMaxValues(s.replace(Regex("(Game \\d+: )"), ""))
        }
        return map
    }

    private fun getMaxValues(game: String): Map<String, Int> {
        println("getMaxValues from $game")

        val showings = game.split(";")
        var maxBlue = 0
        var maxRed = 0
        var maxGreen = 0
        showings.forEach {
            val blue = Regex("((\\d+) blue)").find(it)?.groupValues?.get(2)?.toInt() ?: 0
            val red = Regex("((\\d+) red)").find(it)?.groupValues?.get(2)?.toInt() ?: 0
            val green = Regex("((\\d+) green)").find(it)?.groupValues?.get(2)?.toInt() ?: 0
            if (blue > maxBlue) {
                maxBlue = blue
            }
            if (red > maxRed) {
                maxRed = red
            }
            if (green > maxGreen) {
                maxGreen = green
            }
        }

        return mapOf("blue" to maxBlue, "red" to maxRed, "green" to maxGreen).also { println(it) }
    }
}

private fun Map<String, Int>.isPossibleGame(): Boolean {
    val b = this["blue"]!! <= 14
    return b && this["red"]!! <= 12 && this["green"]!! <= 13
}
