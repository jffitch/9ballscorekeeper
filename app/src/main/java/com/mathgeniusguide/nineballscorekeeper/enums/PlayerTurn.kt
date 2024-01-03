package com.mathgeniusguide.nineballscorekeeper.enums

enum class PlayerTurn {
    PLAYER1,
    PLAYER2;

    fun toBallStatus(): BallStatus {
        return when(this) {
            PLAYER1 -> BallStatus.PLAYER1
            PLAYER2 -> BallStatus.PLAYER2
        }
    }
}