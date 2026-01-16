package org.example.service

import org.example.model.GameStatus
import org.example.model.Player
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class GameServiceTest {
    private val gameService = GameService()

    @Test
    fun `test startNewGame happy path`() {
        val state = gameService.startNewGame(10)
        assertEquals(10, state.heap)
        assertEquals(Player.HUMAN, state.turn)
        assertEquals(GameStatus.IN_PROGRESS, state.status)
    }

    @Test
    fun `test startNewGame with invalid inputs`() {
        assertFailsWith<IllegalArgumentException> {
            gameService.startNewGame(0)
        }
    }

    @Test
    fun `test processTurn happy path`() {
        gameService.startNewGame(13)
        val state = gameService.processTurn(3)
        
        assertEquals(9, state.heap)
        assertEquals(Player.HUMAN, state.turn)
    }

    @Test
    fun `test processTurn with invalid inputs`() {
        gameService.startNewGame(13)

        // Too small
        assertFailsWith<IllegalArgumentException> {
            gameService.processTurn(0)
        }

        // Too large
        assertFailsWith<IllegalArgumentException> {
            gameService.processTurn(4)
        }

        // Exceeds heap
        gameService.startNewGame(2)
        assertFailsWith<IllegalArgumentException> {
            gameService.processTurn(3)
        }
    }

    @Test
    fun `test getGameState happy path`() {
        gameService.startNewGame()
        val state = gameService.getGameState()!!

        assertEquals(13, state.heap)
        assertEquals(Player.HUMAN, state.turn)
    }

    @Test
    fun `test getGameState when not started`() {
        assertNull(gameService.getGameState())
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
