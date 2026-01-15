package org.example.service

import org.example.model.GameState
import org.example.model.GameStatus
import org.example.model.Player
import org.springframework.stereotype.Service

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
        if (matches !in 1..3 || matches > currentState.heap) {
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
