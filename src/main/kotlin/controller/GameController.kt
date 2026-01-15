package org.example.controller

import org.example.model.GameState
import org.example.service.GameService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/game")
class GameController(private val gameService: GameService) {

    @PostMapping("/start")
    fun startGame(
        @RequestParam(defaultValue = "${GameService.DEFAULT_HEAP_SIZE}") initialHeap: Int,
        @RequestParam(defaultValue = "optimal") strategy: String
    ): GameState {
        return gameService.startNewGame(initialHeap, strategy)
    }

    @PostMapping("/move")
    fun makeMove(@RequestParam matches: Int): GameState {
        return gameService.processTurn(matches)
    }

    @GetMapping("/state")
    fun getGameState(): GameState {
        return gameService.getGameState() ?: throw IllegalStateException("No game in progress")
    }
        
    @ExceptionHandler(IllegalStateException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleIllegalState(e: IllegalStateException): Map<String, String> {
        return mapOf("error" to (e.message ?: "Illegal state"))
    }

    @ExceptionHandler(IllegalArgumentException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleIllegalArgument(e: IllegalArgumentException): Map<String, String> {
        return mapOf("error" to (e.message ?: "Invalid argument"))
    }
}
