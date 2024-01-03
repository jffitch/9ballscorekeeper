package com.mathgeniusguide.nineballscorekeeper.objects.stats

import org.junit.Test

class StreakStatsTest {
    @Test
    fun testAddStreakStats_NewStreak() {
        val streakStats1 = StreakStats()
        streakStats1.streak = 11
        streakStats1.turnStreak = 8
        streakStats1.currentStreak = 7
        streakStats1.currentTurnStreak = 3
        val streakStats2 = StreakStats()
        streakStats2.streak = 15
        streakStats2.turnStreak = 10
        streakStats2.currentStreak = 2
        streakStats2.currentTurnStreak = 4
        streakStats1.addStreakStats(streakStats2)
        assert(streakStats1.streak == 15)
        assert(streakStats1.turnStreak == 10)
        assert(streakStats1.currentStreak == 9)
        assert(streakStats1.currentTurnStreak == 7)
    }
    @Test
    fun testAddStreakStats_NewStreakFromCurrentStreak() {
        val streakStats1 = StreakStats()
        streakStats1.streak = 11
        streakStats1.turnStreak = 8
        streakStats1.currentStreak = 10
        streakStats1.currentTurnStreak = 3
        val streakStats2 = StreakStats()
        streakStats2.streak = 7
        streakStats2.turnStreak = 5
        streakStats2.currentStreak = 7
        streakStats2.currentTurnStreak = 6
        streakStats1.addStreakStats(streakStats2)
        assert(streakStats1.streak == 17)
        assert(streakStats1.turnStreak == 9)
        assert(streakStats1.currentStreak == 17)
        assert(streakStats1.currentTurnStreak == 9)
    }
    @Test
    fun testAddStreakStats_NoNewStreak() {
        val streakStats1 = StreakStats()
        streakStats1.streak = 11
        streakStats1.turnStreak = 8
        streakStats1.currentStreak = 5
        streakStats1.currentTurnStreak = 4
        val streakStats2 = StreakStats()
        streakStats2.streak = 7
        streakStats2.turnStreak = 5
        streakStats2.currentStreak = 3
        streakStats2.currentTurnStreak = 1
        streakStats1.addStreakStats(streakStats2)
        assert(streakStats1.streak == 11)
        assert(streakStats1.turnStreak == 8)
        assert(streakStats1.currentStreak == 8)
        assert(streakStats1.currentTurnStreak == 5)
    }
}