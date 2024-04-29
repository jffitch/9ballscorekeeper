package com.mathgeniusguide.nineballscorekeeper.objects

import com.mathgeniusguide.nineballscorekeeper.enums.PlayerTurn

data class ChartShot (
    var inning: String = "",
    var rack: String = "",
    var ballsPocketed: String = "",
    var isFoul: Boolean = false,
    var isStalemate: Boolean = false,
    var isTimeOut: Boolean = false,
    var playerTurn: PlayerTurn = PlayerTurn.PLAYER1
)