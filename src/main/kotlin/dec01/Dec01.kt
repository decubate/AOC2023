package dec01

import getLines

fun main() {
    val solver = Dec01()
    solver.partOne()
    solver.partTwo()
}

class Dec01 {
    fun partOne() {
        val sum = getLines("dec01")
            .map { it.toCharArray().filter { it.isDigit() } }
            .map { "${it.first()}${it.last()}" }
            .map { it.toInt() }
            .foldRight(0) { int, acc -> int + acc }

        println(sum)
    }

    fun partTwo() {
        println(getLines("dec01")
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