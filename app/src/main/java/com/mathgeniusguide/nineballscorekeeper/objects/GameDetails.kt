package com.mathgeniusguide.nineballscorekeeper.objects

import com.mathgeniusguide.nineballscorekeeper.enums.BallStatus
import com.mathgeniusguide.nineballscorekeeper.enums.PlayerTurn
import com.mathgeniusguide.nineballscorekeeper.enums.ShotCondition
import com.mathgeniusguide.nineballscorekeeper.objects.stats.GameStats
import com.mathgeniusguide.nineballscorekeeper.util.matchPointsArray

class GameDetails(val description: Map<String, String>, val innings: List<Pair<String, String>>) {
    val gameStats = GameStats()
    val player1Name = if (description["Player 1"]?.isNotBlank() == true) description["Player 1"] else "Player 1"
    val player2Name = if (description["Player 2"]?.isNotBlank() == true) description["Player 2"] else "Player 2"
    val pronunciation1 = if (description["Pronunciation 1"]?.isNotBlank() == true) description["Pronunciation 1"] else player1Name
    val pronunciation2 = if (description["Pronunciation 2"]?.isNotBlank() == true) description["Pronunciation 2"] else player2Name
    private val player1GoalString = description["Goal 1"]
    private val player2GoalString = description["Goal 2"]
    val player1Goal = if ("\\d+".toRegex().matches(player1GoalString ?: "")) (player1GoalString ?: "-1").toInt() else -1
    val player2Goal = if ("\\d+".toRegex().matches(player2GoalString ?: "")) (player2GoalString ?: "-1").toInt() else -1
    private val announceStreak1String = description["Announce Streak 1"]
    private val announceStreak2String = description["Announce Streak 2"]
    val announceStreak1 = if ("\\d+".toRegex().matches(announceStreak1String ?: "")) (announceStreak1String ?: "-1").toInt() else -1
    val announceStreak2 = if ("\\d+".toRegex().matches(announceStreak2String ?: "")) (announceStreak2String ?: "-1").toInt() else -1
    var player1MatchPoints = -1
    var player2MatchPoints = -1

    var gameOver = false
    var currentPlayerTurnStreak = 0
    val ballStatus = arrayOf(
        BallStatus.ON_TABLE,
        BallStatus.ON_TABLE,
        BallStatus.ON_TABLE,
        BallStatus.ON_TABLE,
        BallStatus.ON_TABLE,
        BallStatus.ON_TABLE,
        BallStatus.ON_TABLE,
        BallStatus.ON_TABLE,
        BallStatus.SCORED_THIS_TURN
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
        if ((player1Goal != -1 && gameStats.player1Stats.score >= player1Goal) || (player2Goal != -1 && gameStats.player2Stats.score >= player2Goal)) {
            gameOver = true
        }
        if (matchPointsArray[8].contains(player1Goal) && matchPointsArray[8].contains(player2Goal)) {
            val player1Rank = matchPointsArray[8].indexOf(player1Goal)
            val player2Rank = matchPointsArray[8].indexOf(player2Goal)
            player1MatchPoints = matchPointsArray.indexOfFirst { it[player1Rank] > gameStats.player1Stats.score }
            player2MatchPoints = matchPointsArray.indexOfFirst { it[player2Rank] > gameStats.player2Stats.score }
            when {
                player1MatchPoints == -1 && player2MatchPoints == -1 -> {
                    player1MatchPoints = 10
                    player2MatchPoints = 10
                }
                player1MatchPoints == -1 -> player1MatchPoints = 20 - player2MatchPoints
                player2MatchPoints == -1 -> player2MatchPoints = 20 - player1MatchPoints
            }
        }
    }
}