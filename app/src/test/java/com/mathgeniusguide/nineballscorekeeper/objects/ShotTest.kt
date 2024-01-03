package com.mathgeniusguide.nineballscorekeeper.objects

import com.mathgeniusguide.nineballscorekeeper.almostBreakAndRunBallStatusNineLeft
import com.mathgeniusguide.nineballscorekeeper.almostBreakAndRunBallStatusOneBallLeft
import com.mathgeniusguide.nineballscorekeeper.onBallStatus
import com.mathgeniusguide.nineballscorekeeper.customStartingBallStatus
import com.mathgeniusguide.nineballscorekeeper.enums.BallStatus
import com.mathgeniusguide.nineballscorekeeper.enums.PlayerTurn
import com.mathgeniusguide.nineballscorekeeper.enums.ShotCondition
import com.mathgeniusguide.nineballscorekeeper.listsMatch
import com.mathgeniusguide.nineballscorekeeper.nineNotPerfect
import com.mathgeniusguide.nineballscorekeeper.ninePerfect
import com.mathgeniusguide.nineballscorekeeper.objects.stats.PlayerStats
import com.mathgeniusguide.nineballscorekeeper.playerStatsMatch
import org.junit.Before
import org.junit.Test

class ShotTest {
    private var expectedPlayerStats = PlayerStats()

    @Before
    fun before() {
        expectedPlayerStats = PlayerStats()
        expectedPlayerStats.shotsTaken = 1
    }

    @Test
    fun testOnePocketedBall() {
        val string = "4"
        val startCondition = ShotCondition.NORMAL
        val ballStatus = customStartingBallStatus.toMutableList()
        val shot = Shot(string, PlayerTurn.PLAYER1, startCondition, ballStatus)
        setScore(1)
        assert(playerStatsMatch(shot.shotPlayerStats, expectedPlayerStats))
        assert(shot.successfulDefense == 0)
        assert(!shot.isTurnEnd)
        assert(shot.shotCondition == ShotCondition.NORMAL)
        assert(listsMatch(ballStatus, mutableListOf(
            BallStatus.PLAYER1,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.DEAD,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.ON_TABLE,
            BallStatus.DEAD,
            BallStatus.ON_TABLE,
            BallStatus.PLAYER1
        )))
    }

    @Test
    fun testTwoPocketedBalls() {
        val string = "47"
        val startCondition = ShotCondition.NORMAL
        val ballStatus = customStartingBallStatus.toMutableList()
        val shot = Shot(string, PlayerTurn.PLAYER1, startCondition, ballStatus)
        setScore(2)
        assert(playerStatsMatch(shot.shotPlayerStats, expectedPlayerStats))
        assert(shot.successfulDefense == 0)
        assert(!shot.isTurnEnd)
        assert(shot.shotCondition == ShotCondition.NORMAL)
        assert(listsMatch(ballStatus, mutableListOf(
            BallStatus.PLAYER1,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.DEAD,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.ON_TABLE,
            BallStatus.DEAD,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.PLAYER1
        )))
    }

    @Test
    fun testSomeAlreadyPocketedBalls() {
        val string = "246"
        val startCondition = ShotCondition.NORMAL
        val ballStatus = customStartingBallStatus.toMutableList()
        val shot = Shot(string, PlayerTurn.PLAYER1, startCondition, ballStatus)
        setScore(1)
        assert(playerStatsMatch(shot.shotPlayerStats, expectedPlayerStats))
        assert(shot.successfulDefense == 0)
        assert(!shot.isTurnEnd)
        assert(shot.shotCondition == ShotCondition.NORMAL)
        assert(listsMatch(ballStatus, mutableListOf(
            BallStatus.PLAYER1,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.DEAD,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.ON_TABLE,
            BallStatus.DEAD,
            BallStatus.ON_TABLE,
            BallStatus.PLAYER1
        )))
    }

    @Test
    fun testFoul() {
        val string = "5W"
        val startCondition = ShotCondition.NORMAL
        val ballStatus = customStartingBallStatus.toMutableList()
        val shot = Shot(string, PlayerTurn.PLAYER1, startCondition, ballStatus)
        expectedPlayerStats.deadBalls = 1
        expectedPlayerStats.fouls.wrongBallFirst = 1
        assert(playerStatsMatch(shot.shotPlayerStats, expectedPlayerStats))
        assert(shot.successfulDefense == 0)
        assert(shot.isTurnEnd)
        assert(shot.shotCondition == ShotCondition.BALL_IN_HAND)
        assert(listsMatch(ballStatus, mutableListOf(
            BallStatus.PLAYER1,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.DEAD,
            BallStatus.ON_TABLE,
            BallStatus.DEAD,
            BallStatus.DEAD,
            BallStatus.ON_TABLE,
            BallStatus.PLAYER1
        )))
    }

    @Test
    fun testDefense() {
        val string = "d"
        val startCondition = ShotCondition.NORMAL
        val ballStatus = customStartingBallStatus.toMutableList()
        val shot = Shot(string, PlayerTurn.PLAYER1, startCondition, ballStatus)
        expectedPlayerStats.achievements.defense.total = 1
        assert(playerStatsMatch(shot.shotPlayerStats, expectedPlayerStats))
        assert(shot.successfulDefense == 0)
        assert(shot.isTurnEnd)
        assert(shot.shotCondition == ShotCondition.DEFENSE)
        assert(listsMatch(ballStatus, mutableListOf(
            BallStatus.PLAYER1,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.DEAD,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.DEAD,
            BallStatus.ON_TABLE,
            BallStatus.PLAYER1
        )))
    }

