package com.mathgeniusguide.nineballscorekeeper

import com.mathgeniusguide.nineballscorekeeper.enums.BallStatus
import com.mathgeniusguide.nineballscorekeeper.objects.stats.AchievementStats
import com.mathgeniusguide.nineballscorekeeper.objects.stats.FoulStats
import com.mathgeniusguide.nineballscorekeeper.objects.stats.GameStats
import com.mathgeniusguide.nineballscorekeeper.objects.stats.PlayerStats
import com.mathgeniusguide.nineballscorekeeper.objects.stats.StreakStats
import com.mathgeniusguide.nineballscorekeeper.objects.stats.SuccessStats

val gameString1 =
    "Player 1:Pawn;Player 2:Queen;Location:Home;Date:2023/12/26|4'',';17'2'',3'5K';9'38'1'2'',';',';4'de',W';5'6e'7'9'7e'2W',1K';3'4'5'',6'';8'9'59'18'2'3'4'6'5'7'9'2e'',1'3'4'e';',5'';6'7'8'',9'1'';2'd',3'';4'de',5'';de',M';6'7'8'9'"
val gameString2 =
    "Player 1:Pawn;Player 2:Queen;Location:Yale Billiards;Date:2024/01/17|9'',';1'2'e',7W';3'4'd',';',';',5'';6'7'',8'K';9'24'1'3'',5'9K';6'',';',';7'',';',';8'9'49'138'2e'W',4'5'6'';',7'';8'',9'3e'W';1'2'd',4'';5'',M';6'7'8'9'7'1'2'',3'9'46'"
val gameString3 =
    "Player 1:Pawn;Player 2:Katy;Location:Home;Date:2024/02/03|',';1'',';2'3'',4'';5'6'7'8'9'34'de',7W';1'2'',';',';5'de',W';6'7'',8K';9'1'2'3'd',';4'd',5'';6e'7'',';8'9'3'1'',2'e';e',W';4'5'de',8W';6'',K';7'9'478'1'6'2'',';',';',3'5'';9'4'1'2'9'7''"
val gameJustStarted = "Player 1:Pawn;Player 2:Queen;Location:Home;Date:2024/04/11|"

val customStartingBallStatus = listOf(
    BallStatus.PLAYER1,
    BallStatus.SCORED_THIS_TURN,
    BallStatus.DEAD,
    BallStatus.ON_TABLE,
    BallStatus.ON_TABLE,
    BallStatus.DEAD,
    BallStatus.ON_TABLE,
    BallStatus.PLAYER1
)
val almostBreakAndRunBallStatusNineLeft = listOf(
    BallStatus.SCORED_THIS_TURN,
    BallStatus.SCORED_THIS_TURN,
    BallStatus.SCORED_THIS_TURN,
    BallStatus.SCORED_THIS_TURN,
    BallStatus.SCORED_THIS_TURN,
    BallStatus.SCORED_THIS_TURN,
    BallStatus.SCORED_THIS_TURN,
    BallStatus.SCORED_THIS_TURN,
)
val almostBreakAndRunBallStatusOneBallLeft = listOf(
    BallStatus.SCORED_THIS_TURN,
    BallStatus.SCORED_THIS_TURN,
    BallStatus.SCORED_THIS_TURN,
    BallStatus.SCORED_THIS_TURN,
    BallStatus.SCORED_THIS_TURN,
    BallStatus.SCORED_THIS_TURN,
    BallStatus.ON_TABLE,
    BallStatus.SCORED_THIS_TURN
)
val ninePerfect = listOf(
    BallStatus.SCORED_THIS_TURN,
    BallStatus.SCORED_THIS_TURN,
    BallStatus.SCORED_THIS_TURN,
    BallStatus.PLAYER1,
    BallStatus.PLAYER1,
    BallStatus.SCORED_THIS_TURN,
    BallStatus.PLAYER1,
    BallStatus.SCORED_THIS_TURN
)
val nineNotPerfect = listOf(
    BallStatus.SCORED_THIS_TURN,
    BallStatus.PLAYER2,
    BallStatus.PLAYER2,
    BallStatus.PLAYER1,
    BallStatus.PLAYER1,
    BallStatus.SCORED_THIS_TURN,
    BallStatus.PLAYER1,
    BallStatus.SCORED_THIS_TURN
)

