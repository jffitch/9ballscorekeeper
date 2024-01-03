package com.mathgeniusguide.nineballscorekeeper.objects

import com.mathgeniusguide.nineballscorekeeper.backToBackToBackBreakAndRunInning
import com.mathgeniusguide.nineballscorekeeper.badBreakInning
import com.mathgeniusguide.nineballscorekeeper.badBreakScratchAfterNineInning
import com.mathgeniusguide.nineballscorekeeper.badBreakScratchInning
import com.mathgeniusguide.nineballscorekeeper.ballInHandAfterOnePointInning
import com.mathgeniusguide.nineballscorekeeper.ballInHandAfterThreePointsInning
import com.mathgeniusguide.nineballscorekeeper.ballInHandInning
import com.mathgeniusguide.nineballscorekeeper.breakAndRunThenContinueInning
import com.mathgeniusguide.nineballscorekeeper.breakAndRunThenMissInning
import com.mathgeniusguide.nineballscorekeeper.continuedAfterBadBreakScratchInning
import com.mathgeniusguide.nineballscorekeeper.continuedAfterFoulInning
import com.mathgeniusguide.nineballscorekeeper.continuedAfterNoPocketInning
import com.mathgeniusguide.nineballscorekeeper.defenseInning
import com.mathgeniusguide.nineballscorekeeper.dryInning
import com.mathgeniusguide.nineballscorekeeper.earlyNineInning
import com.mathgeniusguide.nineballscorekeeper.eightNotPerfect
import com.mathgeniusguide.nineballscorekeeper.enums.BallStatus
import com.mathgeniusguide.nineballscorekeeper.enums.PlayerTurn
import com.mathgeniusguide.nineballscorekeeper.enums.ShotCondition
import com.mathgeniusguide.nineballscorekeeper.foulOnDefenseInning
import com.mathgeniusguide.nineballscorekeeper.illegalNineInning
import com.mathgeniusguide.nineballscorekeeper.immediateEclipseInning
import com.mathgeniusguide.nineballscorekeeper.intentionalEclipseInning
import com.mathgeniusguide.nineballscorekeeper.listsMatch
import com.mathgeniusguide.nineballscorekeeper.multipleSelfEclipsesInning
import com.mathgeniusguide.nineballscorekeeper.nineNotPerfect
import com.mathgeniusguide.nineballscorekeeper.nineThenFourOnBreakInning
import com.mathgeniusguide.nineballscorekeeper.nineThenScratchOnBreakInning
import com.mathgeniusguide.nineballscorekeeper.objects.stats.PlayerStats
import com.mathgeniusguide.nineballscorekeeper.onBallStatus
import com.mathgeniusguide.nineballscorekeeper.onePointInning
import com.mathgeniusguide.nineballscorekeeper.perfectRackInning
import com.mathgeniusguide.nineballscorekeeper.playerStatsMatch
import com.mathgeniusguide.nineballscorekeeper.selfDefenseInning
import com.mathgeniusguide.nineballscorekeeper.selfEclipseReturnInning
import com.mathgeniusguide.nineballscorekeeper.twoInOneShotInning
import com.mathgeniusguide.nineballscorekeeper.twoPointsInning
import org.junit.Before
import org.junit.Test

class InningTest {
    private var expectedPlayerStats = PlayerStats()
    private var ballStatus = onBallStatus(1).toMutableList()
    private val rackList = mutableListOf(Rack())

    @Before
    fun before() {
        expectedPlayerStats = PlayerStats()
    }

    @Test
    fun testDryInning() {
        val inningString = dryInning
        ballStatus = onBallStatus(4).toMutableList()
        val inning = Inning(inningString, PlayerTurn.PLAYER1, ShotCondition.NORMAL, ballStatus, rackList)

        expectedPlayerStats.shotsTaken = 1

        assert(inning.shotCondition == ShotCondition.NORMAL)
        assert(inning.successfulDefense == 0)
        assert(inning.leftOnTable == 0)
        assert(playerStatsMatch(inning.inningPlayerStats, expectedPlayerStats))
        assert(listsMatch(ballStatus, listOf(
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE
        )))
    }

