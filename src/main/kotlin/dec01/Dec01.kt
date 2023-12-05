package dec01

import java.io.File
import java.io.InputStream

fun main() {
    val solver = Dec01()
    solver.partOne()
    solver.partTwo()
}

class Dec01 {
    fun partOne() {
        val sum = getLines()
            .map { it.toCharArray().filter { it.isDigit() } }
            .map { "${it.first()}${it.last()}" }
            .map { it.toInt() }
            .foldRight(0) { int, acc -> int + acc }

        println(sum)
    }

    fun partTwo() {
        val now = System.currentTimeMillis()
        println(getLines()
            .map { it.getDigits() }
            .map { digits ->
                digits.map {
                    if (it.matches(Regex("\\d"))) {
                        it
                    } else {
                        when (it) {
                            "one", "eno" -> 1
                            "two", "owt" -> 2
                            "three", "eerht" -> 3
                            "four", "ruof" -> 4
                            "five", "evif" -> 5
                            "six", "xis" -> 6
                            "seven", "neves" -> 7
                            "eight", "thgie" -> 8
                            "nine", "enin" -> 9
                            else -> throw IllegalArgumentException(it)
                        }
                    }
                }
            }
            .map { "${it.first()}${it.last()}" }
            .map { it.toInt() }
            .foldRight(0) { int, acc ->
                int + acc
            }
        )
        println(System.currentTimeMillis() - now)
    }

    private fun getLines(): MutableList<String> {
        val file = File("src/main/kotlin/dec01/input.txt")
        println(file.absolutePath)
        val inputStream: InputStream = file.inputStream()
        val lineList = mutableListOf<String>()

        inputStream.bufferedReader().forEachLine { lineList.add(it) }
        return lineList
    }

    private fun String.getDigits(): List<String> {
        val pattern = "one|two|three|four|five|six|seven|eight|nine"
        val regex = Regex("(\\d|$pattern)")
        val reversedRegex = Regex("(\\d|${pattern.reversed()})")
        val first = regex.find(this)?.groupValues?.drop(1)
        val last = reversedRegex.find(this.reversed())?.groupValues?.drop(1)
        val plus: List<String>? = first?.plus(last?.asIterable() ?: listOf())
        return plus ?: listOf()
    }

}