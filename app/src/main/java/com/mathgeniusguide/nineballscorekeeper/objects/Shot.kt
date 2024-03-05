package com.mathgeniusguide.nineballscorekeeper.objects

import com.mathgeniusguide.nineballscorekeeper.enums.BallStatus
import com.mathgeniusguide.nineballscorekeeper.enums.PlayerTurn
import com.mathgeniusguide.nineballscorekeeper.enums.ShotCondition
import com.mathgeniusguide.nineballscorekeeper.objects.stats.PlayerStats

class Shot(string: String, playerTurn: PlayerTurn, startCondition: ShotCondition, ballStatus: Array<BallStatus>) {
    private val foulRegex = "[KMWRPO]".toRegex()
    private val isFoul = foulRegex.containsMatchIn(string)
    private val isScratch = string.contains('K') || string.contains('P')
    private val isBadBreak = string.contains('B')
    private var points = calculatePoints(string, isFoul, ballStatus)
    private val isDefense = string.contains('d')
    private val isEclipse = string.contains('e')
    private val isIntentionalEclipse = isDefense && isEclipse && points == 0
    private val isLucky = string.contains('l')
    private val isBank = string.contains('b')
    private val isMiscue = string.contains('m')
    private val isTimeOut = string.contains('t')
    val isStalemate = string.contains('S')
    private val isNine = string.contains('9')
    val isRackEnd = isStalemate || (isNine && !isFoul)

    val successfulDefense = if (startCondition == ShotCondition.DEFENSE || startCondition == ShotCondition.INTENTIONAL_ECLIPSE)
        when {
            isFoul -> 1
            points != 0 -> -1
            else -> 0
        } else 0
    val shotCondition = when {
        isBadBreak || isRackEnd -> ShotCondition.BREAK
        isFoul -> ShotCondition.BALL_IN_HAND
        isIntentionalEclipse -> ShotCondition.INTENTIONAL_ECLIPSE
        isEclipse && points == 0 -> ShotCondition.ECLIPSE
        isEclipse && points != 0 -> ShotCondition.SELF_ECLIPSE
        isDefense && points == 0 -> ShotCondition.DEFENSE
        else -> ShotCondition.NORMAL
    }
    val shotPlayerStats = PlayerStats()
    val isTurnEnd = if (isBadBreak) isScratch else (points == 0 || isFoul)

    init {
        with(shotPlayerStats) {
            score = if (!isFoul && !isBadBreak) points else 0
            deadBalls = if (isFoul && !isBadBreak) points else 0
            shotsTaken = 1

            streaks.currentStreak = score
            streaks.currentTurnStreak = score

            fouls.badBreak = if (isBadBreak) 1 else 0
            fouls.miss = if (string.contains('M')) 1 else 0
            fouls.noRail = if (string.contains('R')) 1 else 0
            fouls.jumpOffTable = if (string.contains('P')) 1 else 0
            fouls.wrongBallFirst = if (string.contains('W')) 1 else 0
            fouls.scratch = if (string.contains('K')) 1 else 0
            fouls.other = if (string.contains('O')) 1 else 0

            achievements.nines = if (isNine && !isFoul) 1 else 0
            achievements.nineOnBreak = if (isNine && !isFoul && startCondition == ShotCondition.BREAK) 1 else 0
            achievements.breakAndRun = if (isNine && !isFoul && ballStatus.all { it == BallStatus.SCORED_THIS_TURN }) 1 else 0
            achievements.perfectRack = if (isNine && !isFoul && ballStatus.dropLast(1).all { it == BallStatus.SCORED_THIS_TURN || it == playerTurn.toBallStatus() }) 1 else 0
            achievements.earlyNine = if (isNine && !isFoul && startCondition != ShotCondition.BREAK && ballStatus.any { it == BallStatus.ON_TABLE }) 1 else 0
            achievements.pointsOnBreak[if (isFoul) 0 else points] += if (startCondition == ShotCondition.BREAK && !isBadBreak) 1 else 0
            achievements.scratchOnBreak = if (isScratch && startCondition == ShotCondition.BREAK) 1 else 0
            achievements.illegalNine = if (isNine && isFoul) 1 else 0
            achievements.defense.failure = if (isDefense && (points != 0 || isFoul)) 1 else 0
            achievements.defense.total = if (isDefense) 1 else 0
            achievements.eclipse = if (isEclipse && points == 0) 1 else 0
            achievements.selfEclipse = if (isEclipse && points != 0) 1 else 0
            achievements.intentionalEclipse = if (isIntentionalEclipse) 1 else 0
            achievements.eclipseReturn = if (isEclipse && points == 0 && (startCondition == ShotCondition.ECLIPSE || startCondition == ShotCondition.INTENTIONAL_ECLIPSE)) 1 else 0
            achievements.eclipseEscape = if (!isFoul && (startCondition == ShotCondition.ECLIPSE || startCondition == ShotCondition.INTENTIONAL_ECLIPSE)) 1 else 0
            achievements.eclipsePocket = if (!isFoul && points != 0 && (startCondition == ShotCondition.ECLIPSE || startCondition == ShotCondition.INTENTIONAL_ECLIPSE)) 1 else 0
            achievements.selfEclipseReturn = if (isEclipse && points == 0 && startCondition == ShotCondition.SELF_ECLIPSE) 1 else 0
            achievements.selfEclipseEscape = if (!isFoul && startCondition == ShotCondition.SELF_ECLIPSE) 1 else 0
            achievements.selfEclipsePocket = if (!isFoul && points != 0 && startCondition == ShotCondition.SELF_ECLIPSE) 1 else 0
            achievements.ballInHandMiss = if (points == 0 && !isFoul && startCondition == ShotCondition.BALL_IN_HAND) 1 else 0
            achievements.ballInHandReturn = if (isFoul && startCondition == ShotCondition.BALL_IN_HAND) 1 else 0
            achievements.lucky = if (isLucky) score else 0
            achievements.bank.success = if (isBank && !isFoul && points != 0) 1 else 0
            achievements.bank.failure = if (isBank && (isFoul || points == 0)) 1 else 0
            achievements.bank.total = if (isBank) 1 else 0
            achievements.miscue = if (isMiscue) 1 else 0
            achievements.miscueHit = if (isMiscue && !isFoul && !isBadBreak) 1 else 0
            achievements.miscuePocket = if (isMiscue && score != 0) 1 else 0
            achievements.miscueBreak = if (isMiscue && startCondition == ShotCondition.BREAK) 1 else 0
            achievements.timeOut = if (isTimeOut) 1 else 0
            achievements.timeOutDefense = if (isTimeOut && !isFoul && isDefense && points == 0) 1 else 0
            achievements.timeOutPocket = if (isTimeOut && !isFoul && points != 0) 1 else 0
            achievements.timeOutFoul = if (isTimeOut && isFoul) 1 else 0
            achievements.timeOutMiss = if (isTimeOut && !isFoul && !isDefense && points == 0) 1 else 0
        }
    }

    private fun calculatePoints(string: String, isFoul: Boolean, ballStatus: Array<BallStatus>): Int {
        var currentPoints = 0
        for (i in listOf('1', '2', '3', '4', '5', '6', '7', '8')) {
            val index = i - '0' - 1
            if (string.contains(i) && ballStatus[index] == BallStatus.ON_TABLE) {
                currentPoints += 1
                ballStatus[index] = if (isFoul) BallStatus.DEAD else BallStatus.SCORED_THIS_TURN
            }
        }
        if (!isFoul && string.contains('9')) {
            currentPoints += 2
        }
        return currentPoints
    }
}