    @Test
    fun testBallInHandMissInning() {
        val inningString = dryInning
        ballStatus = onBallStatus(4).toMutableList()
        val inning = Inning(inningString, PlayerTurn.PLAYER1, ShotCondition.BALL_IN_HAND, ballStatus, rackList)

        expectedPlayerStats.achievements.ballInHandMiss = 1
        expectedPlayerStats.shotsTaken = 1

        assert(inning.shotCondition == ShotCondition.NORMAL)
        assert(inning.successfulDefense == 0)
        assert(inning.leftOnTable == 0)
        assert(playerStatsMatch(inning.inningPlayerStats, expectedPlayerStats))
        assert(listsMatch(ballStatus, listOf(
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE
        )))
    }

    @Test
    fun testOnePointInning() {
        val inningString = onePointInning
        ballStatus = onBallStatus(1).toMutableList()
        val inning = Inning(inningString, PlayerTurn.PLAYER1, ShotCondition.NORMAL, ballStatus, rackList)

        setScore(1)
        expectedPlayerStats.shotsTaken = 2

        assert(inning.shotCondition == ShotCondition.NORMAL)
        assert(inning.successfulDefense == 0)
        assert(inning.leftOnTable == 0)
        assert(playerStatsMatch(inning.inningPlayerStats, expectedPlayerStats))
        assert(listsMatch(ballStatus, listOf(
            BallStatus.PLAYER1,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE
        )))
    }

    @Test
    fun testTwoPointsInning() {
        val inningString = twoPointsInning
        ballStatus = onBallStatus(1).toMutableList()
        val inning = Inning(inningString, PlayerTurn.PLAYER1, ShotCondition.NORMAL, ballStatus, rackList)

        setScore(2)
        expectedPlayerStats.shotsTaken = 3

        assert(inning.shotCondition == ShotCondition.NORMAL)
        assert(inning.successfulDefense == 0)
        assert(inning.leftOnTable == 0)
        assert(playerStatsMatch(inning.inningPlayerStats, expectedPlayerStats))
        assert(listsMatch(ballStatus, listOf(
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE
        )))
    }

    @Test
    fun testTwoInOneShotInning() {
        val inningString = twoInOneShotInning
        ballStatus = onBallStatus(1).toMutableList()
        val inning = Inning(inningString, PlayerTurn.PLAYER1, ShotCondition.NORMAL, ballStatus, rackList)

        setScore(2)
        expectedPlayerStats.shotsTaken = 2

        assert(inning.shotCondition == ShotCondition.NORMAL)
        assert(inning.successfulDefense == 0)
        assert(inning.leftOnTable == 0)
        assert(playerStatsMatch(inning.inningPlayerStats, expectedPlayerStats))
        assert(listsMatch(ballStatus, listOf(
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE
        )))
    }

    @Test
    fun testIntentionalEclipseInning() {
        val inningString = intentionalEclipseInning
        ballStatus = onBallStatus(4).toMutableList()
        val inning = Inning(inningString, PlayerTurn.PLAYER1, ShotCondition.NORMAL, ballStatus, rackList)

        setScore(1)
        expectedPlayerStats.shotsTaken = 2
        expectedPlayerStats.achievements.defense.total = 1
        expectedPlayerStats.achievements.eclipse = 1
        expectedPlayerStats.achievements.intentionalEclipse = 1

        assert(inning.shotCondition == ShotCondition.INTENTIONAL_ECLIPSE)
        assert(inning.successfulDefense == 0)
        assert(inning.leftOnTable == 0)
        assert(playerStatsMatch(inning.inningPlayerStats, expectedPlayerStats))
        assert(listsMatch(ballStatus, listOf(
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE
        )))
    }

    @Test
    fun testDefenseInning() {
        val inningString = defenseInning
        ballStatus = onBallStatus(2).toMutableList()
        val inning = Inning(inningString, PlayerTurn.PLAYER1, ShotCondition.NORMAL, ballStatus, rackList)

        setScore(2)
        expectedPlayerStats.shotsTaken = 3
        expectedPlayerStats.achievements.defense.total = 1

        assert(inning.shotCondition == ShotCondition.DEFENSE)
        assert(inning.successfulDefense == 0)
        assert(inning.leftOnTable == 0)
        assert(playerStatsMatch(inning.inningPlayerStats, expectedPlayerStats))
        assert(listsMatch(ballStatus, listOf(
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE
        )))
    }