val eightNotPerfect = listOf(
    BallStatus.SCORED_THIS_TURN,
    BallStatus.PLAYER2,
    BallStatus.PLAYER2,
    BallStatus.PLAYER1,
    BallStatus.PLAYER1,
    BallStatus.SCORED_THIS_TURN,
    BallStatus.PLAYER1,
    BallStatus.ON_TABLE,
)

fun onBallStatus(ball: Int): List<BallStatus> {
    val ballStatus = mutableListOf<BallStatus>()
    for (i in 1..8) {
        ballStatus.add(if (ball <= i) BallStatus.ON_TABLE else BallStatus.PLAYER1)
    }
    return ballStatus
}

val dryInning = "'"
val onePointInning = "1''"
val twoPointsInning = "1'2''"
val twoInOneShotInning = "12''"
val intentionalEclipseInning = "4'de'"
val defenseInning = "2'3'd'"
val selfDefenseInning = "3'4d'5''"
val foulOnDefenseInning = "3'4'dR'"
val ballInHandAfterThreePointsInning = "5'6'7'8W'"
val ballInHandAfterOnePointInning = "7'8K'"
val ballInHandInning = "M'"
val breakAndRunThenMissInning = "38'1'2'4'6'5'7'9''"
val breakAndRunThenContinueInning = "3'9'4'1'2'3'57'6'8'9'27'1'3'4''"
val backToBackToBackBreakAndRunInning = "7'8'9'6'1'2'3'4'5'7'8'9'34'1'2'5'6'7'8'9'18'2'3'4'7'5'6'9'6'1'de'"
val perfectRackInning = "6'7'8'9''"
val earlyNineInning = "1'2'3'4'5'6'7'9'1''"
val nineThenScratchOnBreakInning = "8'9'2K'"
val nineThenFourOnBreakInning = "7'9'1458'2''"
val immediateEclipseInning = "e'"
val multipleSelfEclipsesInning = "2e'3e'4e'5e''"
val selfEclipseReturnInning = "4'5e'e'"
val illegalNineInning = "5'6'9K'"
val continuedAfterNoPocketInning = "3'4'5''6'7''"
val continuedAfterFoulInning = "6'7P'8''"
val badBreakInning = "B'B'4'1'2''"
val badBreakScratchInning = "BK'"
val badBreakScratchAfterNineInning = "9'BP'"
val continuedAfterBadBreakScratchInning = "8'9'BK'2'1'3''"

fun pairListsMatch(list1: List<Pair<*, *>>, list2: List<Pair<*, *>>): Boolean {
    if (list1.size != list2.size) {
        return false
    }
    for (i in list1.indices) {
        if (list1[i].first != list2[i].first || list1[i].second != list2[i].second) {
            return false
        }
    }
    return true
}

fun listsMatch(list1: List<*>, list2: List<*>): Boolean {
    if (list1.size != list2.size) {
        return false
    }
    for (i in list1.indices) {
        if (list1[i] != list2[i]) {
            return false
        }
    }
    return true
}

fun mapsMatch(map1: Map<String, *>, map2: Map<String, *>): Boolean {
    val map1entries = map1.entries.sortedBy { it.key }
    val map2entries = map2.entries.sortedBy { it.key }
    if (map1entries.size != map2entries.size) {
        return false
    }
    for (i in map1entries.indices) {
        if (map1entries[i].key != map2entries[i].key || map1entries[i].value != map2entries[i].value) {
            return false
        }
    }
    return true
}