    @Test
    fun testEclipse() {
        val string = "e"
        val startCondition = ShotCondition.NORMAL
        val ballStatus = customStartingBallStatus.toMutableList()
        val shot = Shot(string, PlayerTurn.PLAYER1, startCondition, ballStatus)
        expectedPlayerStats.achievements.eclipse = 1
        assert(playerStatsMatch(shot.shotPlayerStats, expectedPlayerStats))
        assert(shot.successfulDefense == 0)
        assert(shot.isTurnEnd)
        assert(shot.shotCondition == ShotCondition.ECLIPSE)
        assert(listsMatch(ballStatus, mutableListOf(
            BallStatus.PLAYER1,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.DEAD,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.DEAD,
            BallStatus.ON_TABLE,
            BallStatus.PLAYER1
        )))
    }

    @Test
    fun testIntentionalEclipse() {
        val string = "de"
        val startCondition = ShotCondition.NORMAL
        val ballStatus = customStartingBallStatus.toMutableList()
        val shot = Shot(string, PlayerTurn.PLAYER1, startCondition, ballStatus)
        expectedPlayerStats.achievements.eclipse = 1
        expectedPlayerStats.achievements.intentionalEclipse = 1
        expectedPlayerStats.achievements.defense.total = 1
        assert(playerStatsMatch(shot.shotPlayerStats, expectedPlayerStats))
        assert(shot.successfulDefense == 0)
        assert(shot.isTurnEnd)
        assert(shot.shotCondition == ShotCondition.INTENTIONAL_ECLIPSE)
        assert(listsMatch(ballStatus, mutableListOf(
            BallStatus.PLAYER1,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.DEAD,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.DEAD,
            BallStatus.ON_TABLE,
            BallStatus.PLAYER1
        )))
    }

    @Test
    fun testPocketOnSelfDefense() {
        val string = "4d"
        val startCondition = ShotCondition.NORMAL
        val ballStatus = customStartingBallStatus.toMutableList()
        val shot = Shot(string, PlayerTurn.PLAYER1, startCondition, ballStatus)
        setScore(1)
        expectedPlayerStats.achievements.defense.total = 1
        expectedPlayerStats.achievements.defense.failure = 1
        assert(playerStatsMatch(shot.shotPlayerStats, expectedPlayerStats))
        assert(shot.successfulDefense == 0)
        assert(!shot.isTurnEnd)
        assert(shot.shotCondition == ShotCondition.NORMAL)
        assert(listsMatch(ballStatus, mutableListOf(
            BallStatus.PLAYER1,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.DEAD,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.ON_TABLE,
            BallStatus.DEAD,
            BallStatus.ON_TABLE,
            BallStatus.PLAYER1
        )))
    }

    @Test
    fun testSelfEclipse() {
        val string = "4e"
        val startCondition = ShotCondition.NORMAL
        val ballStatus = customStartingBallStatus.toMutableList()
        val shot = Shot(string, PlayerTurn.PLAYER1, startCondition, ballStatus)
        setScore(1)
        expectedPlayerStats.achievements.selfEclipse = 1
        assert(playerStatsMatch(shot.shotPlayerStats, expectedPlayerStats))
        assert(shot.successfulDefense == 0)
        assert(!shot.isTurnEnd)
        assert(shot.shotCondition == ShotCondition.SELF_ECLIPSE)
        assert(listsMatch(ballStatus, mutableListOf(
            BallStatus.PLAYER1,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.DEAD,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.ON_TABLE,
            BallStatus.DEAD,
            BallStatus.ON_TABLE,
            BallStatus.PLAYER1
        )))
    }

    @Test
    fun testFoulAfterEclipse() {
        val string = "4W"
        val startCondition = ShotCondition.ECLIPSE
        val ballStatus = customStartingBallStatus.toMutableList()
        val shot = Shot(string, PlayerTurn.PLAYER1, startCondition, ballStatus)
        expectedPlayerStats.deadBalls = 1
        expectedPlayerStats.fouls.wrongBallFirst = 1
        assert(playerStatsMatch(shot.shotPlayerStats, expectedPlayerStats))
        assert(shot.successfulDefense == 0)
        assert(shot.isTurnEnd)
        assert(shot.shotCondition == ShotCondition.BALL_IN_HAND)
        assert(listsMatch(ballStatus, mutableListOf(
            BallStatus.PLAYER1,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.DEAD,
            BallStatus.DEAD,
            BallStatus.ON_TABLE,
            BallStatus.DEAD,
            BallStatus.ON_TABLE,
            BallStatus.PLAYER1
        )))
    }

    @Test
    fun testFoulAfterIntentionalEclipse() {
        val string = "4W"
        val startCondition = ShotCondition.INTENTIONAL_ECLIPSE
        val ballStatus = customStartingBallStatus.toMutableList()
        val shot = Shot(string, PlayerTurn.PLAYER1, startCondition, ballStatus)
        expectedPlayerStats.deadBalls = 1
        expectedPlayerStats.fouls.wrongBallFirst = 1
        assert(playerStatsMatch(shot.shotPlayerStats, expectedPlayerStats))
        assert(shot.successfulDefense == 1)
        assert(shot.isTurnEnd)
        assert(shot.shotCondition == ShotCondition.BALL_IN_HAND)
        assert(listsMatch(ballStatus, mutableListOf(
            BallStatus.PLAYER1,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.DEAD,
            BallStatus.DEAD,
            BallStatus.ON_TABLE,
            BallStatus.DEAD,
            BallStatus.ON_TABLE,
            BallStatus.PLAYER1
        )))
    }