    @Test
    fun testSelfDefenseInning() {
        val inningString = selfDefenseInning
        ballStatus = onBallStatus(3).toMutableList()
        val inning = Inning(inningString, PlayerTurn.PLAYER1, ShotCondition.NORMAL, ballStatus, rackList)

        setScore(3)
        expectedPlayerStats.shotsTaken = 4
        expectedPlayerStats.achievements.defense.failure = 1
        expectedPlayerStats.achievements.defense.total = 1

        assert(inning.shotCondition == ShotCondition.NORMAL)
        assert(inning.successfulDefense == 0)
        assert(inning.leftOnTable == 0)
        assert(playerStatsMatch(inning.inningPlayerStats, expectedPlayerStats))
        assert(listsMatch(ballStatus, listOf(
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE
        )))
    }

    @Test
    fun testFoulOnDefenseInning() {
        val inningString = foulOnDefenseInning
        ballStatus = onBallStatus(3).toMutableList()
        val inning = Inning(inningString, PlayerTurn.PLAYER1, ShotCondition.NORMAL, ballStatus, rackList)

        setScore(2)
        expectedPlayerStats.shotsTaken = 3
        expectedPlayerStats.fouls.noRail = 1
        expectedPlayerStats.achievements.defense.failure = 1
        expectedPlayerStats.achievements.defense.total = 1

        assert(inning.shotCondition == ShotCondition.BALL_IN_HAND)
        assert(inning.successfulDefense == 0)
        assert(inning.leftOnTable == 0)
        assert(playerStatsMatch(inning.inningPlayerStats, expectedPlayerStats))
        assert(listsMatch(ballStatus, listOf(
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE
        )))
    }

    @Test
    fun testBallInHandAfterThreePointsInning() {
        val inningString = ballInHandAfterThreePointsInning
        ballStatus = onBallStatus(5).toMutableList()
        val inning = Inning(inningString, PlayerTurn.PLAYER1, ShotCondition.NORMAL, ballStatus, rackList)

        setScore(3)
        expectedPlayerStats.shotsTaken = 4
        expectedPlayerStats.fouls.wrongBallFirst = 1
        expectedPlayerStats.deadBalls = 1

        assert(inning.shotCondition == ShotCondition.BALL_IN_HAND)
        assert(inning.successfulDefense == 0)
        assert(inning.leftOnTable == 0)
        assert(playerStatsMatch(inning.inningPlayerStats, expectedPlayerStats))
        assert(listsMatch(ballStatus, listOf(
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.DEAD
        )))
    }

    @Test
    fun testBallInHandAfterOnePointInning() {
        val inningString = ballInHandAfterOnePointInning
        ballStatus = onBallStatus(7).toMutableList()
        val inning = Inning(inningString, PlayerTurn.PLAYER1, ShotCondition.NORMAL, ballStatus, rackList)

        setScore(1)
        expectedPlayerStats.shotsTaken = 2
        expectedPlayerStats.fouls.scratch = 1
        expectedPlayerStats.deadBalls = 1

        assert(inning.shotCondition == ShotCondition.BALL_IN_HAND)
        assert(inning.successfulDefense == 0)
        assert(inning.leftOnTable == 0)
        assert(playerStatsMatch(inning.inningPlayerStats, expectedPlayerStats))
        assert(listsMatch(ballStatus, listOf(
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.DEAD
        )))
    }

    @Test
    fun testBallInHandInning() {
        val inningString = ballInHandInning
        ballStatus = onBallStatus(5).toMutableList()
        val inning = Inning(inningString, PlayerTurn.PLAYER1, ShotCondition.NORMAL, ballStatus, rackList)

        expectedPlayerStats.shotsTaken = 1
        expectedPlayerStats.fouls.miss = 1

        assert(inning.shotCondition == ShotCondition.BALL_IN_HAND)
        assert(inning.successfulDefense == 0)
        assert(inning.leftOnTable == 0)
        assert(playerStatsMatch(inning.inningPlayerStats, expectedPlayerStats))
        assert(listsMatch(ballStatus, listOf(
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE
        )))
    }

