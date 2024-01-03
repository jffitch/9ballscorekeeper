package com.mathgeniusguide.nineballscorekeeper.util

import com.mathgeniusguide.nineballscorekeeper.objects.GameDetails
import java.nio.file.Paths

val yyyymmdd = "yyyy/MM/dd"

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