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
        // Human takes 3, heap becomes 10.
        // Computer (optimal) wants to leave human with 4k + 1.
        // 10 matches, computer should take matches such that 10 - X = 9 or 5 or 1.
        // So computer should take 1 to leave 9.
        val state = gameService.makeHumanMove(3)
        
        // After human move (3), heap was 10, human turn ended.
        // After computer move (1), heap should be 9, human turn starts again.
        assertEquals(9, state.heap)
        assertEquals(Player.HUMAN, state.turn)
    }

    @Test
    fun `test winning condition - human loses`() {
        // Start with 2 matches.
        gameService.startNewGame(2)
        // Human takes 1, heap becomes 1.
        // Computer takes 1, heap becomes 0.
        // Computer took the last match, so human wins? No, misere: last match loses.
        // Wait, if heap is 1 and it's computer's turn, computer takes it and loses.
        
        val state = gameService.makeHumanMove(1)
        // Heap was 2. Human took 1 -> Heap 1.
        // Computer MUST take 1 -> Heap 0.
        // Computer took last match -> Computer LOSES -> Human WINS.
        assertEquals(0, state.heap)
        assertEquals(GameStatus.FINISHED, state.status)
        assertEquals(Player.HUMAN, state.winner)
    }

    @Test
    fun `test winning condition - computer wins`() {
        gameService.startNewGame(1)
        // Human takes 1, heap becomes 0.
        // Human took last match -> Human LOSES -> Computer WINS.
        val state = gameService.makeHumanMove(1)
        assertEquals(0, state.heap)
        assertEquals(GameStatus.FINISHED, state.status)
        assertEquals(Player.COMPUTER, state.winner)
    }

    @Test
    fun `test optimal strategy`() {
        val strategy = OptimalStrategy()
        // Target: leave opponent with 1, 5, 9, 13... (4k + 1)
        assertEquals(1, strategy.calculateMove(2)) // 2-1 = 1
        assertEquals(2, strategy.calculateMove(3)) // 3-2 = 1
        assertEquals(3, strategy.calculateMove(4)) // 4-3 = 1
        
        // Losing positions (remainder 0) should return random move 1..3
        val move5 = strategy.calculateMove(5)
        assertTrue(move5 in 1..3)
        
        assertEquals(1, strategy.calculateMove(6)) // 6-1 = 5
        assertEquals(2, strategy.calculateMove(7)) // 7-2 = 5
        assertEquals(3, strategy.calculateMove(8)) // 8-3 = 5
        
        val move9 = strategy.calculateMove(9)
        assertTrue(move9 in 1..3)
    }
}