    @Test
    fun testMissAfterIntentionalEclipse() {
        val string = ""
        val startCondition = ShotCondition.INTENTIONAL_ECLIPSE
        val ballStatus = customStartingBallStatus.toMutableList()
        val shot = Shot(string, PlayerTurn.PLAYER1, startCondition, ballStatus)
        expectedPlayerStats.achievements.eclipseEscape = 1
        assert(playerStatsMatch(shot.shotPlayerStats, expectedPlayerStats))
        assert(shot.successfulDefense == 0)
        assert(shot.isTurnEnd)
        assert(shot.shotCondition == ShotCondition.NORMAL)
        assert(listsMatch(ballStatus, mutableListOf(
            BallStatus.PLAYER1,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.DEAD,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.DEAD,
            BallStatus.ON_TABLE,
            BallStatus.PLAYER1
        )))
    }

    @Test
    fun testPocketAfterIntentionalEclipse() {
        val string = "4"
        val startCondition = ShotCondition.INTENTIONAL_ECLIPSE
        val ballStatus = customStartingBallStatus.toMutableList()
        val shot = Shot(string, PlayerTurn.PLAYER1, startCondition, ballStatus)
        expectedPlayerStats.achievements.eclipseEscape = 1
        expectedPlayerStats.achievements.eclipsePocket = 1
        setScore(1)
        assert(playerStatsMatch(shot.shotPlayerStats, expectedPlayerStats))
        assert(shot.successfulDefense == -1)
        assert(!shot.isTurnEnd)
        assert(shot.shotCondition == ShotCondition.NORMAL)
        assert(listsMatch(ballStatus, mutableListOf(
            BallStatus.PLAYER1,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.DEAD,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.ON_TABLE,
            BallStatus.DEAD,
            BallStatus.ON_TABLE,
            BallStatus.PLAYER1
        )))
    }

    @Test
    fun testEclipseReturn() {
        val string = "e"
        val startCondition = ShotCondition.INTENTIONAL_ECLIPSE
        val ballStatus = customStartingBallStatus.toMutableList()
        val shot = Shot(string, PlayerTurn.PLAYER1, startCondition, ballStatus)
        expectedPlayerStats.achievements.eclipseReturn = 1
        expectedPlayerStats.achievements.eclipseEscape = 1
        expectedPlayerStats.achievements.eclipse = 1
        assert(playerStatsMatch(shot.shotPlayerStats, expectedPlayerStats))
        assert(shot.successfulDefense == 0)
        assert(shot.isTurnEnd)
        assert(shot.shotCondition == ShotCondition.ECLIPSE)
        assert(listsMatch(ballStatus, mutableListOf(
            BallStatus.PLAYER1,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.DEAD,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.DEAD,
            BallStatus.ON_TABLE,
            BallStatus.PLAYER1
        )))
    }

    @Test
    fun testSelfEclipseAfterEclipse() {
        val string = "4e"
        val startCondition = ShotCondition.INTENTIONAL_ECLIPSE
        val ballStatus = customStartingBallStatus.toMutableList()
        val shot = Shot(string, PlayerTurn.PLAYER1, startCondition, ballStatus)
        expectedPlayerStats.achievements.eclipseEscape = 1
        expectedPlayerStats.achievements.eclipsePocket = 1
        expectedPlayerStats.achievements.selfEclipse = 1
        setScore(1)
        assert(playerStatsMatch(shot.shotPlayerStats, expectedPlayerStats))
        assert(shot.successfulDefense == -1)
        assert(!shot.isTurnEnd)
        assert(shot.shotCondition == ShotCondition.SELF_ECLIPSE)
        assert(listsMatch(ballStatus, mutableListOf(
            BallStatus.PLAYER1,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.DEAD,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.ON_TABLE,
            BallStatus.DEAD,
            BallStatus.ON_TABLE,
            BallStatus.PLAYER1
        )))
    }

    @Test
    fun testFoulAfterSelfEclipse() {
        val string = "4W"
        val startCondition = ShotCondition.SELF_ECLIPSE
        val ballStatus = customStartingBallStatus.toMutableList()
        val shot = Shot(string, PlayerTurn.PLAYER1, startCondition, ballStatus)
        expectedPlayerStats.deadBalls = 1
        expectedPlayerStats.fouls.wrongBallFirst = 1
        assert(playerStatsMatch(shot.shotPlayerStats, expectedPlayerStats))
        assert(shot.successfulDefense == 0)
        assert(shot.isTurnEnd)
        assert(shot.shotCondition == ShotCondition.BALL_IN_HAND)
        assert(listsMatch(ballStatus, mutableListOf(
            BallStatus.PLAYER1,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.DEAD,
            BallStatus.DEAD,
            BallStatus.ON_TABLE,
            BallStatus.DEAD,
            BallStatus.ON_TABLE,
            BallStatus.PLAYER1
        )))
    }

