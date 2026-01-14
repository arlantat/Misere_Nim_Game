package org.example.model

enum class Player {
    HUMAN, COMPUTER
}

enum class GameStatus {
    IN_PROGRESS, FINISHED
}

data class GameState(
    val heap: Int,
    val turn: Player,
    val status: GameStatus,
    val winner: Player? = null
)