    @Test
    fun testBallInHandAfterDefenseInning() {
        val inningString = ballInHandInning
        ballStatus = onBallStatus(5).toMutableList()
        val inning = Inning(inningString, PlayerTurn.PLAYER1, ShotCondition.DEFENSE, ballStatus, rackList)

        expectedPlayerStats.shotsTaken = 1
        expectedPlayerStats.fouls.miss = 1

        assert(inning.shotCondition == ShotCondition.BALL_IN_HAND)
        assert(inning.successfulDefense == 1)
        assert(inning.leftOnTable == 0)
        assert(playerStatsMatch(inning.inningPlayerStats, expectedPlayerStats))
        assert(listsMatch(ballStatus, listOf(
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE
        )))
    }

    @Test
    fun testBallInHandReturnInning() {
        val inningString = ballInHandInning
        ballStatus = onBallStatus(5).toMutableList()
        val inning = Inning(inningString, PlayerTurn.PLAYER1, ShotCondition.BALL_IN_HAND, ballStatus, rackList)

        expectedPlayerStats.shotsTaken = 1
        expectedPlayerStats.fouls.miss = 1
        expectedPlayerStats.achievements.ballInHandReturn = 1

        assert(inning.shotCondition == ShotCondition.BALL_IN_HAND)
        assert(inning.successfulDefense == 0)
        assert(inning.leftOnTable == 0)
        assert(playerStatsMatch(inning.inningPlayerStats, expectedPlayerStats))
        assert(listsMatch(ballStatus, listOf(
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE
        )))
    }

    @Test
    fun testBreakAndRunThenMissInning() {
        val inningString = breakAndRunThenMissInning
        ballStatus = onBallStatus(1).toMutableList()
        val inning = Inning(inningString, PlayerTurn.PLAYER1, ShotCondition.BREAK, ballStatus, rackList)

        setScore(10)
        expectedPlayerStats.shotsTaken = 9
        expectedPlayerStats.achievements.breakAndRun = 1
        expectedPlayerStats.achievements.perfectRack = 1
        expectedPlayerStats.achievements.pointsOnBreak[0] = 1
        expectedPlayerStats.achievements.pointsOnBreak[2] = 1
        expectedPlayerStats.achievements.nines = 1

        assert(inning.shotCondition == ShotCondition.NORMAL)
        assert(inning.successfulDefense == 0)
        assert(inning.leftOnTable == 0)
        assert(playerStatsMatch(inning.inningPlayerStats, expectedPlayerStats))
        assert(listsMatch(ballStatus, listOf(
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE
        )))
    }

    @Test
    fun testBreakAndRunThenContinueInning() {
        val inningString = breakAndRunThenContinueInning
        ballStatus = onBallStatus(3).toMutableList()
        val inning = Inning(inningString, PlayerTurn.PLAYER1, ShotCondition.NORMAL, ballStatus, rackList)

        setScore(18)
        expectedPlayerStats.shotsTaken = 15
        expectedPlayerStats.achievements.pointsOnBreak[1] = 1
        expectedPlayerStats.achievements.pointsOnBreak[2] = 1
        expectedPlayerStats.achievements.breakAndRun = 1
        expectedPlayerStats.achievements.perfectRack = 1
        expectedPlayerStats.achievements.earlyNine = 1
        expectedPlayerStats.achievements.nines = 2

        assert(inning.shotCondition == ShotCondition.NORMAL)
        assert(inning.successfulDefense == 0)
        assert(inning.leftOnTable == 5)
        assert(playerStatsMatch(inning.inningPlayerStats, expectedPlayerStats))
        assert(listsMatch(ballStatus, listOf(
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.PLAYER1,
            BallStatus.ON_TABLE
        )))
    }