    @Test
    fun testMissAfterSelfEclipse() {
        val string = ""
        val startCondition = ShotCondition.SELF_ECLIPSE
        val ballStatus = customStartingBallStatus.toMutableList()
        val shot = Shot(string, PlayerTurn.PLAYER1, startCondition, ballStatus)
        expectedPlayerStats.achievements.selfEclipseEscape = 1
        assert(playerStatsMatch(shot.shotPlayerStats, expectedPlayerStats))
        assert(shot.successfulDefense == 0)
        assert(shot.isTurnEnd)
        assert(shot.shotCondition == ShotCondition.NORMAL)
        assert(listsMatch(ballStatus, mutableListOf(
            BallStatus.PLAYER1,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.DEAD,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.DEAD,
            BallStatus.ON_TABLE,
            BallStatus.PLAYER1
        )))
    }

    @Test
    fun testPocketAfterSelfEclipse() {
        val string = "4"
        val startCondition = ShotCondition.SELF_ECLIPSE
        val ballStatus = customStartingBallStatus.toMutableList()
        val shot = Shot(string, PlayerTurn.PLAYER1, startCondition, ballStatus)
        expectedPlayerStats.achievements.selfEclipseEscape = 1
        expectedPlayerStats.achievements.selfEclipsePocket = 1
        setScore(1)
        assert(playerStatsMatch(shot.shotPlayerStats, expectedPlayerStats))
        assert(shot.successfulDefense == 0)
        assert(!shot.isTurnEnd)
        assert(shot.shotCondition == ShotCondition.NORMAL)
        assert(listsMatch(ballStatus, mutableListOf(
            BallStatus.PLAYER1,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.DEAD,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.ON_TABLE,
            BallStatus.DEAD,
            BallStatus.ON_TABLE,
            BallStatus.PLAYER1
        )))
    }

    @Test
    fun testEclipseAfterSelfEclipse() {
        val string = "e"
        val startCondition = ShotCondition.SELF_ECLIPSE
        val ballStatus = customStartingBallStatus.toMutableList()
        val shot = Shot(string, PlayerTurn.PLAYER1, startCondition, ballStatus)
        expectedPlayerStats.achievements.selfEclipseReturn = 1
        expectedPlayerStats.achievements.selfEclipseEscape = 1
        expectedPlayerStats.achievements.eclipse = 1
        assert(playerStatsMatch(shot.shotPlayerStats, expectedPlayerStats))
        assert(shot.successfulDefense == 0)
        assert(shot.isTurnEnd)
        assert(shot.shotCondition == ShotCondition.ECLIPSE)
        assert(listsMatch(ballStatus, mutableListOf(
            BallStatus.PLAYER1,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.DEAD,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.DEAD,
            BallStatus.ON_TABLE,
            BallStatus.PLAYER1
        )))
    }

    @Test
    fun testSelfEclipseAfterSelfEclipse() {
        val string = "4e"
        val startCondition = ShotCondition.SELF_ECLIPSE
        val ballStatus = customStartingBallStatus.toMutableList()
        val shot = Shot(string, PlayerTurn.PLAYER1, startCondition, ballStatus)
        expectedPlayerStats.achievements.selfEclipseEscape = 1
        expectedPlayerStats.achievements.selfEclipsePocket = 1
        expectedPlayerStats.achievements.selfEclipse = 1
        setScore(1)
        assert(playerStatsMatch(shot.shotPlayerStats, expectedPlayerStats))
        assert(shot.successfulDefense == 0)
        assert(!shot.isTurnEnd)
        assert(shot.shotCondition == ShotCondition.SELF_ECLIPSE)
        assert(listsMatch(ballStatus, mutableListOf(
            BallStatus.PLAYER1,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.DEAD,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.ON_TABLE,
            BallStatus.DEAD,
            BallStatus.ON_TABLE,
            BallStatus.PLAYER1
        )))
    }

    @Test
    fun testBadBreak() {
        val string = "B"
        val startCondition = ShotCondition.BREAK
        val ballStatus = onBallStatus(1).toMutableList()
        val shot = Shot(string, PlayerTurn.PLAYER1, startCondition, ballStatus)
        expectedPlayerStats.fouls.badBreak = 1
        assert(playerStatsMatch(shot.shotPlayerStats, expectedPlayerStats))
        assert(shot.successfulDefense == 0)
        assert(!shot.isTurnEnd)
        assert(shot.shotCondition == ShotCondition.BREAK)
        assert(listsMatch(ballStatus, mutableListOf(
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
        )))
    }

    @Test
    fun testBadBreakScratch() {
        val string = "BK"
        val startCondition = ShotCondition.BREAK
        val ballStatus = onBallStatus(1).toMutableList()
        val shot = Shot(string, PlayerTurn.PLAYER1, startCondition, ballStatus)
        expectedPlayerStats.fouls.badBreak = 1
        expectedPlayerStats.fouls.scratch = 1
        expectedPlayerStats.achievements.scratchOnBreak = 1
        assert(playerStatsMatch(shot.shotPlayerStats, expectedPlayerStats))
        assert(shot.successfulDefense == 0)
        assert(shot.isTurnEnd)
        assert(shot.shotCondition == ShotCondition.BREAK)
        assert(listsMatch(ballStatus, mutableListOf(
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
        )))
    }

