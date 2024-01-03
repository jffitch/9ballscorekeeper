package com.mathgeniusguide.nineballscorekeeper

import com.mathgeniusguide.nineballscorekeeper.objects.GameDetails
import com.mathgeniusguide.nineballscorekeeper.util.getGameDetails
import com.mathgeniusguide.nineballscorekeeper.util.undoShot
import org.junit.Test

class GlobalValuesAndFunctionsTest {
    @Test
    fun testGetGameDetails1() {
        val expectedGameDetails = GameDetails(
            mapOf(
                "Player 1" to "Pawn",
                "Player 2" to "Queen",
                "Location" to "Home",
                "Date" to "2023/12/26"
            ),
            listOf(
                Pair("4''", "'"),
                Pair("17'2''", "3'5K'"),
                Pair("9'38'1'2''", "'"),
                Pair("'", "'"),
                Pair("4'de'", "W'"),
                Pair("5'6e'7'9'7e'2W'", "1K'"),
                Pair("3'4'5''", "6''"),
                Pair("8'9'59'18'2'3'4'6'5'7'9'2e''", "1'3'4'e'"),
                Pair("'", "5''"),
                Pair("6'7'8''", "9'1''"),
                Pair("2'd'", "3''"),
                Pair("4'de'", "5''"),
                Pair("de'", "M'"),
                Pair("6'7'8'9'", "")
            )
        )
        val actualGameDetails = getGameDetails(gameString1)
        assert(mapsMatch(expectedGameDetails.description, actualGameDetails.description))
        assert(pairListsMatch(expectedGameDetails.innings, actualGameDetails.innings))
    }

    @Test
    fun testGetGameDetails2() {
        val expectedGameDetails = GameDetails(
            mapOf(
                "Player 1" to "Pawn",
                "Player 2" to "Queen",
                "Location" to "Yale Billiards",
                "Date" to "2024/01/17"
            ),
            listOf(
                Pair("9''", "'"),
                Pair("1'2'e'", "7W'"),
                Pair("3'4'd'", "'"),
                Pair("'", "'"),
                Pair("'", "5''"),
                Pair("6'7''", "8'K'"),
                Pair("9'24'1'3''", "5'9K'"),
                Pair("6''", "'"),
                Pair("'", "'"),
                Pair("7''", "'"),
                Pair("'", "'"),
                Pair("8'9'49'138'2e'W'", "4'5'6''"),
                Pair("'", "7''"),
                Pair("8''", "9'3e'W'"),
                Pair("1'2'd'", "4''"),
                Pair("5''", "M'"),
                Pair("6'7'8'9'7'1'2''", "3'9'46'")
            )
        )
        val actualGameDetails = getGameDetails(gameString2)
        assert(mapsMatch(expectedGameDetails.description, actualGameDetails.description))
        assert(pairListsMatch(expectedGameDetails.innings, actualGameDetails.innings))
    }

    @Test
    fun testGetGameDetails3() {
        val expectedGameDetails = GameDetails(
            mapOf(
                "Player 1" to "Pawn",
                "Player 2" to "Katy",
                "Location" to "Home",
                "Date" to "2024/02/03"
            ),
            listOf(
                Pair("'", "'"),
                Pair("1''", "'"),
                Pair("2'3''", "4''"),
                Pair("5'6'7'8'9'34'de'", "7W'"),
                Pair("1'2''", "'"),
                Pair("'", "'"),
                Pair("5'de'", "W'"),
                Pair("6'7''", "8K'"),
                Pair("9'1'2'3'd'", "'"),
                Pair("4'd'", "5''"),
                Pair("6e'7''", "'"),
                Pair("8'9'3'1''", "2'e'"),
                Pair("e'", "W'"),
                Pair("4'5'de'", "8W'"),
                Pair("6''", "K'"),
                Pair("7'9'478'1'6'2''", "'"),
                Pair("'", "'"),
                Pair("'", "3'5''"),
                Pair("9'4'1'2'9'7''", "")
            )
        )
        val actualGameDetails = getGameDetails(gameString3)
        assert(mapsMatch(expectedGameDetails.description, actualGameDetails.description))
        assert(pairListsMatch(expectedGameDetails.innings, actualGameDetails.innings))
    }

    @Test
    fun testGameJustStarted() {
        val expectedGameDetails = GameDetails(
            mapOf(
                "Player 1" to "Pawn",
                "Player 2" to "Queen",
                "Location" to "Home",
                "Date" to "2024/04/11"
            ),
            listOf(
                Pair("", "")
            )
        )
        val actualGameDetails = getGameDetails(gameJustStarted)
        assert(mapsMatch(expectedGameDetails.description, actualGameDetails.description))
        assert(pairListsMatch(expectedGameDetails.innings, actualGameDetails.innings))
    }

    @Test
    fun testUndoFirstShot() {
        val string = "Player 1:Pawn;Player 2:Queen|13e'"
        val expected = "Player 1:Pawn;Player 2:Queen|"
        val actual = string.undoShot()
        assert(expected == actual)
    }

    @Test
    fun testUndoShot() {
        val string = "Player 1:Pawn;Player 2:Queen|13e'2'4'5'6'"
        val expected = "Player 1:Pawn;Player 2:Queen|13e'2'4'5'"
        val actual = string.undoShot()
        assert(expected == actual)
    }

    @Test
    fun testUndoPlayer1TurnEnd() {
        val string = "Player 1:Pawn;Player 2:Queen|13e'2'4'5'6W',"
        val expected = "Player 1:Pawn;Player 2:Queen|13e'2'4'5'"
        val actual = string.undoShot()
        assert(expected == actual)
    }

    @Test
    fun testUndoPlayer2TurnEnd() {
        val string = "Player 1:Pawn;Player 2:Queen|13e'2'4'5'',6'7'de';"
        val expected = "Player 1:Pawn;Player 2:Queen|13e'2'4'5'',6'7'"
        val actual = string.undoShot()
        assert(expected == actual)
    }

    @Test
    fun testUndoTwoShots() {
        val string = "Player 1:Pawn;Player 2:Queen|13e'2'4'5'6'"
        val expected = "Player 1:Pawn;Player 2:Queen|13e'2'4'"
        val actual = string.undoShot().undoShot()
        assert(expected == actual)
    }

    @Test
    fun testUndoTwoShotsInningBetween() {
        val string = "Player 1:Pawn;Player 2:Queen|13e'2'4'5'',6'7'de';8b'"
        val expected = "Player 1:Pawn;Player 2:Queen|13e'2'4'5'',6'7'"
        val actual = string.undoShot().undoShot()
        assert(expected == actual)
    }

    @Test
    fun testUndoAtBeginning() {
        val string = "Player 1:Pawn;Player 2:Queen|"
        val expected = "Player 1:Pawn;Player 2:Queen|"
        val actual = string.undoShot()
        assert(expected == actual)
    }
}