    @Test
    fun testBackToBackToBackBreakAndRunInning() {
        val inningString = backToBackToBackBreakAndRunInning
        ballStatus = onBallStatus(7).toMutableList()
        val inning = Inning(inningString, PlayerTurn.PLAYER1, ShotCondition.NORMAL, ballStatus, rackList)

        setScore(36)
        expectedPlayerStats.shotsTaken = 31
        expectedPlayerStats.achievements.pointsOnBreak[1] = 2
        expectedPlayerStats.achievements.pointsOnBreak[2] = 2
        expectedPlayerStats.achievements.breakAndRun = 3
        expectedPlayerStats.achievements.perfectRack = 4
        expectedPlayerStats.achievements.defense.total = 1
        expectedPlayerStats.achievements.eclipse = 1
        expectedPlayerStats.achievements.intentionalEclipse = 1
        expectedPlayerStats.achievements.nines = 4

        assert(inning.shotCondition == ShotCondition.INTENTIONAL_ECLIPSE)
        assert(inning.successfulDefense == 0)
        assert(inning.leftOnTable == 0)
        assert(playerStatsMatch(inning.inningPlayerStats, expectedPlayerStats))
        assert(listsMatch(ballStatus, listOf(
            BallStatus.PLAYER1,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.PLAYER1,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE
        )))
    }

    @Test
    fun testPerfectRackInning() {
        val inningString = perfectRackInning
        ballStatus = onBallStatus(6).toMutableList()
        val inning = Inning(inningString, PlayerTurn.PLAYER1, ShotCondition.NORMAL, ballStatus, rackList)

        setScore(5)
        expectedPlayerStats.shotsTaken = 5
        expectedPlayerStats.achievements.perfectRack = 1
        expectedPlayerStats.achievements.pointsOnBreak[0] = 1
        expectedPlayerStats.achievements.nines = 1

        assert(inning.shotCondition == ShotCondition.NORMAL)
        assert(inning.successfulDefense == 0)
        assert(inning.leftOnTable == 0)
        assert(playerStatsMatch(inning.inningPlayerStats, expectedPlayerStats))
        assert(listsMatch(ballStatus, listOf(
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE
        )))
    }

    @Test
    fun testEarlyNineInning() {
        val inningString = earlyNineInning
        ballStatus = onBallStatus(1).toMutableList()
        val inning = Inning(inningString, PlayerTurn.PLAYER1, ShotCondition.BREAK, ballStatus, rackList)

        setScore(10)
        expectedPlayerStats.shotsTaken = 10
        expectedPlayerStats.achievements.earlyNine = 1
        expectedPlayerStats.achievements.pointsOnBreak[1] = 2
        expectedPlayerStats.achievements.nines = 1

        assert(inning.shotCondition == ShotCondition.NORMAL)
        assert(inning.successfulDefense == 0)
        assert(inning.leftOnTable == 1)
        assert(playerStatsMatch(inning.inningPlayerStats, expectedPlayerStats))
        assert(listsMatch(ballStatus, listOf(
            BallStatus.PLAYER1,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE
        )))
    }

    @Test
    fun testNineThenScratchOnBreakInning() {
        val inningString = nineThenScratchOnBreakInning
        ballStatus = eightNotPerfect.toMutableList()
        val inning = Inning(inningString, PlayerTurn.PLAYER1, ShotCondition.NORMAL, ballStatus, rackList)

        setScore(3)
        expectedPlayerStats.shotsTaken = 3
        expectedPlayerStats.fouls.scratch = 1
        expectedPlayerStats.achievements.pointsOnBreak[0] = 1
        expectedPlayerStats.achievements.scratchOnBreak = 1
        expectedPlayerStats.deadBalls = 1
        expectedPlayerStats.achievements.nines = 1

        assert(inning.shotCondition == ShotCondition.BALL_IN_HAND)
        assert(inning.successfulDefense == 0)
        assert(inning.leftOnTable == 0)
        assert(playerStatsMatch(inning.inningPlayerStats, expectedPlayerStats))
        assert(listsMatch(ballStatus, listOf(
            BallStatus.ON_TABLE,
            BallStatus.DEAD,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE
        )))
    }

