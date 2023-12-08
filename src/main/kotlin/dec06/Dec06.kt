package dec06

import getLines

fun main() {
    val solver = Dec06()
    solver.partOne()
    solver.partTwo()
}

class Dec06 {

    fun partOne() {
        val lines = getLines("dec06")
        val races = getRaces(lines)
        println(races.fold(1) { acc, race -> acc * race.getNumWaysToWin() })
    }

    private fun getRaces(lines: MutableList<String>): MutableList<Race> {
        val races = mutableListOf<Race>()
        val times = lines[0].split(Regex("\\s+")).drop(1).map { it.toLong() }
        val distances = lines[1].split(Regex("\\s+")).drop(1).map { it.toLong() }

        for (i in times.indices) {
            races.add(Race(times[i], distances[i]))
        }
        return races
    }


    fun partTwo() {
        val lines = getLines("dec06")
        val time = lines[0].split(Regex("\\s+")).drop(1).fold("") { acc, part -> acc + part }.toLong()
        val distance = lines[1].split(Regex("\\s+")).drop(1).fold("") { acc, part -> acc + part }.toLong()
        val race = Race(time, distance)
        println(race.getNumWaysToWin())
    }

    data class Race(val time: Long, val record: Long) {
        fun getNumWaysToWin(): Int {
            var numWays = 0
            for (i in 0..time) {
                if (i * (time - i) > record) {
                    numWays++
                }
            }
            return numWays
        }
    }
}