package org.example.service

import org.example.model.GameStatus
import org.example.model.Player
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GameServiceTest {
    private val gameService = GameService()

    @Test
    fun `test start new game`() {
        val state = gameService.startNewGame(10)
        assertEquals(10, state.heap)
        assertEquals(Player.HUMAN, state.turn)
        assertEquals(GameStatus.IN_PROGRESS, state.status)
    }

    @Test
    fun `test human move and computer response`() {
        gameService.startNewGame(13)
        val state = gameService.processTurn(3)
        
        assertEquals(9, state.heap)
        assertEquals(Player.HUMAN, state.turn)
    }

    @Test
    fun `test winning condition - human wins`() {
        gameService.startNewGame(2)
        val state = gameService.processTurn(1)

        assertEquals(0, state.heap)
        assertEquals(GameStatus.FINISHED, state.status)
        assertEquals(Player.HUMAN, state.winner)
    }

    @Test
    fun `test winning condition - computer wins`() {
        gameService.startNewGame(1)
        val state = gameService.processTurn(1)
        assertEquals(0, state.heap)
        assertEquals(GameStatus.FINISHED, state.status)
        assertEquals(Player.COMPUTER, state.winner)
    }
}
