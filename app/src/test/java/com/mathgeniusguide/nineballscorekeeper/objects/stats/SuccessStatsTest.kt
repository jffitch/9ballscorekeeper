package com.mathgeniusguide.nineballscorekeeper.objects.stats

import org.junit.Test

class SuccessStatsTest{
    @Test
    fun testAddSuccessStats1() {
        val successStats1 = SuccessStats()
        successStats1.success = 4
        successStats1.failure = 5
        val successStats2 = SuccessStats()
        successStats2.success = 3
        successStats2.failure = 8
        successStats1.addSuccessStats(successStats2)
        assert(successStats1.success == 7)
        assert(successStats1.failure == 13)
    }
    @Test
    fun testAddSuccessStats2() {
        val successStats1 = SuccessStats()
        successStats1.success = 2
        successStats1.failure = 9
        val successStats2 = SuccessStats()
        successStats2.success = 7
        successStats2.failure = 1
        successStats1.addSuccessStats(successStats2)
        assert(successStats1.success == 9)
        assert(successStats1.failure == 10)
    }
    @Test
    fun testAddSuccessStats3() {
        val successStats1 = SuccessStats()
        successStats1.success = 11
        successStats1.failure = 4
        val successStats2 = SuccessStats()
        successStats2.success = 25
        successStats2.failure = 17
        successStats1.addSuccessStats(successStats2)
        assert(successStats1.success == 36)
        assert(successStats1.failure == 21)
    }
}