    @Test
    fun testDryBreak() {
        val string = ""
        val startCondition = ShotCondition.BREAK
        val ballStatus = onBallStatus(1).toMutableList()
        val shot = Shot(string, PlayerTurn.PLAYER1, startCondition, ballStatus)
        expectedPlayerStats.achievements.pointsOnBreak[0] = 1
        assert(playerStatsMatch(shot.shotPlayerStats, expectedPlayerStats))
        assert(shot.successfulDefense == 0)
        assert(shot.isTurnEnd)
        assert(shot.shotCondition == ShotCondition.NORMAL)
        assert(listsMatch(ballStatus, mutableListOf(
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
        )))
    }

    @Test
    fun testOneBallOnBreak() {
        val string = "3"
        val startCondition = ShotCondition.BREAK
        val ballStatus = onBallStatus(1).toMutableList()
        val shot = Shot(string, PlayerTurn.PLAYER1, startCondition, ballStatus)
        expectedPlayerStats.achievements.pointsOnBreak[1] = 1
        setScore(1)
        assert(playerStatsMatch(shot.shotPlayerStats, expectedPlayerStats))
        assert(shot.successfulDefense == 0)
        assert(!shot.isTurnEnd)
        assert(shot.shotCondition == ShotCondition.NORMAL)
        assert(listsMatch(ballStatus, mutableListOf(
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
        )))
    }

    @Test
    fun testNineOnBreak() {
        val string = "39"
        val startCondition = ShotCondition.BREAK
        val ballStatus = onBallStatus(1).toMutableList()
        val shot = Shot(string, PlayerTurn.PLAYER1, startCondition, ballStatus)
        expectedPlayerStats.achievements.pointsOnBreak[3] = 1
        expectedPlayerStats.achievements.nineOnBreak = 1
        expectedPlayerStats.achievements.nines = 1
        setScore(3)
        assert(playerStatsMatch(shot.shotPlayerStats, expectedPlayerStats))
        assert(shot.successfulDefense == 0)
        assert(!shot.isTurnEnd)
        assert(shot.shotCondition == ShotCondition.BREAK)
        assert(listsMatch(ballStatus, mutableListOf(
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
        )))
    }

    @Test
    fun testBreakAndRunNineOnly() {
        val string = "9"
        val startCondition = ShotCondition.NORMAL
        val ballStatus = almostBreakAndRunBallStatusNineLeft.toMutableList()
        val shot = Shot(string, PlayerTurn.PLAYER1, startCondition, ballStatus)
        expectedPlayerStats.achievements.breakAndRun = 1
        expectedPlayerStats.achievements.perfectRack = 1
        expectedPlayerStats.achievements.nines = 1
        setScore(2)
        assert(playerStatsMatch(shot.shotPlayerStats, expectedPlayerStats))
        assert(shot.successfulDefense == 0)
        assert(!shot.isTurnEnd)
        assert(shot.shotCondition == ShotCondition.BREAK)
        assert(listsMatch(ballStatus, mutableListOf(
            BallStatus.SCORED_THIS_TURN,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.SCORED_THIS_TURN
        )))
    }

    @Test
    fun testBreakAndRunOneBallLeft() {
        val string = "79"
        val startCondition = ShotCondition.NORMAL
        val ballStatus = almostBreakAndRunBallStatusOneBallLeft.toMutableList()
        val shot = Shot(string, PlayerTurn.PLAYER1, startCondition, ballStatus)
        expectedPlayerStats.achievements.breakAndRun = 1
        expectedPlayerStats.achievements.perfectRack = 1
        expectedPlayerStats.achievements.nines = 1
        setScore(3)
        assert(playerStatsMatch(shot.shotPlayerStats, expectedPlayerStats))
        assert(shot.successfulDefense == 0)
        assert(!shot.isTurnEnd)
        assert(shot.shotCondition == ShotCondition.BREAK)
        assert(listsMatch(ballStatus, mutableListOf(
            BallStatus.SCORED_THIS_TURN,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.SCORED_THIS_TURN
        )))
    }

    @Test
    fun testEarlyNineCustom() {
        val string = "9"
        val startCondition = ShotCondition.NORMAL
        val ballStatus = customStartingBallStatus.toMutableList()
        val shot = Shot(string, PlayerTurn.PLAYER1, startCondition, ballStatus)
        expectedPlayerStats.achievements.earlyNine = 1
        expectedPlayerStats.achievements.nines = 1
        setScore(2)
        assert(playerStatsMatch(shot.shotPlayerStats, expectedPlayerStats))
        assert(shot.successfulDefense == 0)
        assert(!shot.isTurnEnd)
        assert(shot.shotCondition == ShotCondition.BREAK)
        assert(listsMatch(ballStatus, mutableListOf(
            BallStatus.PLAYER1,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.DEAD,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.DEAD,
            BallStatus.ON_TABLE,
            BallStatus.PLAYER1
        )))
    }

    @Test
    fun testEarlyNineStart() {
        val string = "9"
        val startCondition = ShotCondition.NORMAL
        val ballStatus = onBallStatus(1).toMutableList()
        val shot = Shot(string, PlayerTurn.PLAYER1, startCondition, ballStatus)
        expectedPlayerStats.achievements.earlyNine = 1
        expectedPlayerStats.achievements.nines = 1
        setScore(2)
        assert(playerStatsMatch(shot.shotPlayerStats, expectedPlayerStats))
        assert(shot.successfulDefense == 0)
        assert(!shot.isTurnEnd)
        assert(shot.shotCondition == ShotCondition.BREAK)
        assert(listsMatch(ballStatus, mutableListOf(
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
        )))
    }

