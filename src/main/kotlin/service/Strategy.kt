package org.example.service

import kotlin.random.Random

interface Strategy {
    fun calculateMove(heap: Int): Int
}

class RandomStrategy : Strategy {
    override fun calculateMove(heap: Int): Int {
        return Random.nextInt(1, minOf(heap, 3) + 1)
    }
}

class OptimalStrategy : Strategy {
    override fun calculateMove(heap: Int): Int {
        // In Misère Nim with one heap of size N, where you can take 1-3 matches.
        // The goal is to NOT take the last match.
        // This is equivalent to Normal Nim where the goal is to take the last match,
        // but with a slight twist at the end.
        // For a single heap, you want to leave your opponent with 4k + 1 matches.
        // If you leave them with 1, they must take it and lose.
        // If you leave them with 5, whatever they take (1, 2, or 3), you can then leave them with 1.
        
        val target = 1
        val remainder = (heap - target) % 4
        return if (remainder == 0) {
            // We are in a losing position if the opponent plays optimally, 
            // so we play randomly and hope for the best.
            Random.nextInt(1, minOf(heap, 3) + 1)
        } else {
            remainder
        }
    }
}
