package com.mathgeniusguide.nineballscorekeeper.objects

import com.mathgeniusguide.nineballscorekeeper.enums.BallStatus
import com.mathgeniusguide.nineballscorekeeper.enums.PlayerTurn
import com.mathgeniusguide.nineballscorekeeper.enums.ShotCondition
import com.mathgeniusguide.nineballscorekeeper.objects.stats.PlayerStats

class Inning(string: String, playerTurn: PlayerTurn, startCondition: ShotCondition, ballStatus: MutableList<BallStatus>, rackList: MutableList<Rack>) {
    private val shots = string.substringBeforeLast('\'').split("'")
    val inningPlayerStats = PlayerStats()
    var shotCondition = startCondition
    var successfulDefense = 0
    var leftOnTable = 0
    var isTurnEnd = false
    init {
        for (shotString in shots) {
            val shot = Shot(shotString, playerTurn, shotCondition, ballStatus)
            inningPlayerStats.addPlayerStats(shot.shotPlayerStats)
            shotCondition = shot.shotCondition
            if (successfulDefense == 0) {
                successfulDefense = shot.successfulDefense
            }
            val currentRack = rackList.last()
            currentRack.player1 = ballStatus.count { it == BallStatus.PLAYER1 || (playerTurn == PlayerTurn.PLAYER1 && it == BallStatus.SCORED_THIS_TURN)}
            currentRack.player2 = ballStatus.count { it == BallStatus.PLAYER2 || (playerTurn == PlayerTurn.PLAYER2 && it == BallStatus.SCORED_THIS_TURN)}
            currentRack.deadBalls = ballStatus.count { it == BallStatus.DEAD }
            if (shot.isRackEnd) {
                with (ballStatus.count { it == BallStatus.ON_TABLE }) {
                    leftOnTable += this
                    currentRack.deadBalls += this
                }

                when {
                    shot.isStalemate -> currentRack.deadBalls += 2
                    playerTurn == PlayerTurn.PLAYER1 -> currentRack.player1 += 2
                    playerTurn == PlayerTurn.PLAYER2 -> currentRack.player2 += 2
                }
                rackList.add(Rack())
                ballStatus.replaceAll { BallStatus.ON_TABLE }
            }
            if (shot.isTurnEnd) {
                inningPlayerStats.streaks.currentTurnStreak = 0
                ballStatus.replaceAll { if (it == BallStatus.SCORED_THIS_TURN) playerTurn.toBallStatus() else it }
                isTurnEnd = true
                break
            }
        }
    }
}