fun achievementStatsMatch(
    achievementStats1: AchievementStats,
    achievementStats2: AchievementStats
): Boolean {
    return achievementStats1.nineOnBreak == achievementStats2.nineOnBreak && achievementStats1.breakAndRun == achievementStats2.breakAndRun && achievementStats1.perfectRack == achievementStats2.perfectRack && achievementStats1.scratchOnBreak == achievementStats2.scratchOnBreak && achievementStats1.nines == achievementStats2.nines && achievementStats1.earlyNine == achievementStats2.earlyNine && achievementStats1.illegalNine == achievementStats2.illegalNine && achievementStats1.eclipse == achievementStats2.eclipse && achievementStats1.selfEclipse == achievementStats2.selfEclipse && achievementStats1.intentionalEclipse == achievementStats2.intentionalEclipse && achievementStats1.eclipseReturn == achievementStats2.eclipseReturn && achievementStats1.eclipseEscape == achievementStats2.eclipseEscape && achievementStats1.eclipsePocket == achievementStats2.eclipsePocket && achievementStats1.selfEclipseReturn == achievementStats2.selfEclipseReturn && achievementStats1.selfEclipseEscape == achievementStats2.selfEclipseEscape && achievementStats1.selfEclipsePocket == achievementStats2.selfEclipsePocket && achievementStats1.ballInHandMiss == achievementStats2.ballInHandMiss && achievementStats1.ballInHandReturn == achievementStats2.ballInHandReturn && achievementStats1.lucky == achievementStats2.lucky && listsMatch(achievementStats1.pointsOnBreak, achievementStats2.pointsOnBreak) && successStatsMatch(achievementStats1.defense, achievementStats2.defense) && successStatsMatch(achievementStats1.bank, achievementStats2.bank) && achievementStats1.miscue == achievementStats2.miscue && achievementStats1.miscueHit == achievementStats2.miscueHit && achievementStats1.miscuePocket == achievementStats2.miscuePocket && achievementStats1.miscueBreak == achievementStats2.miscueBreak
}

fun successStatsMatch(successStats1: SuccessStats, successStats2: SuccessStats): Boolean {
    return successStats1.success == successStats2.success && successStats1.failure == successStats2.failure && successStats1.total == successStats2.total
}

fun foulStatsMatch(
    foulStats1: FoulStats,
    foulStats2: FoulStats
): Boolean {
    return foulStats1.scratch == foulStats2.scratch && foulStats1.miss == foulStats2.miss && foulStats1.wrongBallFirst == foulStats2.wrongBallFirst && foulStats1.noRail == foulStats2.noRail && foulStats1.jumpOffTable == foulStats2.jumpOffTable && foulStats1.badBreak == foulStats2.badBreak && foulStats1.other == foulStats2.other
}

fun streakStatsMatch(
    streakStats1: StreakStats,
    streakStats2: StreakStats
): Boolean {
    return streakStats1.streak == streakStats2.streak && streakStats1.turnStreak == streakStats2.turnStreak && streakStats1.currentStreak == streakStats2.currentStreak && streakStats1.currentTurnStreak == streakStats2.currentTurnStreak
}

fun playerStatsMatch(
    playerStats1: PlayerStats,
    playerStats2: PlayerStats
): Boolean {
    return playerStats1.score == playerStats2.score && playerStats1.deadBalls == playerStats2.deadBalls && playerStats1.shotsTaken == playerStats2.shotsTaken && foulStatsMatch(playerStats1.fouls, playerStats2.fouls) && streakStatsMatch(playerStats1.streaks, playerStats2.streaks) && achievementStatsMatch(playerStats1.achievements, playerStats2.achievements)
}

fun gameStatsMatch(
    gameStats1: GameStats,
    gameStats2: GameStats
): Boolean {
    return gameStats1.innings == gameStats2.innings && playerStatsMatch(gameStats1.player1Stats, gameStats2.player1Stats) && playerStatsMatch(gameStats1.player2Stats, gameStats2.player2Stats) && gameStats1.playerTurn == gameStats2.playerTurn && gameStats1.deadBalls == gameStats2.deadBalls
}