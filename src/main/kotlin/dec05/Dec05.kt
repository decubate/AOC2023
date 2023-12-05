package dec05

import java.io.File
import java.io.InputStream

fun main() {
    val solver = Dec05()
    solver.partOne()
    solver.partTwo()
}

class Dec05 {

    fun partOne() {
        val lines = getLines()
        val seeds = mutableListOf<Long>()
        val maps = mutableMapOf<GardeningType, List<Range>>()
        var type: GardeningType? = null
        lines.forEach {
            if (it.startsWith("seeds: ")) {
                it.replace("seeds: ", "").split(" ").map { it.toLong() }.forEach {
                    seeds.add(it)
                }
            } else if (it.isEmpty()) {
                type = null
            } else if (it == "seed-to-soil map:") {
                type = GardeningType.SEED_TO_SOIL
                maps[GardeningType.SEED_TO_SOIL] = mutableListOf()
            } else if (it == "soil-to-fertilizer map:") {
                type = GardeningType.SOIL_TO_FERTILIZER
                maps[GardeningType.SOIL_TO_FERTILIZER] = mutableListOf()
            } else if (it == "fertilizer-to-water map:") {
                type = GardeningType.FERTILIZER_TO_WATER
                maps[GardeningType.FERTILIZER_TO_WATER] = mutableListOf()
            } else if (it == "water-to-light map:") {
                type = GardeningType.WATER_TO_LIGHT
                maps[GardeningType.WATER_TO_LIGHT] = mutableListOf()
            } else if (it == "light-to-temperature map:") {
                type = GardeningType.LIGHT_TO_TEMP
                maps[GardeningType.LIGHT_TO_TEMP] = mutableListOf()
            } else if (it == "temperature-to-humidity map:") {
                type = GardeningType.TEMP_TO_HUMIDITY
                maps[GardeningType.TEMP_TO_HUMIDITY] = mutableListOf()
            } else if (it == "humidity-to-location map:") {
                type = GardeningType.HUMIDITY_TO_LOCATION
                maps[GardeningType.HUMIDITY_TO_LOCATION] = mutableListOf()
            } else {
                val elements = it.split(" ").map { it.toLong() }
                maps[type!!] = maps[type!!]!!.plus(Range(elements[0], elements[1], elements[2]))
            }
        }

        val locations = sortedSetOf<Long>()

        seeds.forEach {
            var currentValue = it
            GardeningType.values().forEach {
                currentValue = maps[it]!!.getDestinationValue(currentValue)
            }
            locations.add(currentValue)
        }

        println(locations.first())
    }


    fun partTwo() {
        val lines = getLines()
        val seeds = mutableListOf<LongRange>()
        val maps = mutableMapOf<GardeningType, List<Range>>()
        var type: GardeningType? = null
        lines.forEach {
            if (it.startsWith("seeds: ")) {
                it.replace("seeds: ", "").split(" ").map { it.toLong() }.windowed(2, 2).forEach {
                    seeds.add(LongRange(it[0], it[0] + it[1] - 1))
                }
                println("parsed ${seeds.fold(0) { acc: Long, range: LongRange -> acc + range.count() }} seeds") // 1_994_747_387
            } else if (it.isEmpty()) {
                type = null
            } else if (it == "seed-to-soil map:") {
                type = GardeningType.SEED_TO_SOIL
                maps[GardeningType.SEED_TO_SOIL] = mutableListOf()
            } else if (it == "soil-to-fertilizer map:") {
                type = GardeningType.SOIL_TO_FERTILIZER
                maps[GardeningType.SOIL_TO_FERTILIZER] = mutableListOf()
            } else if (it == "fertilizer-to-water map:") {
                type = GardeningType.FERTILIZER_TO_WATER
                maps[GardeningType.FERTILIZER_TO_WATER] = mutableListOf()
            } else if (it == "water-to-light map:") {
                type = GardeningType.WATER_TO_LIGHT
                maps[GardeningType.WATER_TO_LIGHT] = mutableListOf()
            } else if (it == "light-to-temperature map:") {
                type = GardeningType.LIGHT_TO_TEMP
                maps[GardeningType.LIGHT_TO_TEMP] = mutableListOf()
            } else if (it == "temperature-to-humidity map:") {
                type = GardeningType.TEMP_TO_HUMIDITY
                maps[GardeningType.TEMP_TO_HUMIDITY] = mutableListOf()
            } else if (it == "humidity-to-location map:") {
                type = GardeningType.HUMIDITY_TO_LOCATION
                maps[GardeningType.HUMIDITY_TO_LOCATION] = mutableListOf()
            } else {
                val elements = it.split(" ").map { it.toLong() }
                maps[type!!] = maps[type!!]!!.plus(Range(elements[0], elements[1], elements[2]))
            }
        }

        var minLocation = Long.MAX_VALUE

        var numProcessed = 0L
        seeds.forEach {
            it.forEach {
                var currentValue = it
                GardeningType.values().forEach {
                    val destinationValue = maps[it]!!.getDestinationValue(currentValue)
                    currentValue = destinationValue
                }
                if (currentValue < minLocation) {
                    minLocation = currentValue
                }
                numProcessed++
                if (numProcessed % 10_000_000L == 0L) {
                    println("processed $numProcessed seeds")
                }
            }
        }

        println(minLocation)
    }

    fun List<Range>.getDestinationValue(source: Long): Long {
        this.forEach {
            val translatedValue = it.getTranslatedValue(source)
            if (translatedValue != -1L) {
                return translatedValue
            }
        }
        return source
    }

    enum class GardeningType {
        SEED_TO_SOIL,
        SOIL_TO_FERTILIZER,
        FERTILIZER_TO_WATER,
        WATER_TO_LIGHT,
        LIGHT_TO_TEMP,
        TEMP_TO_HUMIDITY,
        HUMIDITY_TO_LOCATION,

    }

    data class Range(val destinationStart: Long, val sourceStart: Long, val length: Long) {
        fun getTranslatedValue(number: Long): Long {
            return if (number >= sourceStart && number <= sourceStart + length - 1) {
                val offset = number - sourceStart
                return (destinationStart + offset)
            } else {
                -1
            }
        }
    }

    private fun getLines(): MutableList<String> {
        val file = File("src/main/kotlin/dec05/input.txt")
        println(file.absolutePath)
        val inputStream: InputStream = file.inputStream()
        val lineList = mutableListOf<String>()

        inputStream.bufferedReader().forEachLine { lineList.add(it) }
        return lineList
    }
}