    @Test
    fun testEarlyNineOneBallLeft() {
        val string = "9"
        val startCondition = ShotCondition.NORMAL
        val ballStatus = almostBreakAndRunBallStatusOneBallLeft.toMutableList()
        val shot = Shot(string, PlayerTurn.PLAYER1, startCondition, ballStatus)
        expectedPlayerStats.achievements.earlyNine = 1
        expectedPlayerStats.achievements.nines = 1
        setScore(2)
        assert(playerStatsMatch(shot.shotPlayerStats, expectedPlayerStats))
        assert(shot.successfulDefense == 0)
        assert(!shot.isTurnEnd)
        assert(shot.shotCondition == ShotCondition.BREAK)
        assert(listsMatch(ballStatus, mutableListOf(
            BallStatus.SCORED_THIS_TURN,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.ON_TABLE,
            BallStatus.SCORED_THIS_TURN
        )))
    }

    @Test
    fun testRegularNinePerfect() {
        val string = "9"
        val startCondition = ShotCondition.NORMAL
        val ballStatus = ninePerfect.toMutableList()
        val shot = Shot(string, PlayerTurn.PLAYER1, startCondition, ballStatus)
        setScore(2)
        expectedPlayerStats.achievements.perfectRack = 1
        expectedPlayerStats.achievements.nines = 1
        assert(playerStatsMatch(shot.shotPlayerStats, expectedPlayerStats))
        assert(shot.successfulDefense == 0)
        assert(!shot.isTurnEnd)
        assert(shot.shotCondition == ShotCondition.BREAK)
        assert(listsMatch(ballStatus, mutableListOf(
            BallStatus.SCORED_THIS_TURN,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.PLAYER1,
            BallStatus.SCORED_THIS_TURN
        )))
    }

    @Test
    fun testRegularNineNotPerfect() {
        val string = "9"
        val startCondition = ShotCondition.NORMAL
        val ballStatus = nineNotPerfect.toMutableList()
        val shot = Shot(string, PlayerTurn.PLAYER1, startCondition, ballStatus)
        expectedPlayerStats.achievements.nines = 1
        setScore(2)
        assert(playerStatsMatch(shot.shotPlayerStats, expectedPlayerStats))
        assert(shot.successfulDefense == 0)
        assert(!shot.isTurnEnd)
        assert(shot.shotCondition == ShotCondition.BREAK)
        assert(listsMatch(ballStatus, mutableListOf(
            BallStatus.SCORED_THIS_TURN,
            BallStatus.PLAYER2,
            BallStatus.PLAYER2,
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.PLAYER1,
            BallStatus.SCORED_THIS_TURN
        )))
    }

    @Test
    fun testDryBreakScratch() {
        val string = "K"
        val startCondition = ShotCondition.BREAK
        val ballStatus = onBallStatus(1).toMutableList()
        val shot = Shot(string, PlayerTurn.PLAYER1, startCondition, ballStatus)
        expectedPlayerStats.achievements.pointsOnBreak[0] = 1
        expectedPlayerStats.achievements.scratchOnBreak = 1
        expectedPlayerStats.fouls.scratch = 1
        assert(playerStatsMatch(shot.shotPlayerStats, expectedPlayerStats))
        assert(shot.successfulDefense == 0)
        assert(shot.isTurnEnd)
        assert(shot.shotCondition == ShotCondition.BALL_IN_HAND)
        assert(listsMatch(ballStatus, mutableListOf(
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
        )))
    }

    @Test
    fun testOneBallOnBreakScratch() {
        val string = "3K"
        val startCondition = ShotCondition.BREAK
        val ballStatus = onBallStatus(1).toMutableList()
        val shot = Shot(string, PlayerTurn.PLAYER1, startCondition, ballStatus)
        expectedPlayerStats.achievements.pointsOnBreak[0] = 1
        expectedPlayerStats.achievements.scratchOnBreak = 1
        expectedPlayerStats.fouls.scratch = 1
        expectedPlayerStats.deadBalls = 1
        assert(playerStatsMatch(shot.shotPlayerStats, expectedPlayerStats))
        assert(shot.successfulDefense == 0)
        assert(shot.isTurnEnd)
        assert(shot.shotCondition == ShotCondition.BALL_IN_HAND)
        assert(listsMatch(ballStatus, mutableListOf(
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.DEAD,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
        )))
    }

    @Test
    fun testNineOnBreakScratch() {
        val string = "39K"
        val startCondition = ShotCondition.BREAK
        val ballStatus = onBallStatus(1).toMutableList()
        val shot = Shot(string, PlayerTurn.PLAYER1, startCondition, ballStatus)
        expectedPlayerStats.achievements.pointsOnBreak[0] = 1
        expectedPlayerStats.achievements.illegalNine = 1
        expectedPlayerStats.achievements.scratchOnBreak = 1
        expectedPlayerStats.deadBalls = 1
        expectedPlayerStats.fouls.scratch = 1
        assert(playerStatsMatch(shot.shotPlayerStats, expectedPlayerStats))
        assert(shot.successfulDefense == 0)
        assert(shot.isTurnEnd)
        assert(shot.shotCondition == ShotCondition.BALL_IN_HAND)
        assert(listsMatch(ballStatus, mutableListOf(
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.DEAD,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
        )))
    }

