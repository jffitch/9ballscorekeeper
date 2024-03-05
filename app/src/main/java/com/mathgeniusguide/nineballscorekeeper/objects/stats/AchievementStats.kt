package com.mathgeniusguide.nineballscorekeeper.objects.stats

class AchievementStats {
    var nineOnBreak = 0
    var breakAndRun = 0
    var perfectRack = 0
    val pointsOnBreak = mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    var scratchOnBreak = 0
    var nines = 0
    var earlyNine = 0
    var illegalNine = 0
    val defense = SuccessStats()
    var eclipse = 0
    var selfEclipse = 0
    var intentionalEclipse = 0
    var eclipseReturn = 0
    var eclipseEscape = 0
    var eclipsePocket = 0
    var selfEclipseReturn = 0
    var selfEclipseEscape = 0
    var selfEclipsePocket = 0
    var ballInHandMiss = 0
    var ballInHandReturn = 0
    var lucky = 0
    val bank = SuccessStats()
    var miscue = 0
    var miscueHit = 0
    var miscuePocket = 0
    var miscueBreak = 0
    var timeOut = 0
    var timeOutDefense = 0
    var timeOutPocket = 0
    var timeOutFoul = 0
    var timeOutMiss = 0

    fun addAchievementStats(achievementStats: AchievementStats) {
        nineOnBreak += achievementStats.nineOnBreak
        breakAndRun += achievementStats.breakAndRun
        perfectRack += achievementStats.perfectRack
        for (i in pointsOnBreak.indices) {
            pointsOnBreak[i] += achievementStats.pointsOnBreak[i]
        }
        scratchOnBreak += achievementStats.scratchOnBreak
        nines += achievementStats.nines
        earlyNine += achievementStats.earlyNine
        illegalNine += achievementStats.illegalNine
        defense.addSuccessStats(achievementStats.defense)
        eclipse += achievementStats.eclipse
        selfEclipse += achievementStats.selfEclipse
        intentionalEclipse += achievementStats.intentionalEclipse
        eclipseReturn += achievementStats.eclipseReturn
        eclipseEscape += achievementStats.eclipseEscape
        eclipsePocket += achievementStats.eclipsePocket
        selfEclipseReturn += achievementStats.selfEclipseReturn
        selfEclipseEscape += achievementStats.selfEclipseEscape
        selfEclipsePocket += achievementStats.selfEclipsePocket
        ballInHandMiss += achievementStats.ballInHandMiss
        ballInHandReturn += achievementStats.ballInHandReturn
        lucky += achievementStats.lucky
        bank.addSuccessStats(achievementStats.bank)
        miscue += achievementStats.miscue
        miscueHit += achievementStats.miscueHit
        miscuePocket += achievementStats.miscuePocket
        miscueBreak += achievementStats.miscueBreak
        timeOut += achievementStats.timeOut
        timeOutDefense += achievementStats.timeOutDefense
        timeOutPocket += achievementStats.timeOutPocket
        timeOutFoul += achievementStats.timeOutFoul
        timeOutMiss += achievementStats.timeOutMiss
    }

    fun averagePointsOnBreak(): Double {
        var currentPoints = 0
        var currentBreaks = 0
        for (i in pointsOnBreak.indices) {
            currentBreaks += pointsOnBreak[i]
            currentPoints += i * pointsOnBreak[i]
        }
        return currentPoints.toDouble() / currentBreaks.toDouble()
    }
}