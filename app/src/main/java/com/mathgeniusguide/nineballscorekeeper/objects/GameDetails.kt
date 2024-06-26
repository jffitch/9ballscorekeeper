package com.mathgeniusguide.nineballscorekeeper.objects

import com.mathgeniusguide.nineballscorekeeper.enums.BallStatus
import com.mathgeniusguide.nineballscorekeeper.enums.DescriptionKey
import com.mathgeniusguide.nineballscorekeeper.enums.PlayerTurn
import com.mathgeniusguide.nineballscorekeeper.enums.ShotCondition
import com.mathgeniusguide.nineballscorekeeper.objects.stats.GameStats
import com.mathgeniusguide.nineballscorekeeper.util.matchPointsArray

class GameDetails(val description: Map<String, String>, val innings: List<Pair<String, String>>) {
    val gameStats = GameStats()
    val location = getDescription(DescriptionKey.LOCATION)
    val date = getDescription(DescriptionKey.DATE)
    val player1Name = getDescription(DescriptionKey.PLAYER_1, "Player 1")
    val player2Name = getDescription(DescriptionKey.PLAYER_2, "Player 2")
    val pronunciation1 = getDescription(DescriptionKey.PRONUNCIATION_1, player1Name)
    val pronunciation2 = getDescription(DescriptionKey.PRONUNCIATION_2, player2Name)
    val team1 = getDescription(DescriptionKey.TEAM_1, "")
    val team2 = getDescription(DescriptionKey.TEAM_2, "")
    val tournamentId = getDescription(DescriptionKey.TOURNAMENT_ID, "")
    private val player1GoalString = getDescription(DescriptionKey.GOAL_1)
    private val player2GoalString = getDescription(DescriptionKey.GOAL_2)
    val player1Goal = if ("\\d+".toRegex().matches(player1GoalString ?: "")) (player1GoalString ?: "-1").toInt() else -1
    val player2Goal = if ("\\d+".toRegex().matches(player2GoalString ?: "")) (player2GoalString ?: "-1").toInt() else -1
    private val announceStreak1String = getDescription(DescriptionKey.ANNOUNCE_STREAK_1)
    private val announceStreak2String = getDescription(DescriptionKey.ANNOUNCE_STREAK_2)
    val announceStreak1 = if ("\\d+".toRegex().matches(announceStreak1String ?: "")) (announceStreak1String ?: "-1").toInt() else -1
    val announceStreak2 = if ("\\d+".toRegex().matches(announceStreak2String ?: "")) (announceStreak2String ?: "-1").toInt() else -1
    var player1MatchPoints = -1
    var player2MatchPoints = -1

    val startInnings = getDescription(DescriptionKey.START_INNINGS, "0")?.toInt() ?: 0
    val startRacks = getDescription(DescriptionKey.START_RACKS, "0")?.toInt() ?: 0
    val startScore1 = getDescription(DescriptionKey.START_SCORE_1, "0")?.toInt() ?: 0
    val startScore2 = getDescription(DescriptionKey.START_SCORE_2, "0")?.toInt() ?: 0
    val startNineBreak1 = getDescription(DescriptionKey.START_NINE_BREAK_1, "0")?.toInt() ?: 0
    val startNineBreak2 = getDescription(DescriptionKey.START_NINE_BREAK_2, "0")?.toInt() ?: 0
    val startBreakRun1 = getDescription(DescriptionKey.START_BREAK_RUN_1, "0")?.toInt() ?: 0
    val startBreakRun2 = getDescription(DescriptionKey.START_BREAK_RUN_2, "0")?.toInt() ?: 0
    val startDefense1 = getDescription(DescriptionKey.START_DEFENSE_1, "0")?.toInt() ?: 0
    val startDefense2 = getDescription(DescriptionKey.START_DEFENSE_2, "0")?.toInt() ?: 0
    val startDeadBalls = startRacks * 10 - startScore1 - startScore2

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
        gameStats.deadBalls = startDeadBalls
        gameStats.innings = startInnings
        gameStats.player1Stats.score = startScore1
        gameStats.player1Stats.achievements.defense.total = startDefense1
        gameStats.player1Stats.achievements.nineOnBreak = startNineBreak1
        gameStats.player1Stats.achievements.breakAndRun = startBreakRun1
        gameStats.player2Stats.score = startScore2
        gameStats.player2Stats.achievements.defense.total = startDefense2
        gameStats.player2Stats.achievements.nineOnBreak = startNineBreak2
        gameStats.player2Stats.achievements.breakAndRun = startBreakRun2
        rackList.add(Rack())
        with (rackList[0]) {
            this.player1Total = startScore1
            this.player2Total = startScore2
            this.deadBallsTotal = startDeadBalls
            this.inningsTotal = startInnings
        }
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
        if (isValidApaGoals()) {
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

    fun getDescription(key: DescriptionKey, default: String? = null): String? {
        if (default == null) {
            return description[key.text]
        }
        return if (description[key.text]?.isNotBlank() == true) description[key.text] else default
    }

    fun isValidApaGoals(): Boolean {
        return matchPointsArray[8].contains(player1Goal) && matchPointsArray[8].contains(player2Goal)
    }
}