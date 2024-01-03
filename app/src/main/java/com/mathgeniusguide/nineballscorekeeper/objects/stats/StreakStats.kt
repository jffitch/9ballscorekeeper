package com.mathgeniusguide.nineballscorekeeper.objects.stats

class StreakStats {
    var streak = 0
    var turnStreak = 0
    var currentStreak = 0
    var currentTurnStreak = 0

    fun addStreakStats(streakStats: StreakStats) {
        currentStreak += streakStats.currentStreak
        currentTurnStreak += streakStats.currentTurnStreak
        streak = maxOf(streak, streakStats.streak, currentStreak)
        turnStreak = maxOf(turnStreak, streakStats.turnStreak, currentTurnStreak)
    }
}