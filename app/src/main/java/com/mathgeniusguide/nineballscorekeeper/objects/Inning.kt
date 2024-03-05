package com.mathgeniusguide.nineballscorekeeper.objects

import com.mathgeniusguide.nineballscorekeeper.enums.BallStatus
import com.mathgeniusguide.nineballscorekeeper.enums.PlayerTurn
import com.mathgeniusguide.nineballscorekeeper.enums.ShotCondition
import com.mathgeniusguide.nineballscorekeeper.objects.stats.PlayerStats

class Inning(string: String, playerTurn: PlayerTurn, startCondition: ShotCondition, ballStatus: Array<BallStatus>, rackList: MutableList<Rack>) {
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
            currentRack.player1 = ballStatus.dropLast(1).count { it == BallStatus.PLAYER1 || (playerTurn == PlayerTurn.PLAYER1 && it == BallStatus.SCORED_THIS_TURN)}
            currentRack.player2 = ballStatus.dropLast(1).count { it == BallStatus.PLAYER2 || (playerTurn == PlayerTurn.PLAYER2 && it == BallStatus.SCORED_THIS_TURN)}
            currentRack.deadBalls = ballStatus.count { it == BallStatus.DEAD }
            if (playerTurn == PlayerTurn.PLAYER1) {
                currentRack.player1TimeOuts += shot.shotPlayerStats.achievements.timeOut
            }
            if (playerTurn == PlayerTurn.PLAYER2) {
                currentRack.player2TimeOuts += shot.shotPlayerStats.achievements.timeOut
            }
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
                ballStatus.fill(BallStatus.ON_TABLE)
                ballStatus[8] = BallStatus.SCORED_THIS_TURN
            }
            if (shot.isTurnEnd) {
                inningPlayerStats.streaks.currentTurnStreak = 0
                for (i in 0..8) {
                    if (ballStatus[i] == BallStatus.SCORED_THIS_TURN) {
                        ballStatus[i] = playerTurn.toBallStatus()
                    }
                }
                isTurnEnd = true
                break
            }
        }
    }
}