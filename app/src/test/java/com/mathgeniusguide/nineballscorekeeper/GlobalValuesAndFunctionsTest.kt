package com.mathgeniusguide.nineballscorekeeper

import com.mathgeniusguide.nineballscorekeeper.objects.GameDetails
import com.mathgeniusguide.nineballscorekeeper.util.addEmptyShotAt
import com.mathgeniusguide.nineballscorekeeper.util.cleanGameString
import com.mathgeniusguide.nineballscorekeeper.util.getChartShotTitle
import com.mathgeniusguide.nineballscorekeeper.util.getGameDetails
import com.mathgeniusguide.nineballscorekeeper.util.isClean
import com.mathgeniusguide.nineballscorekeeper.util.removeDuplicateBalls
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

    @Test
    fun testGetChartShotTitleNoBallsPocketed() {
        val expected = "Inning 12. Rack 4. John's turn.\nNo balls pocketed."
        val actual = getChartShotTitle("12", "4", "", "John")
        assert(expected == actual)
    }

    @Test
    fun testGetChartShotTitleOneBallPocketed() {
        val expected = "Inning 13. Rack 5. Steve's turn.\n6 ball pocketed. Foul."
        val actual = getChartShotTitle("13", "5", "6K", "Steve")
        assert(expected == actual)
    }

    @Test
    fun testGetChartShotTitleTwoBallsPocketed() {
        val expected = "Inning 17. Rack 3.\n5 and 7 balls pocketed."
        val actual = getChartShotTitle("17", "3", "57l", null)
        assert(expected == actual)
    }

    @Test
    fun testGetChartShotTitleThreeBallsPocketed() {
        val expected = "Inning 20. Rack 6. Paul's turn.\n2, 4, and 8 balls pocketed."
        val actual = getChartShotTitle("20", "6", "248", "Paul")
        assert(expected == actual)
    }

    @Test
    fun testCleanInningsAlreadyClean() {
        assert(gameString1 == gameString1.cleanGameString())
        assert(gameString2 == gameString2.cleanGameString())
        assert(gameString3 + ',' == (gameString3 + ',').cleanGameString())
    }

    @Test
    fun testCleanInningsAddOnEnd() {
        assert(gameString3 + ',' == gameString3.cleanGameString())
    }

    @Test
    fun testCleanInningsExtraBars() {
        val start = "Player 1:Pawn;Player 2:Queen;Location:Home;Date:2024/04/23|5'',4|'';|'|,';12'3',|||6||'"
        val expected = "Player 1:Pawn;Player 2:Queen;Location:Home;Date:2024/04/23|5'',4'';',';12'3'',6'"
        val actual = start.cleanGameString()
        assert(expected == actual)
    }

    @Test
    fun testCleanInningsNoApostrophes() {
        val start = "Player 1:Pawn;Player 2:Queen;Location:Home;Date:2024/04/23|1,2;3,4;5,6;"
        val expected = "Player 1:Pawn;Player 2:Queen;Location:Home;Date:2024/04/23|1'',2'';3'',4'';5'',6'';"
        val actual = start.cleanGameString()
        assert(expected == actual)
    }

    @Test
    fun testCleanInningsTooManyRounds() {
        val start = "Player 1:Pawn;Player 2:Queen;Location:Home;Date:2024/04/23|1';;2'',3'4'',5'';6''7''8'',9'1'2'3K'4';"
        val expected = "Player 1:Pawn;Player 2:Queen;Location:Home;Date:2024/04/23|1'',';',';2'',3'4'';6'',9'1'2'3K';"
        val actual = start.cleanGameString()
        assert(expected == actual)
    }

    @Test
    fun testIsClean() {
        assert(gameString1.isClean())
        assert(gameString2.isClean())
        assert(!gameString3.isClean())
        assert((gameString3 + ',').isClean())
        assert(!"Player 1:Pawn;Player 2:Queen;Location:Home;Date:2024/04/23|5'',4|'';|'|,';12'3',|||6||'".isClean())
        assert(!"Player 1:Pawn;Player 2:Queen;Location:Home;Date:2024/04/23|1,2;3,4;5,6;".isClean())
        assert(!"Player 1:Pawn;Player 2:Queen;Location:Home;Date:2024/04/23|1';;2'',3'4'',5'';6''7''8'',9'1'2'3K'4';".isClean())
        assert("Player 1:Pawn;Player 2:Queen;Location:Home;Date:2024/04/23|5'',4'';',';12'3'',6'".isClean())
        assert("Player 1:Pawn;Player 2:Queen;Location:Home;Date:2024/04/23|1'',2'';3'',4'';5'',6'';".isClean())
        assert("Player 1:Pawn;Player 2:Queen;Location:Home;Date:2024/04/23|1'',';',';2'',3'4'';6'',9'1'2'3K';".isClean())
    }

    @Test
    fun testRemoveDuplicateBallsNoChange() {
        val start = "Player 1:Pawn;Player 2:Queen;Location:Home;Date:2024/04/23|1'2'',3'4'';5'6'',7'8'';"
        val expected = "Player 1:Pawn;Player 2:Queen;Location:Home;Date:2024/04/23|1'2'',3'4'';5'6'',7'8'';"
        val actual = start.removeDuplicateBalls()
        assert(expected == actual)
    }

    @Test
    fun testRemoveDuplicateBallsEarlyNineNoChange() {
        val start = "Player 1:Pawn;Player 2:Queen;Location:Home;Date:2024/04/23|1'2'',3'4'9'';1'2'',3'4'';"
        val expected = "Player 1:Pawn;Player 2:Queen;Location:Home;Date:2024/04/23|1'2'',3'4'9'';1'2'',3'4'';"
        val actual = start.removeDuplicateBalls()
        assert(expected == actual)
    }

    @Test
    fun testRemoveDuplicateBallsInOneShot() {
        val start = "Player 1:Pawn;Player 2:Queen;Location:Home;Date:2024/04/23|11'23'44'5'',66'7'88'';"
        val expected = "Player 1:Pawn;Player 2:Queen;Location:Home;Date:2024/04/23|1'23'4'5'',6'7'8'';"
        val actual = start.removeDuplicateBalls()
        assert(expected == actual)
    }

    @Test
    fun testRemoveDuplicateBallsSomeRemoved() {
        val start = "Player 1:Pawn;Player 2:Queen;Location:Home;Date:2024/04/23|1'2'3'2'4';5'6'7'5'8'';"
        val expected = "Player 1:Pawn;Player 2:Queen;Location:Home;Date:2024/04/23|1'2'3''4';5'6'7''8'';"
        val actual = start.removeDuplicateBalls()
        assert(expected == actual)
    }

    @Test
    fun testRemoveDuplicateBallsEarlyIllegalNine() {
        val start = "Player 1:Pawn;Player 2:Queen;Location:Home;Date:2024/04/23|1'2'',3'4'9W'';1'2'',3'4'5'';"
        val expected = "Player 1:Pawn;Player 2:Queen;Location:Home;Date:2024/04/23|1'2'',3'4'9W'';''',''5'';"
        val actual = start.removeDuplicateBalls()
        assert(expected == actual)
    }

    @Test
    fun testAddInningMiddleOfInning() {
        val start = gameString1
        val expected = "Player 1:Pawn;Player 2:Queen;Location:Home;Date:2023/12/26|4'',';17'2'',3'5K';9'38'1'2'',';',';4'de',W';5'6e'7'9'7e'2W',1K';3'4'',5'';6'',8'9'59'18'2'3'4'6'5'7'9'2e'';1'3'4'e',';5'',6'7'8'';9'1'',2'd';3'',4'de';5'',de';M',6'7'8'9'"
        val actual = start.addEmptyShotAt(28)
        assert(expected == actual)
    }

    @Test
    fun testAddInningEndOfInning() {
        val start = gameString2
        val expected = "Player 1:Pawn;Player 2:Queen;Location:Yale Billiards;Date:2024/01/17|9'',';1'2'e',7W';3'4'd',';',';',5'';6'7'',8'K';9'24'1'3'',5'9K';6'',';',';7'',';',';8'9'49'138'2e'W',4'5'6'';',7'';8'',9'3e'W';1'2'd',';4'',5'';M',6'7'8'9'7'1'2'';3'9'46'"
        val actual = start.addEmptyShotAt(59)
        assert(expected == actual)
    }

    @Test
    fun testAddInningEndOfGame() {
        val start = gameString3 + ','
        val expected = "Player 1:Pawn;Player 2:Katy;Location:Home;Date:2024/02/03|',';1'',';2'3'',4'';5'6'7'8'9'34'de',7W';1'2'',';',';5'de',W';6'7'',8K';9'1'2'3'd',';4'd',5'';6e'7'',';8'9'3'1'',2'e';e',W';4'5'de',8W';6'',K';7'9'478'1'6'2'',';',';',3'5'';9'4'1'2'9'7'',';"
        val actual = start.addEmptyShotAt(200)
        assert(expected == actual)
    }

    @Test
    fun testAddInningBeginningOfGame() {
        val start = gameString3 + ','
        val expected = "Player 1:Pawn;Player 2:Katy;Location:Home;Date:2024/02/03|',';',1'';',2'3'';4'',5'6'7'8'9'34'de';7W',1'2'';',';',5'de';W',6'7'';8K',9'1'2'3'd';',4'd';5'',6e'7'';',8'9'3'1'';2'e',e';W',4'5'de';8W',6'';K',7'9'478'1'6'2'';',';',';3'5'',9'4'1'2'9'7'';"
        val actual = start.addEmptyShotAt(0)
        assert(expected == actual)
    }

}