    @Test
    fun testBreakAndRunNineOnlyScratch() {
        val string = "9K"
        val startCondition = ShotCondition.NORMAL
        val ballStatus = almostBreakAndRunBallStatusNineLeft.toMutableList()
        val shot = Shot(string, PlayerTurn.PLAYER1, startCondition, ballStatus)
        expectedPlayerStats.achievements.illegalNine = 1
        expectedPlayerStats.fouls.scratch = 1
        assert(playerStatsMatch(shot.shotPlayerStats, expectedPlayerStats))
        assert(shot.successfulDefense == 0)
        assert(shot.isTurnEnd)
        assert(shot.shotCondition == ShotCondition.BALL_IN_HAND)
        assert(listsMatch(ballStatus, mutableListOf(
            BallStatus.SCORED_THIS_TURN,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.SCORED_THIS_TURN
        )))
    }

    @Test
    fun testBreakAndRunOneBallLeftScratch() {
        val string = "79K"
        val startCondition = ShotCondition.NORMAL
        val ballStatus = almostBreakAndRunBallStatusOneBallLeft.toMutableList()
        val shot = Shot(string, PlayerTurn.PLAYER1, startCondition, ballStatus)
        expectedPlayerStats.achievements.illegalNine = 1
        expectedPlayerStats.fouls.scratch = 1
        expectedPlayerStats.deadBalls = 1
        assert(playerStatsMatch(shot.shotPlayerStats, expectedPlayerStats))
        assert(shot.successfulDefense == 0)
        assert(shot.isTurnEnd)
        assert(shot.shotCondition == ShotCondition.BALL_IN_HAND)
        assert(listsMatch(ballStatus, mutableListOf(
            BallStatus.SCORED_THIS_TURN,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.DEAD,
            BallStatus.SCORED_THIS_TURN
        )))
    }

    @Test
    fun testEarlyNineCustomScratch() {
        val string = "9K"
        val startCondition = ShotCondition.NORMAL
        val ballStatus = customStartingBallStatus.toMutableList()
        val shot = Shot(string, PlayerTurn.PLAYER1, startCondition, ballStatus)
        expectedPlayerStats.achievements.illegalNine = 1
        expectedPlayerStats.fouls.scratch = 1
        assert(playerStatsMatch(shot.shotPlayerStats, expectedPlayerStats))
        assert(shot.successfulDefense == 0)
        assert(shot.isTurnEnd)
        assert(shot.shotCondition == ShotCondition.BALL_IN_HAND)
        assert(listsMatch(ballStatus, mutableListOf(
            BallStatus.PLAYER1,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.DEAD,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.DEAD,
            BallStatus.ON_TABLE,
            BallStatus.PLAYER1
        )))
    }

    @Test
    fun testEarlyNineStartScratch() {
        val string = "9K"
        val startCondition = ShotCondition.NORMAL
        val ballStatus = onBallStatus(1).toMutableList()
        val shot = Shot(string, PlayerTurn.PLAYER1, startCondition, ballStatus)
        expectedPlayerStats.achievements.illegalNine = 1
        expectedPlayerStats.fouls.scratch = 1
        assert(playerStatsMatch(shot.shotPlayerStats, expectedPlayerStats))
        assert(shot.successfulDefense == 0)
        assert(shot.isTurnEnd)
        assert(shot.shotCondition == ShotCondition.BALL_IN_HAND)
        assert(listsMatch(ballStatus, mutableListOf(
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
        )))
    }

    @Test
    fun testEarlyNineOneBallLeftScratch() {
        val string = "9K"
        val startCondition = ShotCondition.NORMAL
        val ballStatus = almostBreakAndRunBallStatusOneBallLeft.toMutableList()
        val shot = Shot(string, PlayerTurn.PLAYER1, startCondition, ballStatus)
        expectedPlayerStats.achievements.illegalNine = 1
        expectedPlayerStats.fouls.scratch = 1
        assert(playerStatsMatch(shot.shotPlayerStats, expectedPlayerStats))
        assert(shot.successfulDefense == 0)
        assert(shot.isTurnEnd)
        assert(shot.shotCondition == ShotCondition.BALL_IN_HAND)
        assert(listsMatch(ballStatus, mutableListOf(
            BallStatus.SCORED_THIS_TURN,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.ON_TABLE,
            BallStatus.SCORED_THIS_TURN
        )))
    }

    @Test
    fun testRegularNineScratch() {
        val string = "9K"
        val startCondition = ShotCondition.NORMAL
        val ballStatus = onBallStatus(9).toMutableList()
        val shot = Shot(string, PlayerTurn.PLAYER1, startCondition, ballStatus)
        expectedPlayerStats.achievements.illegalNine = 1
        expectedPlayerStats.fouls.scratch = 1
        assert(playerStatsMatch(shot.shotPlayerStats, expectedPlayerStats))
        assert(shot.successfulDefense == 0)
        assert(shot.isTurnEnd)
        assert(shot.shotCondition == ShotCondition.BALL_IN_HAND)
        assert(listsMatch(ballStatus, mutableListOf(
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.PLAYER1
        )))
    }

