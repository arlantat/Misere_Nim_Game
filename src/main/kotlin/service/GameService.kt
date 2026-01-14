package org.example.service

import org.example.model.GameState
import org.example.model.GameStatus
import org.example.model.Player
import org.springframework.stereotype.Service
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
        // In Misere Nim with one heap of size N, where you can take 1-3 matches.
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
            // so we just take 1 and hope for the best.
            1
        } else {
            remainder
        }
    }
}

@Service
class GameService {
    private var currentState: GameState = GameState(0, Player.HUMAN, GameStatus.FINISHED)
    private var strategy: Strategy = OptimalStrategy()

    fun startNewGame(initialHeap: Int, strategyType: String = "optimal"): GameState {
        strategy = if (strategyType.lowercase() == "random") {
            RandomStrategy()
        } else {
            OptimalStrategy()
        }
        currentState = GameState(initialHeap, Player.HUMAN, GameStatus.IN_PROGRESS)
        return currentState
    }

    fun getGameState(): GameState = currentState

    fun makeHumanMove(matches: Int): GameState {
        if (currentState.status == GameStatus.FINISHED) {
            throw IllegalStateException("Game is already finished")
        }
        if (currentState.turn != Player.HUMAN) {
            throw IllegalStateException("It's not human's turn")
        }
        if (matches < 1 || matches > 3 || matches > currentState.heap) {
            throw IllegalArgumentException("Invalid move: can take 1-3 matches and not more than current heap")
        }

        currentState = applyMove(currentState, matches)
        
        if (currentState.status == GameStatus.IN_PROGRESS) {
            // Automatically trigger computer move if game is not over
            makeComputerMove()
        }
        
        return currentState
    }

    private fun makeComputerMove(): GameState {
        val matches = strategy.calculateMove(currentState.heap)
        currentState = applyMove(currentState, matches)
        return currentState
    }

    private fun applyMove(state: GameState, matches: Int): GameState {
        val newHeap = state.heap - matches
        if (newHeap == 0) {
            // Player who took the last match loses
            val winner = if (state.turn == Player.HUMAN) Player.COMPUTER else Player.HUMAN
            return state.copy(heap = 0, status = GameStatus.FINISHED, winner = winner)
        }
        val nextPlayer = if (state.turn == Player.HUMAN) Player.COMPUTER else Player.HUMAN
        return state.copy(heap = newHeap, turn = nextPlayer)
    }
}
