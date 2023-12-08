package dec08

import java.io.File
import java.io.InputStream

fun main() {
    val solver = Dec08()
    solver.partOne()
    solver.partTwo()
}

class Dec08 {

    fun partOne() {
        val lines = getLines()
        val instructions = lines[0].toCharArray()
        val regex = Regex("([A-Z]{3}) = \\(([A-Z]{3}), ([A-Z]{3})\\)")
        val nodes = lines.drop(2).map { line ->
            val groupValues = regex.find(line)!!.groupValues
            Node(groupValues[1], groupValues[2], groupValues[3])
        }.associateBy { it.name }

        val numSteps = distanceTo(nodes["AAA"]!!, { node -> node.name == "ZZZ" }, instructions, nodes)
        println(numSteps)
    }

    private fun distanceTo(
        start: Node,
        predicate: (Node) -> Boolean,
        instructions: CharArray,
        nodes: Map<String, Node>
    ): Long {
        var current = start
        var numSteps = 0L
        while (!predicate(current)) {
            for (c in instructions) {
                if (predicate(current)) {
                    break
                }
                when (c) {
                    'L' -> {
                        current = nodes[current.left]!!
                    }

                    'R' -> {
                        current = nodes[current.right]!!
                    }
                }
                numSteps++
            }
        }
        return numSteps
    }

    fun partTwo() {
        val lines = getLines()
        val instructions = lines[0].toCharArray()
        val regex = Regex("([A-Z]{3}) = \\(([A-Z]{3}), ([A-Z]{3})\\)")
        val nodes = lines.drop(2).map { line ->
            val groupValues = regex.find(line)!!.groupValues
            Node(groupValues[1], groupValues[2], groupValues[3])
        }.associateBy { it.name }

        val start = nodes.keys.filter { it.endsWith("A") }.map { nodes[it] }
        val distances = mutableListOf<Long>()

        for (node in start) {
            distances.add(distanceTo(node!!, { it.name.endsWith("Z") }, instructions, nodes))
        }

        var currentLCM = leastCommonMultiple(distances[0], distances[1])
        for (i in 2..<distances.size) {
            currentLCM = leastCommonMultiple(currentLCM, distances[i])
        }

        println(currentLCM)
    }

    data class Node(val name: String, val left: String, val right: String)

    fun leastCommonMultiple(a: Long, b: Long): Long {
        val larger = if (a > b) a else b
        val maxLcm = a * b
        var lcm = larger
        while (lcm <= maxLcm) {
            if (lcm % a == 0L && lcm % b == 0L) {
                return lcm
            }
            lcm += larger
        }

        return maxLcm
    }

    private fun getLines(): MutableList<String> {
        val file = File("src/main/kotlin/dec08/input.txt")
        println(file.absolutePath)
        val inputStream: InputStream = file.inputStream()
        val lineList = mutableListOf<String>()

        inputStream.bufferedReader().forEachLine { lineList.add(it) }
        return lineList
    }
}