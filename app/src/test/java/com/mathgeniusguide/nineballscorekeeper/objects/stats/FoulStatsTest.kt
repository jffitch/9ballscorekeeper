package com.mathgeniusguide.nineballscorekeeper.objects.stats

import org.junit.Before
import org.junit.Test

class FoulStatsTest {
    val foulStats1 = FoulStats()
    val foulStats2 = FoulStats()
    val foulStats3 = FoulStats()

    @Before
    fun before() {
        foulStats1.badBreak = 2
        foulStats1.scratch = 3
        foulStats1.wrongBallFirst = 3
        foulStats1.miss = 2
        foulStats1.jumpOffTable = 1
        foulStats1.noRail = 1
        foulStats1.other = 2

        foulStats2.badBreak = 3
        foulStats2.scratch = 4
        foulStats2.wrongBallFirst = 1
        foulStats2.miss = 2
        foulStats2.jumpOffTable = 0
        foulStats2.noRail = 2
        foulStats2.other = 1

        foulStats3.badBreak = 1
        foulStats3.scratch = 5
        foulStats3.wrongBallFirst = 6
        foulStats3.miss = 4
        foulStats3.jumpOffTable = 1
        foulStats3.noRail = 3
        foulStats3.other = 0
    }

    @Test
    fun testAddFoulStats1And2() {
        foulStats1.addFoulStats(foulStats2)
        assert(foulStats1.badBreak == 5)
        assert(foulStats1.scratch == 7)
        assert(foulStats1.wrongBallFirst == 4)
        assert(foulStats1.miss == 4)
        assert(foulStats1.jumpOffTable == 1)
        assert(foulStats1.noRail == 3)
        assert(foulStats1.other == 3)
    }

    @Test
    fun testAddFoulStats1And3() {
        foulStats1.addFoulStats(foulStats3)
        assert(foulStats1.badBreak == 3)
        assert(foulStats1.scratch == 8)
        assert(foulStats1.wrongBallFirst == 9)
        assert(foulStats1.miss == 6)
        assert(foulStats1.jumpOffTable == 2)
        assert(foulStats1.noRail == 4)
        assert(foulStats1.other == 2)
    }

    @Test
    fun testAddFoulStats2And3() {
        foulStats2.addFoulStats(foulStats3)
        assert(foulStats2.badBreak == 4)
        assert(foulStats2.scratch == 9)
        assert(foulStats2.wrongBallFirst == 7)
        assert(foulStats2.miss == 6)
        assert(foulStats2.jumpOffTable == 1)
        assert(foulStats2.noRail == 5)
        assert(foulStats2.other == 1)
    }

    @Test
    fun totalFoulStats1() {
        assert(foulStats1.total() == 12)
    }

    @Test
    fun totalFoulStats2() {
        assert(foulStats2.total() == 10)
    }

    @Test
    fun totalFoulStats3() {
        assert(foulStats3.total() == 19)
    }
}