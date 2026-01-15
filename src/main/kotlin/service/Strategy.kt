package org.example.service

import kotlin.random.Random

enum class StrategyType {
    RANDOM, OPTIMAL;

    companion object {
        fun fromString(type: String): StrategyType {
            return entries.find { it.name.equals(type, ignoreCase = true) } ?: OPTIMAL
        }
    }
}

object StrategyFactory {
    fun createStrategy(type: StrategyType): Strategy {
        return when (type) {
            StrategyType.RANDOM -> RandomStrategy()
            StrategyType.OPTIMAL -> OptimalStrategy()
        }
    }
}

interface Strategy {
    fun calculateMove(heap: Int): Int
}

class RandomStrategy : Strategy {
    override fun calculateMove(heap: Int): Int {
        return Random.nextInt(GameService.MIN_MATCHES, minOf(heap, GameService.MAX_MATCHES) + 1)
    }
}

class OptimalStrategy : Strategy {
    override fun calculateMove(heap: Int): Int {
        val target = 1
        val remainder = (heap - target) % (GameService.MAX_MATCHES + 1)
        return if (remainder == 0) {
            Random.nextInt(GameService.MIN_MATCHES, minOf(heap, GameService.MAX_MATCHES) + 1)
        } else {
            remainder
        }
    }
}