    @Test
    fun testNineThenFourOnBreakInning() {
        val inningString = nineThenFourOnBreakInning
        ballStatus = onBallStatus(7).toMutableList()
        val inning = Inning(inningString, PlayerTurn.PLAYER1, ShotCondition.NORMAL, ballStatus, rackList)

        setScore(8)
        expectedPlayerStats.shotsTaken = 5
        expectedPlayerStats.achievements.earlyNine = 1
        expectedPlayerStats.achievements.pointsOnBreak[4] = 1
        expectedPlayerStats.achievements.nines = 1

        assert(inning.shotCondition == ShotCondition.NORMAL)
        assert(inning.successfulDefense == 0)
        assert(inning.leftOnTable == 1)
        assert(playerStatsMatch(inning.inningPlayerStats, expectedPlayerStats))
        assert(listsMatch(ballStatus, listOf(
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.ON_TABLE,
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.PLAYER1
        )))
    }

    @Test
    fun testImmediateEclipseInning() {
        val inningString = immediateEclipseInning
        ballStatus = onBallStatus(3).toMutableList()
        val inning = Inning(inningString, PlayerTurn.PLAYER1, ShotCondition.NORMAL, ballStatus, rackList)

        expectedPlayerStats.shotsTaken = 1
        expectedPlayerStats.achievements.eclipse = 1

        assert(inning.shotCondition == ShotCondition.ECLIPSE)
        assert(inning.successfulDefense == 0)
        assert(inning.leftOnTable == 0)
        assert(playerStatsMatch(inning.inningPlayerStats, expectedPlayerStats))
        assert(listsMatch(ballStatus, listOf(
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE
        )))
    }

    @Test
    fun testEclipseReturnInning() {
        val inningString = immediateEclipseInning
        ballStatus = onBallStatus(3).toMutableList()
        val inning = Inning(inningString, PlayerTurn.PLAYER1, ShotCondition.INTENTIONAL_ECLIPSE, ballStatus, rackList)

        expectedPlayerStats.shotsTaken = 1
        expectedPlayerStats.achievements.eclipse = 1
        expectedPlayerStats.achievements.eclipseEscape = 1
        expectedPlayerStats.achievements.eclipseReturn = 1

        assert(inning.shotCondition == ShotCondition.ECLIPSE)
        assert(inning.successfulDefense == 0)
        assert(inning.leftOnTable == 0)
        assert(playerStatsMatch(inning.inningPlayerStats, expectedPlayerStats))
        assert(listsMatch(ballStatus, listOf(
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE
        )))
    }

    @Test
    fun testEclipseAfterBallInHandInning() {
        val inningString = immediateEclipseInning
        ballStatus = onBallStatus(3).toMutableList()
        val inning = Inning(inningString, PlayerTurn.PLAYER1, ShotCondition.BALL_IN_HAND, ballStatus, rackList)

        expectedPlayerStats.shotsTaken = 1
        expectedPlayerStats.achievements.eclipse = 1
        expectedPlayerStats.achievements.ballInHandMiss = 1

        assert(inning.shotCondition == ShotCondition.ECLIPSE)
        assert(inning.successfulDefense == 0)
        assert(inning.leftOnTable == 0)
        assert(playerStatsMatch(inning.inningPlayerStats, expectedPlayerStats))
        assert(listsMatch(ballStatus, listOf(
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE
        )))
    }

    @Test
    fun testMultipleSelfEclipsesInning() {
        val inningString = multipleSelfEclipsesInning
        ballStatus = onBallStatus(2).toMutableList()
        val inning = Inning(inningString, PlayerTurn.PLAYER1, ShotCondition.NORMAL, ballStatus, rackList)

        setScore(4)
        expectedPlayerStats.shotsTaken = 5
        expectedPlayerStats.achievements.selfEclipse = 4
        expectedPlayerStats.achievements.selfEclipseEscape = 4
        expectedPlayerStats.achievements.selfEclipsePocket = 3

        assert(inning.shotCondition == ShotCondition.NORMAL)
        assert(inning.successfulDefense == 0)
        assert(inning.leftOnTable == 0)
        assert(playerStatsMatch(inning.inningPlayerStats, expectedPlayerStats))
        assert(listsMatch(ballStatus, listOf(
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE
        )))
    }

