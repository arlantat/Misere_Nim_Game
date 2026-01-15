package org.example.model

data class GameState(
    val heap: Int,
    val turn: Player,
    val status: GameStatus,
    val winner: Player? = null
)
