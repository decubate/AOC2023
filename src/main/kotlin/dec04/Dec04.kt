package dec04

import java.io.File
import java.io.InputStream

fun main() {
    val solver = Dec04()
    solver.partOne()
    solver.partTwo()
}

class Dec04 {

    fun partOne() {
        val lines = getLines()
        val cards = lines.map {
            it.toCard()
        }.fold(0.0) { acc, card -> acc + card.value() }

        println(cards)
    }


    fun partTwo() {
        val lines = getLines()
        val allCards = mutableListOf<Card>()

        val originalCards = lines.map {
            it.toCard()
        }

        originalCards.forEach { card ->
            val numCard = allCards.filter { it.name == card.name }.size
            for (i in 0..numCard) {
                allCards.addAll(card.getSubList(originalCards, originalCards.indexOf(card)))
            }
        }

        println(allCards.size + originalCards.size)
    }

    data class Card(val name: String, val winningNumbers: List<Int>, val numbers: List<Int>) {
        fun value(): Double {
            var numWins = 0
            numbers.forEach {
                if (winningNumbers.contains(it)) {
                    numWins++
                }
            }
            val pow = if (numWins == 0) 0.0 else Math.pow(2.0, (numWins - 1).toDouble())
            println("$this is worth $pow")
            return pow
        }

        val numWinningNumbers: Int
            get() {
                var numWins = 0
                numbers.forEach {
                    if (winningNumbers.contains(it)) {
                        numWins++
                    }
                }
                return numWins
            }

        fun getSubList(originalCards: List<Card>, index: Int): List<Card> {
            val numWinningNumbers = numWinningNumbers
            if (numWinningNumbers == 0) {
                return listOf()
            }
            val subList = originalCards.subList(index + 1, Math.min(index + numWinningNumbers + 1, originalCards.size))
            return subList
        }

        fun allCards(originalCards: List<Card>, index: Int): List<Card> {
            val numWinningNumbers = numWinningNumbers
            if (numWinningNumbers == 0) {
                return listOf(this)
            }
            val subList = getSubList(originalCards, index)
            println("sublist for ${this.name} ${subList.map { it.name }}")

            val flatMap = subList.flatMap {
                it.allCards(originalCards, originalCards.indexOf(it))
            }
            println("final sublist for ${this.name} ${flatMap.map { it.name }}")
            return flatMap
        }
    }

    private fun getLines(): MutableList<String> {
        val file = File("src/main/kotlin/dec04/input.txt")
        println(file.absolutePath)
        val inputStream: InputStream = file.inputStream()
        val lineList = mutableListOf<String>()

        inputStream.bufferedReader().forEachLine { lineList.add(it) }
        return lineList
    }
}

private fun String.toCard(): Dec04.Card {
    println("parsing $this")
    val name = Regex("(Card\\s+\\d+:)").find(this)?.value ?: ""
    val stripped = this.replace(Regex("(Card\\s+\\d+:)"), "")
    val parts = stripped.split(" | ")
    val winningNumbers = parts[0].split(" ").filter { it.isNotEmpty() }.map { it.toInt() }
    val numbers = parts[1].split(" ").filter { it.isNotEmpty() }.map { it.toInt() }
    return Dec04.Card(name, winningNumbers, numbers)
}
