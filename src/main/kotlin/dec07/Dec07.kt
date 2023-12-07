package dec07

import java.io.File
import java.io.InputStream

fun main() {
    val solver = Dec07()
    solver.partOne()
    solver.partTwo()
}

class Dec07 {

    fun partOne() {
        val value = getLines().map {
            val parts = it.split(" ")
            Hand(parts[0].toCharArray().asList(), parts[1].toLong())
        }.sortedWith(HandSorter(false)).mapIndexed { index, hand ->
            hand.bid * (index + 1)
        }.fold(0L) { acc, value ->
            acc + value
        }

        println("part 1: $value")
    }

    fun partTwo() {
        val value = getLines().map {
            val parts = it.split(" ")
            Hand(parts[0].toCharArray().asList(), parts[1].toLong())
        }.sortedWith(HandSorter(jokerRule = true)).mapIndexed { index, hand ->
            hand.bid * (index + 1)
        }.fold(0L) { acc, value ->
            acc + value
        }

        println("part 2: $value")
    }

    class Hand(val cards: List<Char>, val bid: Long) {
        val type: Type by lazy {
            val distinct = cards.distinct()
            if (distinct.size == 1) {
                Type.FIVE_OF_A_KIND
            } else if (distinct.size == 2) {
                if (cards.filter { it == distinct.first() }.size == 4 || cards.filter { it == distinct.last() }.size == 4) {
                    Type.FOUR_OF_A_KIND
                } else {
                    Type.FULL_HOUSE
                }
            } else if (distinct.size == 3) {
                if (cards.filter { it == distinct.first() }.size == 3 || cards.filter { it == distinct.last() }.size == 3 || cards.filter { it == distinct[1] }.size == 3) {
                    Type.THREE_OF_A_KIND
                } else {
                    Type.TWO_PAIR
                }
            } else if (distinct.size == 4) {
                Type.ONE_PAIR
            } else {
                Type.HIGH_CARD
            }
        }

        val typeWithJokers: Type by lazy {
            val distinct = cards.distinct()
            if (!cards.contains('J')) {
                type
            } else {
                val numJokers = cards.filter { it == 'J' }.size
                if (distinct.size == 2 || distinct.size == 1) {
                    Type.FIVE_OF_A_KIND
                } else if (numJokers == 3 && distinct.size == 3) {
                    Type.FOUR_OF_A_KIND
                } else if (numJokers == 2 && distinct.size == 3) { // JJXXY
                    Type.FOUR_OF_A_KIND
                } else if (numJokers == 2 && distinct.size == 4) { // JJXYZ
                    Type.THREE_OF_A_KIND
                } else if (numJokers == 1) {
                    if (distinct.size == 3) {
                        val firstSet = cards.filter { it == distinct[0] }
                        val secondSet = cards.filter { it == distinct[1] }
                        val thirdSet = cards.filter { it == distinct[2] }
                        if (firstSet.size == 3 || secondSet.size == 3 || thirdSet.size == 3) { // JXXXY
                            Type.FOUR_OF_A_KIND
                        } else { // JXXYY
                            Type.FULL_HOUSE
                        }
                    } else if (distinct.size == 4) { // JXXYZ
                        Type.THREE_OF_A_KIND
                    } else {
                        Type.ONE_PAIR
                    }
                } else {
                    Type.ONE_PAIR
                }
            }
        }
    }

    class HandSorter(val jokerRule: Boolean) : Comparator<Hand> {
        override fun compare(o1: Hand, o2: Hand): Int {

            if ((if (jokerRule) o1.typeWithJokers else o1.type) != (if (jokerRule) o2.typeWithJokers else o2.type)) {
                return Type.values().indexOf((if (jokerRule) o2.typeWithJokers else o2.type)) - Type.values().indexOf((if (jokerRule) o1.typeWithJokers else o1.type))
            } else {
                for (i in 0..<5) {
                    val compareCards = compareCards(o1.cards[i], o2.cards[i], jokers = jokerRule)
                    if (compareCards < 0) {
                        return -1
                    } else if (compareCards > 0) {
                        return 1
                    }
                }
                return 0
            }
        }

        fun compareCards(o1: Char, o2: Char, jokers: Boolean): Int { // negative if o1 < o2
            if (o1 == o2) {
                return 0
            }
            if (jokers) {
                if (o1 == 'J') {
                    return -1
                }
                if (o2 == 'J') {
                    return 1
                }
            }
            if (o1.isDigit() && o2.isDigit()) {
                return o1.digitToInt() - o2.digitToInt()
            }
            if (o1.isDigit()) {
                return -1
            }
            if (o2.isDigit()) {
                return 1
            }
            val i = when (o1) {
                'A' -> 1
                'K' -> if (o2 == 'A') -1 else 1
                'Q' -> if (o2 == 'A' || o2 == 'K') -1 else 1
                'J' -> if (o2 == 'A' || o2 == 'K' || o2 == 'Q') -1 else 1
                'T' -> if (o2 == 'A' || o2 == 'K' || o2 == 'Q' || o2 == 'J') -1 else 1
                else -> 0
            }
            return i

        }


    }

    enum class Type {
        FIVE_OF_A_KIND,
        FOUR_OF_A_KIND,
        FULL_HOUSE,
        THREE_OF_A_KIND,
        TWO_PAIR,
        ONE_PAIR,
        HIGH_CARD
    }

    private fun getLines(): MutableList<String> {
        val file = File("src/main/kotlin/dec07/input.txt")
        println(file.absolutePath)
        val inputStream: InputStream = file.inputStream()
        val lineList = mutableListOf<String>()

        inputStream.bufferedReader().forEachLine { lineList.add(it) }
        return lineList
    }
}