    @Test
    fun testLucky() {
        val string = "4l"
        val startCondition = ShotCondition.NORMAL
        val ballStatus = customStartingBallStatus.toMutableList()
        val shot = Shot(string, PlayerTurn.PLAYER1, startCondition, ballStatus)
        expectedPlayerStats.achievements.lucky = 1
        setScore(1)
        assert(playerStatsMatch(shot.shotPlayerStats, expectedPlayerStats))
        assert(shot.successfulDefense == 0)
        assert(!shot.isTurnEnd)
        assert(shot.shotCondition == ShotCondition.NORMAL)
        assert(listsMatch(ballStatus, mutableListOf(
            BallStatus.PLAYER1,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.DEAD,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.ON_TABLE,
            BallStatus.DEAD,
            BallStatus.ON_TABLE,
            BallStatus.PLAYER1
        )))
    }

    @Test
    fun testBankMiss() {
        val string = "b"
        val startCondition = ShotCondition.NORMAL
        val ballStatus = customStartingBallStatus.toMutableList()
        val shot = Shot(string, PlayerTurn.PLAYER1, startCondition, ballStatus)
        expectedPlayerStats.achievements.bank.failure = 1
        expectedPlayerStats.achievements.bank.total = 1
        assert(playerStatsMatch(shot.shotPlayerStats, expectedPlayerStats))
        assert(shot.successfulDefense == 0)
        assert(shot.isTurnEnd)
        assert(shot.shotCondition == ShotCondition.NORMAL)
        assert(listsMatch(ballStatus, mutableListOf(
            BallStatus.PLAYER1,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.DEAD,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.DEAD,
            BallStatus.ON_TABLE,
            BallStatus.PLAYER1
        )))
    }

    @Test
    fun testBank() {
        val string = "4b"
        val startCondition = ShotCondition.NORMAL
        val ballStatus = customStartingBallStatus.toMutableList()
        val shot = Shot(string, PlayerTurn.PLAYER1, startCondition, ballStatus)
        expectedPlayerStats.achievements.bank.success = 1
        expectedPlayerStats.achievements.bank.total = 1
        setScore(1)
        assert(playerStatsMatch(shot.shotPlayerStats, expectedPlayerStats))
        assert(shot.successfulDefense == 0)
        assert(!shot.isTurnEnd)
        assert(shot.shotCondition == ShotCondition.NORMAL)
        assert(listsMatch(ballStatus, mutableListOf(
            BallStatus.PLAYER1,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.DEAD,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.ON_TABLE,
            BallStatus.DEAD,
            BallStatus.ON_TABLE,
            BallStatus.PLAYER1
        )))
    }

    @Test
    fun testBankScratch() {
        val string = "4Kb"
        val startCondition = ShotCondition.NORMAL
        val ballStatus = customStartingBallStatus.toMutableList()
        val shot = Shot(string, PlayerTurn.PLAYER1, startCondition, ballStatus)
        expectedPlayerStats.achievements.bank.failure = 1
        expectedPlayerStats.achievements.bank.total = 1
        expectedPlayerStats.fouls.scratch = 1
        expectedPlayerStats.deadBalls = 1
        assert(playerStatsMatch(shot.shotPlayerStats, expectedPlayerStats))
        assert(shot.successfulDefense == 0)
        assert(shot.isTurnEnd)
        assert(shot.shotCondition == ShotCondition.BALL_IN_HAND)
        assert(listsMatch(ballStatus, mutableListOf(
            BallStatus.PLAYER1,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.DEAD,
            BallStatus.DEAD,
            BallStatus.ON_TABLE,
            BallStatus.DEAD,
            BallStatus.ON_TABLE,
            BallStatus.PLAYER1
        )))
    }

    @Test
    fun testOneBallLucky() {
        val string = "4l"
        val startCondition = ShotCondition.NORMAL
        val ballStatus = customStartingBallStatus.toMutableList()
        val shot = Shot(string, PlayerTurn.PLAYER1, startCondition, ballStatus)
        setScore(1)
        expectedPlayerStats.achievements.lucky = 1
        assert(playerStatsMatch(shot.shotPlayerStats, expectedPlayerStats))
        assert(shot.successfulDefense == 0)
        assert(!shot.isTurnEnd)
        assert(shot.shotCondition == ShotCondition.NORMAL)
        assert(listsMatch(ballStatus, mutableListOf(
            BallStatus.PLAYER1,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.DEAD,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.ON_TABLE,
            BallStatus.DEAD,
            BallStatus.ON_TABLE,
            BallStatus.PLAYER1
        )))
    }
    @Test
    fun testTwoBallsLucky() {
        val string = "45l"
        val startCondition = ShotCondition.NORMAL
        val ballStatus = customStartingBallStatus.toMutableList()
        val shot = Shot(string, PlayerTurn.PLAYER1, startCondition, ballStatus)
        setScore(2)
        expectedPlayerStats.achievements.lucky = 2
        assert(playerStatsMatch(shot.shotPlayerStats, expectedPlayerStats))
        assert(shot.successfulDefense == 0)
        assert(!shot.isTurnEnd)
        assert(shot.shotCondition == ShotCondition.NORMAL)
        assert(listsMatch(ballStatus, mutableListOf(
            BallStatus.PLAYER1,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.DEAD,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.SCORED_THIS_TURN,
            BallStatus.DEAD,
            BallStatus.ON_TABLE,
            BallStatus.PLAYER1
        )))
    }

    private fun setScore(shotScore: Int) {
        expectedPlayerStats.score = shotScore
        expectedPlayerStats.streaks.currentStreak = shotScore
        expectedPlayerStats.streaks.currentTurnStreak = shotScore
    }
}