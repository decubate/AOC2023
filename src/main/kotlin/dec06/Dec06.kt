package dec06

import java.io.File
import java.io.InputStream

fun main() {
    val solver = Dec06()
    solver.partOne()
    solver.partTwo()
}

class Dec06 {

    fun partOne() {
        val lines = getLines()
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
        val lines = getLines()
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

    private fun getLines(): MutableList<String> {
        val file = File("src/main/kotlin/dec06/input.txt")
        println(file.absolutePath)
        val inputStream: InputStream = file.inputStream()
        val lineList = mutableListOf<String>()

        inputStream.bufferedReader().forEachLine { lineList.add(it) }
        return lineList
    }
}