    @Test
    fun testMultipleSelfEclipsesAfterEclipseInning() {
        val inningString = multipleSelfEclipsesInning
        ballStatus = onBallStatus(2).toMutableList()
        val inning = Inning(inningString, PlayerTurn.PLAYER1, ShotCondition.INTENTIONAL_ECLIPSE, ballStatus, rackList)

        setScore(4)
        expectedPlayerStats.shotsTaken = 5
        expectedPlayerStats.achievements.selfEclipse = 4
        expectedPlayerStats.achievements.selfEclipseEscape = 4
        expectedPlayerStats.achievements.selfEclipsePocket = 3
        expectedPlayerStats.achievements.eclipseEscape = 1
        expectedPlayerStats.achievements.eclipsePocket = 1

        assert(inning.shotCondition == ShotCondition.NORMAL)
        assert(inning.successfulDefense == -1)
        assert(inning.leftOnTable == 0)
        assert(playerStatsMatch(inning.inningPlayerStats, expectedPlayerStats))
        assert(listsMatch(ballStatus, listOf(
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE
        )))
    }

    @Test
    fun testSelfEclipseReturnInning() {
        val inningString = selfEclipseReturnInning
        ballStatus = onBallStatus(4).toMutableList()
        val inning = Inning(inningString, PlayerTurn.PLAYER1, ShotCondition.NORMAL, ballStatus, rackList)

        setScore(2)
        expectedPlayerStats.achievements.selfEclipse = 1
        expectedPlayerStats.achievements.eclipse = 1
        expectedPlayerStats.achievements.selfEclipseEscape = 1
        expectedPlayerStats.achievements.selfEclipseReturn = 1
        expectedPlayerStats.shotsTaken = 3

        assert(inning.shotCondition == ShotCondition.ECLIPSE)
        assert(inning.successfulDefense == 0)
        assert(inning.leftOnTable == 0)
        assert(playerStatsMatch(inning.inningPlayerStats, expectedPlayerStats))
        assert(listsMatch(ballStatus, listOf(
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE
        )))
    }

    @Test
    fun testIllegalNineInning() {
        val inningString = illegalNineInning
        ballStatus = onBallStatus(5).toMutableList()
        val inning = Inning(inningString, PlayerTurn.PLAYER1, ShotCondition.NORMAL, ballStatus, rackList)

        setScore(2)
        expectedPlayerStats.fouls.scratch = 1
        expectedPlayerStats.achievements.illegalNine = 1
        expectedPlayerStats.shotsTaken = 3

        assert(inning.shotCondition == ShotCondition.BALL_IN_HAND)
        assert(inning.successfulDefense == 0)
        assert(inning.leftOnTable == 0)
        assert(playerStatsMatch(inning.inningPlayerStats, expectedPlayerStats))
        assert(listsMatch(ballStatus, listOf(
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE
        )))
    }

    @Test
    fun testContinuedAfterNoPocketInning() {
        val inningString = continuedAfterNoPocketInning
        ballStatus = onBallStatus(3).toMutableList()
        val inning = Inning(inningString, PlayerTurn.PLAYER1, ShotCondition.NORMAL, ballStatus, rackList)

        setScore(3)
        expectedPlayerStats.shotsTaken = 4

        assert(inning.shotCondition == ShotCondition.NORMAL)
        assert(inning.successfulDefense == 0)
        assert(inning.leftOnTable == 0)
        assert(playerStatsMatch(inning.inningPlayerStats, expectedPlayerStats))
        assert(listsMatch(ballStatus, listOf(
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE
        )))
    }

    @Test
    fun testContinuedAfterFoulInning() {
        val inningString = continuedAfterFoulInning
        ballStatus = onBallStatus(6).toMutableList()
        val inning = Inning(inningString, PlayerTurn.PLAYER1, ShotCondition.NORMAL, ballStatus, rackList)

        setScore(1)
        expectedPlayerStats.fouls.jumpOffTable = 1
        expectedPlayerStats.deadBalls = 1
        expectedPlayerStats.shotsTaken = 2

        assert(inning.shotCondition == ShotCondition.BALL_IN_HAND)
        assert(inning.successfulDefense == 0)
        assert(inning.leftOnTable == 0)
        assert(playerStatsMatch(inning.inningPlayerStats, expectedPlayerStats))
        assert(listsMatch(ballStatus, listOf(
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.DEAD,
            BallStatus.ON_TABLE
        )))
    }

