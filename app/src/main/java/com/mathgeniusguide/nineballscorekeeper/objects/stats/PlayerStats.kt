package com.mathgeniusguide.nineballscorekeeper.objects.stats

class PlayerStats {
    var score = 0
    var deadBalls = 0
    var shotsTaken = 0
    val streaks = StreakStats()
    val fouls = FoulStats()
    val achievements = AchievementStats()

    fun addPlayerStats(playerStats: PlayerStats) {
        score += playerStats.score
        deadBalls += playerStats.deadBalls
        shotsTaken += playerStats.shotsTaken
        streaks.addStreakStats(playerStats.streaks)
        fouls.addFoulStats(playerStats.fouls)
        achievements.addAchievementStats(playerStats.achievements)
    }
}