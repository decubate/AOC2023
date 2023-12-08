package dec04

import getLines

fun main() {
    val solver = Dec04()
    solver.partOne()
    solver.partTwo()
}

class Dec04 {

    fun partOne() {
        val lines = getLines("dec04")
        val cards = lines.map {
            it.toCard()
        }.fold(0.0) { acc, card -> acc + card.value() }

        println(cards)
    }


    fun partTwo() {
        val lines = getLines("dec04")
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
    }
}

private fun String.toCard(): Dec04.Card {
    val name = Regex("(Card\\s+\\d+:)").find(this)?.value ?: ""
    val stripped = this.replace(Regex("(Card\\s+\\d+:)"), "")
    val parts = stripped.split(" | ")
    val winningNumbers = parts[0].split(" ").filter { it.isNotEmpty() }.map { it.toInt() }
    val numbers = parts[1].split(" ").filter { it.isNotEmpty() }.map { it.toInt() }
    return Dec04.Card(name, winningNumbers, numbers)
}