    @Test
    fun testbadBreakInning() {
        val inningString = badBreakInning
        ballStatus = onBallStatus(1).toMutableList()
        val inning = Inning(inningString, PlayerTurn.PLAYER1, ShotCondition.NORMAL, ballStatus, rackList)

        setScore(3)
        expectedPlayerStats.fouls.badBreak = 2
        expectedPlayerStats.shotsTaken = 6
        expectedPlayerStats.achievements.pointsOnBreak[1] = 1

        assert(inning.shotCondition == ShotCondition.NORMAL)
        assert(inning.successfulDefense == 0)
        assert(inning.leftOnTable == 0)
        assert(playerStatsMatch(inning.inningPlayerStats, expectedPlayerStats))
        assert(listsMatch(ballStatus, listOf(
            BallStatus.PLAYER1,
            BallStatus.PLAYER1,
            BallStatus.ON_TABLE,
            BallStatus.PLAYER1,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE
        )))
    }

    @Test
    fun testBadBreakScratchInning() {
        val inningString = badBreakScratchInning
        ballStatus = onBallStatus(1).toMutableList()
        val inning = Inning(inningString, PlayerTurn.PLAYER1, ShotCondition.BREAK, ballStatus, rackList)

        expectedPlayerStats.fouls.badBreak = 1
        expectedPlayerStats.fouls.scratch = 1
        expectedPlayerStats.achievements.scratchOnBreak = 1
        expectedPlayerStats.shotsTaken = 1

        assert(inning.shotCondition == ShotCondition.BREAK)
        assert(inning.successfulDefense == 0)
        assert(inning.leftOnTable == 0)
        assert(playerStatsMatch(inning.inningPlayerStats, expectedPlayerStats))
        assert(listsMatch(ballStatus, listOf(
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE
        )))
    }

    @Test
    fun testBadBreakScratchAfterNineInning() {
        val inningString = badBreakScratchAfterNineInning
        ballStatus = nineNotPerfect.toMutableList()
        val inning = Inning(inningString, PlayerTurn.PLAYER1, ShotCondition.NORMAL, ballStatus, rackList)

        setScore(2)
        expectedPlayerStats.fouls.badBreak = 1
        expectedPlayerStats.fouls.jumpOffTable = 1
        expectedPlayerStats.achievements.scratchOnBreak = 1
        expectedPlayerStats.shotsTaken = 2
        expectedPlayerStats.achievements.nines = 1

        assert(inning.shotCondition == ShotCondition.BREAK)
        assert(inning.successfulDefense == 0)
        assert(inning.leftOnTable == 0)
        assert(playerStatsMatch(inning.inningPlayerStats, expectedPlayerStats))
        assert(listsMatch(ballStatus, listOf(
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE
        )))
    }

    @Test
    fun testContinuedAfterBadBreakScratchInning() {
        val inningString = continuedAfterBadBreakScratchInning
        ballStatus = eightNotPerfect.toMutableList()
        val inning = Inning(inningString, PlayerTurn.PLAYER1, ShotCondition.NORMAL, ballStatus, rackList)

        setScore(3)
        expectedPlayerStats.fouls.badBreak = 1
        expectedPlayerStats.fouls.scratch = 1
        expectedPlayerStats.achievements.scratchOnBreak = 1
        expectedPlayerStats.shotsTaken = 3
        expectedPlayerStats.achievements.nines = 1

        assert(inning.shotCondition == ShotCondition.BREAK)
        assert(inning.successfulDefense == 0)
        assert(inning.leftOnTable == 0)
        assert(playerStatsMatch(inning.inningPlayerStats, expectedPlayerStats))
        assert(listsMatch(ballStatus, listOf(
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE,
            BallStatus.ON_TABLE
        )))
    }

    private fun setScore(shotScore: Int) {
        expectedPlayerStats.score = shotScore
        expectedPlayerStats.streaks.streak = shotScore
        expectedPlayerStats.streaks.turnStreak = shotScore
        expectedPlayerStats.streaks.currentStreak = shotScore
    }
}