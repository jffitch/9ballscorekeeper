package com.mathgeniusguide.nineballscorekeeper.objects

import com.mathgeniusguide.nineballscorekeeper.enums.PlayerTurn

data class ChartShot (
    var isInningStart: Boolean = false,
    var isRackStart: Boolean = false,
    var inning: String = "",
    var rack: String = "",
    var ballsPocketed: String = "",
    var isDefense: Boolean = false,
    var isFoul: Boolean = false,
    var isStalemate: Boolean = false,
    var isTimeOut: Boolean = false,
    var playerTurn: PlayerTurn = PlayerTurn.PLAYER1
)