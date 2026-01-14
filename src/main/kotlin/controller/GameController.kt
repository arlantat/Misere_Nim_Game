package org.example.controller

import org.example.model.GameState
import org.example.service.GameService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/game")
class GameController(private val gameService: GameService) {

    @PostMapping("/start")
    fun startGame(
        @RequestParam(defaultValue = "13") initialHeap: Int,
        @RequestParam(defaultValue = "optimal") strategy: String
    ): GameState {
        return gameService.startNewGame(initialHeap, strategy)
    }

    @PostMapping("/move")
    fun makeMove(@RequestParam matches: Int): GameState {
        return gameService.makeHumanMove(matches)
    }

    @get:GetMapping("/state")
    val gameState: GameState
        get() = gameService.getGameState()
        
    @ExceptionHandler(IllegalStateException::class)
    fun handleIllegalState(e: IllegalStateException): Map<String, String> {
        return mapOf("error" to (e.message ?: "Illegal state"))
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(e: IllegalArgumentException): Map<String, String> {
        return mapOf("error" to (e.message ?: "Invalid argument"))
    }
}
