package com.mathgeniusguide.nineballscorekeeper.objects.stats

import com.mathgeniusguide.nineballscorekeeper.enums.PlayerTurn

class GameStats {
    var innings = 0
    val player1Stats = PlayerStats()
    val player2Stats = PlayerStats()
    var playerTurn = PlayerTurn.PLAYER1
    var deadBalls = 0
}