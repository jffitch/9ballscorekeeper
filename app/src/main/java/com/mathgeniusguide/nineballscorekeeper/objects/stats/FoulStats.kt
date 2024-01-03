package com.mathgeniusguide.nineballscorekeeper.objects.stats

class FoulStats {
    var scratch = 0
    var miss = 0
    var wrongBallFirst = 0
    var noRail = 0
    var jumpOffTable = 0
    var badBreak = 0
    var other = 0

    fun addFoulStats(foulStats: FoulStats) {
        scratch += foulStats.scratch
        miss += foulStats.miss
        wrongBallFirst += foulStats.wrongBallFirst
        noRail += foulStats.noRail
        jumpOffTable += foulStats.jumpOffTable
        badBreak += foulStats.badBreak
        other += foulStats.other
    }

    fun total() = scratch + miss + wrongBallFirst + noRail + jumpOffTable + other
}