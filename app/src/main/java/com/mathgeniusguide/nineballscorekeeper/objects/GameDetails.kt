package com.mathgeniusguide.nineballscorekeeper.objects

import com.mathgeniusguide.nineballscorekeeper.enums.BallStatus
import com.mathgeniusguide.nineballscorekeeper.enums.PlayerTurn
import com.mathgeniusguide.nineballscorekeeper.enums.ShotCondition
import com.mathgeniusguide.nineballscorekeeper.objects.stats.GameStats

class GameDetails(val description: Map<String, String>, val innings: List<Pair<String, String>>) {
    val gameStats = GameStats()
    val player1Name = if (description["Player 1"]?.isNotBlank() == true) description["Player 1"] else "Player 1"
    val player2Name = if (description["Player 2"]?.isNotBlank() == true) description["Player 2"] else "Player 2"
    var currentPlayerTurnStreak = 0
    val ballStatus = mutableListOf(
        BallStatus.ON_TABLE,
        BallStatus.ON_TABLE,
        BallStatus.ON_TABLE,
        BallStatus.ON_TABLE,
        BallStatus.ON_TABLE,
        BallStatus.ON_TABLE,
        BallStatus.ON_TABLE,
        BallStatus.ON_TABLE,
    )
    val rackList = mutableListOf<Rack>()
    var shotCondition = ShotCondition.BREAK
    init {
        rackList.add(Rack())
        for (pair in innings) {
            if (pair.first == "") {
                break
            }
            val player1Inning = Inning(pair.first, PlayerTurn.PLAYER1, shotCondition, ballStatus, rackList)
            currentPlayerTurnStreak = player1Inning.inningPlayerStats.streaks.currentTurnStreak
            player1Inning.inningPlayerStats.streaks.currentTurnStreak = 0
            gameStats.player1Stats.addPlayerStats(player1Inning.inningPlayerStats)
            if (player1Inning.inningPlayerStats.score != 0) {
                gameStats.player2Stats.streaks.currentStreak = 0
            }
            if (player1Inning.successfulDefense == 1) {
                gameStats.player2Stats.achievements.defense.success++
            }
            if (player1Inning.successfulDefense == -1) {
                gameStats.player2Stats.achievements.defense.failure++
            }
            gameStats.playerTurn = if (player1Inning.isTurnEnd) PlayerTurn.PLAYER2 else PlayerTurn.PLAYER1
            shotCondition = player1Inning.shotCondition
            gameStats.deadBalls += player1Inning.leftOnTable + player1Inning.inningPlayerStats.deadBalls
            if (pair.second == "") {
                break
            }
            val player2Inning = Inning(pair.second, PlayerTurn.PLAYER2, shotCondition, ballStatus, rackList)
            currentPlayerTurnStreak = player2Inning.inningPlayerStats.streaks.currentTurnStreak
            player2Inning.inningPlayerStats.streaks.currentTurnStreak = 0
            gameStats.player2Stats.addPlayerStats(player2Inning.inningPlayerStats)
            if (player2Inning.inningPlayerStats.score != 0) {
                gameStats.player1Stats.streaks.currentStreak = 0
            }
            if (player2Inning.successfulDefense == 1) {
                gameStats.player1Stats.achievements.defense.success++
            }
            if (player2Inning.successfulDefense == -1) {
                gameStats.player1Stats.achievements.defense.failure++
            }
            gameStats.playerTurn = if (player2Inning.isTurnEnd) PlayerTurn.PLAYER1 else PlayerTurn.PLAYER2
            shotCondition = player2Inning.shotCondition
            gameStats.deadBalls += player2Inning.leftOnTable + player2Inning.inningPlayerStats.deadBalls
            if(player2Inning.isTurnEnd) {
                gameStats.innings++
                rackList.last().innings++
            }
        }
        for (i in rackList.indices) {
            for (j in i until rackList.size) {
                rackList[j].index++
                rackList[j].player1Total += rackList[i].player1
                rackList[j].player2Total += rackList[i].player2
                rackList[j].deadBallsTotal += rackList[i].deadBalls
                rackList[j].inningsTotal += rackList[i].innings
            }
        }
        when (gameStats.playerTurn) {
            PlayerTurn.PLAYER1 -> gameStats.player1Stats.streaks
            PlayerTurn.PLAYER2 -> gameStats.player2Stats.streaks
        }.currentTurnStreak = currentPlayerTurnStreak
    }
}