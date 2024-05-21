package com.mathgeniusguide.nineballscorekeeper.util

import com.mathgeniusguide.nineballscorekeeper.objects.GameDetails
import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

val yyyymmdd = "yyyy/MM/dd"
val matchPointsArray = listOf(
    listOf(3, 4, 5, 6, 7, 9, 11, 14, 18),
    listOf(4, 6, 7, 9, 11, 13, 16, 20, 25),
    listOf(5, 8, 10, 12, 15, 18, 22, 27, 32),
    listOf(7, 9, 12, 15, 19, 23, 27, 33, 39),
    listOf(8, 11, 15, 19, 23, 28, 33, 40, 47),
    listOf(9, 13, 17, 22, 27, 32, 38, 46, 54),
    listOf(11, 15, 20, 25, 30, 37, 44, 53, 61),
    listOf(12, 17, 22, 28, 34, 41, 50, 59, 68),
    listOf(14, 19, 25, 31, 38, 46, 55, 65, 75)
)

fun getGameDetails(gameString: String): GameDetails {
    val descriptionString = gameString.substringBefore('|', "")
    val descriptionItems = descriptionString.split(';')
    val descriptionMap = mutableMapOf<String, String>()
    for (keyValue in descriptionItems) {
        if (keyValue.count { it == ':' } != 1) {
            continue
        }
        val key = keyValue.substringBefore(':')
        val value = keyValue.substringAfter(':')
        descriptionMap.put(key, value)
    }
    val inningsString = gameString.substringAfterLast('|')
    val inningsItems = inningsString.split(';')
    val inningsList = mutableListOf<Pair<String, String>>()
    for (pair in inningsItems) {
        val first = pair.substringBefore(',')
        val second = pair.substringAfter(',', "")
        inningsList.add(Pair(first, second))
    }
    return GameDetails(descriptionMap, inningsList)
}

fun String.undoShot(): String {
    val lastShot = "(?<=['|,;])[^,;'|]*'[,;]?$".toRegex()
    return replace(lastShot, "")
}

fun String.translateGameInfo(gameInfo: String): String {
    return gameInfo.replace(" *: *".toRegex(), ":").replace(" *\n *".toRegex(), ";").trim() + '|' + this.substringAfter('|',"")
}
fun String.translateGameInfoReverse(): String {
    return substringBefore('|').replace(":", ": ").replace(";", "\n")
}

fun String.toSpelledOutDate(): String {
    val sdf = SimpleDateFormat("yyyy/MM/dd", Locale.US)
    val date = sdf.parse(this)
    val newSdf = SimpleDateFormat("MMMM d, yyyy", Locale.US)
    return newSdf.format(date ?: Date())
}

fun isSameTournament(gameDetails1: GameDetails, gameDetails2: GameDetails): Boolean {
    if (
        // if either goal in either game is not a valid APA goal, return false
        !gameDetails1.isValidApaGoals() ||
        !gameDetails2.isValidApaGoals() ||
        // if the team names are empty or don't match, return false
        !teamsMatch(gameDetails1, gameDetails2)
    ) {
        return false
    }
    // if tournament IDs match, return true
    if (!gameDetails1.tournamentId.isNullOrEmpty() || !gameDetails2.tournamentId.isNullOrEmpty()) {
        return gameDetails1.tournamentId == gameDetails2.tournamentId
    }
    // if tournament IDs don't match, return whether location and date match
    return !gameDetails1.location.isNullOrEmpty()
            && !gameDetails2.location.isNullOrEmpty()
            && gameDetails1.location == gameDetails2.location
            && !gameDetails1.date.isNullOrEmpty()
            && !gameDetails2.date.isNullOrEmpty()
            && gameDetails1.date == gameDetails2.date
}

fun teamsMatch(gameDetails1: GameDetails, gameDetails2: GameDetails): Boolean {
    if (
        gameDetails1.team1.isNullOrEmpty() ||
        gameDetails1.team2.isNullOrEmpty() ||
        gameDetails2.team1.isNullOrEmpty() ||
        gameDetails2.team2.isNullOrEmpty()
    ) {
        return false
    }
    if (gameDetails1.team1 == gameDetails2.team1) {
        return gameDetails1.team2 == gameDetails2.team2
    }
    if (gameDetails1.team1 == gameDetails2.team2) {
        return gameDetails1.team2 == gameDetails2.team1
    }
    return false
}