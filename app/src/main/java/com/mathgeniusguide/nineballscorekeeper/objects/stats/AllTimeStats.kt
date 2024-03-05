package com.mathgeniusguide.nineballscorekeeper.objects.stats

import com.mathgeniusguide.nineballscorekeeper.objects.GameDetails
import java.lang.Integer.max
import java.lang.Double.max
import java.lang.Math.min

class AllTimeStats(val playerName: String) {
    var gamesPlayed = 0

    var maxScore = -1
    var minScore = -1
    var totalScore = 0
    var maxStreak = -1
    var minStreak = -1
    var maxTurnStreak = -1
    var minTurnStreak = -1
    var totalStreak = 0
    var totalTurnStreak = 0
    var minInnings = -1
    var maxInnings = -1
    var totalInnings = 0

    var maxNines = -1
    var minNines = -1
    var totalNines = 0
    var totalPerfectRack = 0
    var totalBreakRun = 0
    var totalNineBreak = 0

    var score_0_9 = 0
    var score_10_19 = 0
    var score_20_29 = 0
    var score_30_39 = 0
    var score_40_49 = 0
    var score_50_59 = 0
    var score_60_69 = 0
    var score_70_79 = 0
    var score_80_89 = 0
    var score_90_99 = 0
    var score_100_plus = 0

    var maxFouls = -1
    var minFouls = -1
    var totalFouls = 0
    var minFoulPointRatio = (-1).toDouble()
    var maxFoulPointRatio = 0.toDouble()
    var minFoulInningRatio = (-1).toDouble()
    var maxFoulInningRatio = 0.toDouble()
    var moreFoulsThanPoints = 0

    fun add(gameDetails: GameDetails) {
        val player = getPlayer(gameDetails)
        if (player != null) {
            gamesPlayed += 1
            totalScore += player.score
            totalStreak += player.streaks.streak
            totalTurnStreak += player.streaks.turnStreak
            totalInnings += gameDetails.gameStats.innings
            totalFouls += player.fouls.total()

            totalNines += player.achievements.nines
            totalBreakRun += player.achievements.breakAndRun
            totalPerfectRack += player.achievements.perfectRack
            totalNineBreak += player.achievements.nineOnBreak

            score_0_9 += if (player.score < 10) 1 else 0
            score_10_19 += if (player.score in 10..19) 1 else 0
            score_20_29 += if (player.score in 20..29) 1 else 0
            score_30_39 += if (player.score in 30..39) 1 else 0
            score_40_49 += if (player.score in 40..49) 1 else 0
            score_50_59 += if (player.score in 50..59) 1 else 0
            score_60_69 += if (player.score in 60..69) 1 else 0
            score_70_79 += if (player.score in 70..79) 1 else 0
            score_80_89 += if (player.score in 80..89) 1 else 0
            score_90_99 += if (player.score in 90..99) 1 else 0
            score_100_plus += if (player.score >= 100) 1 else 0

            val foulInningRatio = player.fouls.total().toDouble() / gameDetails.gameStats.innings.toDouble()
            val foulPointRatio = player.fouls.total().toDouble() / player.score.toDouble()

            moreFoulsThanPoints += if (player.fouls.total() > player.score) 1 else 0
            maxScore = max(player.score, maxScore)
            maxFouls = max(player.fouls.total(), maxFouls)
            maxNines = max(player.achievements.nines, maxNines)
            maxInnings = max(gameDetails.gameStats.innings, maxInnings)
            maxStreak = max(player.streaks.streak, maxStreak)
            maxTurnStreak = max(player.streaks.turnStreak, maxTurnStreak)
            maxFoulInningRatio = max(foulInningRatio, maxFoulInningRatio)
            maxFoulPointRatio = max(foulPointRatio, maxFoulPointRatio)
            minScore = if (minScore == -1) player.score else min(player.score, minScore)
            minFouls = if (minFouls == -1) player.fouls.total() else min(player.fouls.total(), minFouls)
            minInnings = if (minInnings == -1) gameDetails.gameStats.innings else min(gameDetails.gameStats.innings, minInnings)
            minNines = if (minNines == -1) player.achievements.nines else min(player.achievements.nines, minNines)
            minStreak = if (minStreak == -1) player.streaks.streak else min(player.streaks.streak, minStreak)
            minTurnStreak = if (minTurnStreak == -1) player.streaks.turnStreak else min(player.streaks.turnStreak, minTurnStreak)
            minFoulInningRatio = if (minFoulInningRatio == -1.0) foulInningRatio else min(foulInningRatio, minFoulInningRatio)
            minFoulPointRatio = if (minFoulPointRatio == -1.0) foulPointRatio else min(foulPointRatio, minFoulPointRatio)
        }
    }

    private fun getPlayer(gameDetails: GameDetails): PlayerStats? {
        return when (playerName) {
            gameDetails.player1Name -> gameDetails.gameStats.player1Stats
            gameDetails.player2Name -> gameDetails.gameStats.player2Stats
            else -> null
        }
    }
}