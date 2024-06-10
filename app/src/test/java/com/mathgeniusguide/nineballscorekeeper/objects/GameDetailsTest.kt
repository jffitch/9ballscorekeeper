package com.mathgeniusguide.nineballscorekeeper.objects

import com.mathgeniusguide.nineballscorekeeper.enums.BallStatus
import com.mathgeniusguide.nineballscorekeeper.enums.PlayerTurn
import com.mathgeniusguide.nineballscorekeeper.enums.ShotCondition
import com.mathgeniusguide.nineballscorekeeper.gameJustStarted
import com.mathgeniusguide.nineballscorekeeper.gameStatsMatch
import com.mathgeniusguide.nineballscorekeeper.gameString1
import com.mathgeniusguide.nineballscorekeeper.gameString2
import com.mathgeniusguide.nineballscorekeeper.gameString3
import com.mathgeniusguide.nineballscorekeeper.listsMatch
import com.mathgeniusguide.nineballscorekeeper.objects.stats.GameStats
import com.mathgeniusguide.nineballscorekeeper.util.getGameDetails
import org.junit.Before
import org.junit.Test

class GameDetailsTest {
    private var expectedGameStats = GameStats()

    @Before
    fun before() {
        expectedGameStats = GameStats()
    }

    @Test
    fun testGameString1() {
        val gameDetails = getGameDetails(gameString1)
        expectedGameStats.deadBalls = 12
        expectedGameStats.playerTurn = PlayerTurn.PLAYER1
        expectedGameStats.innings = 13
        with (expectedGameStats.player1Stats) {
            score = 47
            deadBalls = 1
            shotsTaken = 50
            streaks.streak = 17
            streaks.turnStreak = 17
            streaks.currentStreak = 5
            streaks.currentTurnStreak = 5
            fouls.wrongBallFirst = 1
            achievements.pointsOnBreak[1] = 3
            achievements.pointsOnBreak[2] = 2
            achievements.pointsOnBreak[3] = 1
            achievements.intentionalEclipse = 3
            achievements.defense.total = 4
            achievements.defense.success = 2
            achievements.defense.failure = 2
            achievements.eclipse = 3
            achievements.eclipseEscape = 1
            achievements.selfEclipse = 3
            achievements.selfEclipseEscape = 2
            achievements.selfEclipsePocket = 1
            achievements.nineOnBreak = 1
            achievements.breakAndRun = 1
            achievements.perfectRack = 2
            achievements.earlyNine = 1
            achievements.nines = 6
        }
        with (expectedGameStats.player2Stats) {
            score = 11
            deadBalls = 2
            shotsTaken = 23
            streaks.streak = 4
            streaks.turnStreak = 3
            fouls.wrongBallFirst = 1
            fouls.miss = 1
            fouls.scratch = 2
            achievements.pointsOnBreak[1] = 1
            achievements.eclipse = 1
            achievements.eclipseEscape = 1
            achievements.eclipsePocket = 1
            achievements.ballInHandReturn = 1
            achievements.nines = 1
        }
        assert(gameDetails.currentPlayerTurnStreak == 5)
        assert(gameDetails.shotCondition == ShotCondition.BREAK)
        assert(listsMatch(gameDetails.ballStatus.toList(), listOf(
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.SCORED_THIS_TURN
        )))
        assert(gameStatsMatch(gameDetails.gameStats, expectedGameStats))
    }

    @Test
    fun testGameString2() {
        val gameDetails = getGameDetails(gameString2)
        expectedGameStats.deadBalls = 20
        expectedGameStats.playerTurn = PlayerTurn.PLAYER2
        expectedGameStats.innings = 16
        with (expectedGameStats.player1Stats) {
            score = 36
            shotsTaken = 44
            streaks.streak = 12
            streaks.turnStreak = 10
            fouls.wrongBallFirst = 1
            achievements.pointsOnBreak[0] = 1
            achievements.pointsOnBreak[1] = 1
            achievements.pointsOnBreak[2] = 2
            achievements.pointsOnBreak[3] = 2
            achievements.defense.total = 2
            achievements.defense.failure = 1
            achievements.eclipse = 1
            achievements.selfEclipse = 1
            achievements.nineOnBreak = 2
            achievements.nines = 5
        }
        with (expectedGameStats.player2Stats) {
            score = 16
            deadBalls = 1
            shotsTaken = 29
            streaks.streak = 7
            streaks.turnStreak = 5
            streaks.currentStreak = 5
            streaks.currentTurnStreak = 5
            fouls.wrongBallFirst = 2
            fouls.miss = 1
            fouls.scratch = 2
            achievements.pointsOnBreak[1] = 1
            achievements.pointsOnBreak[2] = 1
            achievements.selfEclipse = 1
            achievements.illegalNine = 1
            achievements.earlyNine = 1
            achievements.nines = 2
        }
        assert(gameDetails.currentPlayerTurnStreak == 5)
        assert(gameDetails.shotCondition == ShotCondition.NORMAL)
        assert(listsMatch(gameDetails.ballStatus.toList(), listOf(
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.ON_TABLE,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.SCORED_THIS_TURN
        )))
        assert(gameStatsMatch(gameDetails.gameStats, expectedGameStats))
    }

    @Test
    fun testGameString3() {
        val gameDetails = getGameDetails(gameString3)
        expectedGameStats.deadBalls = 8
        expectedGameStats.playerTurn = PlayerTurn.PLAYER2
        expectedGameStats.innings = 18
        with (expectedGameStats.player1Stats) {
            score = 48
            shotsTaken = 58
            streaks.streak = 18
            streaks.turnStreak = 9
            streaks.currentStreak = 8
            achievements.pointsOnBreak[0] = 1
            achievements.pointsOnBreak[1] = 4
            achievements.pointsOnBreak[2] = 1
            achievements.pointsOnBreak[3] = 1
            achievements.defense.total = 5
            achievements.defense.success = 3
            achievements.defense.failure = 1
            achievements.eclipse = 4
            achievements.intentionalEclipse = 3
            achievements.eclipseEscape = 1
            achievements.eclipseReturn = 1
            achievements.selfEclipse = 1
            achievements.selfEclipseEscape = 1
            achievements.selfEclipsePocket = 1
            achievements.earlyNine = 1
            achievements.nines = 6
        }
        with (expectedGameStats.player2Stats) {
            score = 5
            deadBalls = 3
            shotsTaken = 23
            streaks.streak = 2
            streaks.turnStreak = 2
            fouls.wrongBallFirst = 4
            fouls.scratch = 2
            achievements.eclipse = 1
        }
        assert(gameDetails.currentPlayerTurnStreak == 0)
        assert(gameDetails.shotCondition == ShotCondition.NORMAL)
        assert(listsMatch(gameDetails.ballStatus.toList(), listOf(
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.PLAYER1,
            BallStatus.ON_TABLE,
            BallStatus.PLAYER1
        )))
        assert(gameStatsMatch(gameDetails.gameStats, expectedGameStats))
    }

    @Test
    fun testGameJustStarted() {
        val gameDetails = getGameDetails(gameJustStarted)
        expectedGameStats.playerTurn = PlayerTurn.PLAYER1
        assert(gameDetails.currentPlayerTurnStreak == 0)
        assert(gameDetails.shotCondition == ShotCondition.BREAK)
        assert(listsMatch(gameDetails.ballStatus.toList(), listOf(
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.SCORED_THIS_TURN
        )))
        assert(gameStatsMatch(gameDetails.gameStats, expectedGameStats))
    }
}