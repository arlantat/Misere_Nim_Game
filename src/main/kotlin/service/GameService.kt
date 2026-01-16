package org.example.service

import org.example.model.GameState
import org.example.model.GameStatus
import org.example.model.Player
import org.springframework.stereotype.Service

@Service
class GameService {
    companion object {
        const val MIN_MATCHES = 1
        const val MAX_MATCHES = 3
        const val DEFAULT_HEAP_SIZE = 13
    }
    
    private var currentState: GameState? = null
    private var currentStrategy: Strategy = StrategyFactory.createStrategy(StrategyType.OPTIMAL)

    fun startNewGame(initialHeap: Int = DEFAULT_HEAP_SIZE, strategyType: String = "optimal"): GameState {
        if (initialHeap < 1) {
            throw IllegalArgumentException("Initial heap must be at least 1")
        }
        currentStrategy = StrategyFactory.createStrategy(StrategyType.fromString(strategyType))
        val newState = GameState(initialHeap, Player.HUMAN, GameStatus.IN_PROGRESS)
        currentState = newState
        return newState
    }

    fun getGameState(): GameState? = currentState

    fun processTurn(humanMatches: Int): GameState {
        val state = currentState ?: throw IllegalStateException("Game hasn't started")
        
        if (state.status == GameStatus.FINISHED) {
            throw IllegalStateException("Game is already finished")
        }
        if (state.turn != Player.HUMAN) {
            throw IllegalStateException("It's not human's turn")
        }
        if (humanMatches !in MIN_MATCHES..MAX_MATCHES || humanMatches > state.heap) {
            throw IllegalArgumentException("Invalid move: can take $MIN_MATCHES-$MAX_MATCHES matches and not more than current heap")
        }

        var updatedState = applyMove(state, humanMatches)
        
        if (updatedState.status == GameStatus.IN_PROGRESS) {
            val computerMatches = currentStrategy.calculateMove(updatedState.heap)
            updatedState = applyMove(updatedState, computerMatches)
        }
        
        currentState = updatedState
        return updatedState
    }

    private fun applyMove(state: GameState, matches: Int): GameState {
        val newHeap = state.heap - matches
        val winner = if (newHeap == 0) {
            if (state.turn == Player.HUMAN) Player.COMPUTER else Player.HUMAN
        } else null
        
        val status = if (newHeap == 0) GameStatus.FINISHED else GameStatus.IN_PROGRESS
        val nextPlayer = if (state.turn == Player.HUMAN) Player.COMPUTER else Player.HUMAN
        
        return state.copy(
            heap = newHeap,
            turn = nextPlayer,
            status = status,
            winner = winner
        )
    }
}
