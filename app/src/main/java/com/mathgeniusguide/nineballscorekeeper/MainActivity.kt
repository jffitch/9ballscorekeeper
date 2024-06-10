package com.mathgeniusguide.nineballscorekeeper

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.mathgeniusguide.nineballscorekeeper.databinding.ActivityMainBinding
import com.mathgeniusguide.nineballscorekeeper.enums.PlayerTurn
import com.mathgeniusguide.nineballscorekeeper.enums.SharedPreferencesTarget
import com.mathgeniusguide.nineballscorekeeper.objects.ChartShot
import com.mathgeniusguide.nineballscorekeeper.objects.GameDetails
import com.mathgeniusguide.nineballscorekeeper.util.getGameDetails
import com.mathgeniusguide.nineballscorekeeper.util.isSameTournament
import com.mathgeniusguide.nineballscorekeeper.util.translateGameInfo
import com.mathgeniusguide.nineballscorekeeper.util.translateGameInfoReverse
import com.mathgeniusguide.nineballscorekeeper.util.yyyymmdd
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var textToSpeech: TextToSpeech
    var gameString = ""
    var gameList = mutableListOf<String>()
    var chartList = mutableListOf<ChartShot>()
    lateinit var gameDetails: GameDetails
    var player1RunningTotal = 0
    var player2RunningTotal = 0
    var player1Wins = 0
    var player2Wins = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        readGameList()
        textToSpeech = TextToSpeech(this, this)
        if (readSharedPreferences(SharedPreferencesTarget.GAME_INFO) == "") {
            writeSharedPreferences(SharedPreferencesTarget.GAME_INFO, defaultText())
        }
        gameString = readSharedPreferences(SharedPreferencesTarget.GAME_STRING)
        if (gameString == "") {
            gameString =
                "".translateGameInfo(readSharedPreferences(SharedPreferencesTarget.GAME_INFO))
            writeSharedPreferences(SharedPreferencesTarget.GAME_STRING, gameString)
        }
        gameDetails = getGameDetails(gameString)

        if (readSharedPreferences(SharedPreferencesTarget.ZERO_SCORE) == "") {
            writeSharedPreferences(SharedPreferencesTarget.ZERO_SCORE, getString(R.string.goose_egg))
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setSupportActionBar(binding.toolbar)
        navController = findNavController(R.id.nav_host_fragment)
        binding.toolbar.setupWithNavController(
            navController,
            AppBarConfiguration(
                setOf(
                    R.id.game,
                    R.id.rack_stats,
                    R.id.player_stats,
                    R.id.history,
                    R.id.all_time
                )
            )
        )
        binding.tabs.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_bar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    override fun onInit(status: Int) {
        textToSpeech.language = Locale.US
    }

    fun writeSharedPreferences(target: SharedPreferencesTarget, string: String) {
        val editor = getPreferences(Context.MODE_PRIVATE).edit()
        editor.putString(target.target, string)
        editor.apply()
    }

    fun readSharedPreferences(target: SharedPreferencesTarget): String {
        val pref = getPreferences(Context.MODE_PRIVATE)
        return pref.getString(target.target, "") ?: ""
    }

    fun writeGameList() {
        val editor = getPreferences(Context.MODE_PRIVATE).edit()
        editor.putStringSet("game_list", gameList.toSet())
        editor.apply()
    }

    fun addToGameList(gameString: String) {
        gameList.add(gameString)
        gameList.sortBy { getGameDetails(it).date }
        writeGameList()
    }

    fun removeFromGameList(index: Int) {
        gameList.removeAt(index)
        writeGameList()
    }

    fun readGameList() {
        val pref = getPreferences(Context.MODE_PRIVATE)
        gameList = (pref.getStringSet("game_list", setOf())?.toList()
            ?: listOf()).sortedBy { getGameDetails(it).date }.toMutableList()
    }

    fun defaultText(): String {
        val dateString = SimpleDateFormat(yyyymmdd, Locale.ENGLISH).format(Date())
        return String.format(getString(R.string.default_game_info_string), dateString)
    }

    fun updateGameString(string: String) {
        writeSharedPreferences(SharedPreferencesTarget.GAME_STRING, string)
        writeSharedPreferences(SharedPreferencesTarget.GAME_INFO, string.translateGameInfoReverse())
        gameString = string
        gameDetails = getGameDetails(string)
    }

    fun resetGame() {
        updateGameString(gameString.substringBefore('|') + '|')
        writeSharedPreferences(SharedPreferencesTarget.LOADED_GAME, "")
    }

    fun speak(speech: String) {
        textToSpeech.speak(speech, TextToSpeech.QUEUE_ADD, null, "")
    }

    fun speakScore() {
        val player1Score = gameDetails.gameStats.player1Stats.score
        val player2Score = gameDetails.gameStats.player2Stats.score
        val player1Name = gameDetails.pronunciation1
        val player2Name = gameDetails.pronunciation2
        val winner = tournamentWinner()
        if (winner != null) {
            speak(
                String.format(
                    getString(R.string.tournament_over),
                    winner
                )
            )
        }
        speak(
            String.format(
                if (gameDetails.gameOver || winner != null) getString(R.string.spoken_final_score) else getString(R.string.spoken_score),
                player1Name,
                if (player1Score == 0) readSharedPreferences(SharedPreferencesTarget.ZERO_SCORE) else player1Score,
                player2Name,
                if (player2Score == 0) readSharedPreferences(SharedPreferencesTarget.ZERO_SCORE) else player2Score
            )
        )
    }

    fun calculateRunningTotal() {
        player1RunningTotal = 0
        player2RunningTotal = 0
        player1Wins = 0
        player2Wins = 0
        for (i in gameList) {
            val game = getGameDetails(i)
            if (isSameTournament(gameDetails, game)) {
                if (gameDetails.team1 == game.team1) {
                    player1RunningTotal += game.player1MatchPoints
                    if (game.player1MatchPoints >= 12) {
                        player1Wins++
                    }
                }
                if (gameDetails.team1 == game.team2) {
                    player1RunningTotal += game.player2MatchPoints
                    if (game.player2MatchPoints >= 12) {
                        player1Wins++
                    }
                }
                if (gameDetails.team2 == game.team1) {
                    player2RunningTotal += game.player1MatchPoints
                    if (game.player1MatchPoints >= 12) {
                        player2Wins++
                    }
                }
                if (gameDetails.team2 == game.team2) {
                    player2RunningTotal += game.player2MatchPoints
                    if (game.player2MatchPoints >= 12) {
                        player2Wins++
                    }
                }
            }
        }
    }

    fun tournamentWinner(): String? {
        if (readSharedPreferences(SharedPreferencesTarget.END_AT_TOURNAMENT_WIN) != "true") {
            return null
        }
        if (gameDetails.player1MatchPoints + player1RunningTotal >=
            (if (player1Wins >= 3) 50 else 51)) {
            return gameDetails.team1
        }
        if (gameDetails.player2MatchPoints + player2RunningTotal >=
            (if (player2Wins >= 3) 50 else 51)) {
            return gameDetails.team2
        }
        return null
    }

    fun calculateChartList() {
        chartList.clear()
        var isRackStart = true
        var rackNumber = gameDetails.startRacks
        var inningNumber = gameDetails.startInnings
        for (pair in gameDetails.innings) {
            var isInningStart = true
            val player1Shots = pair.first.substringBeforeLast('\'').split("'")
            val player2Shots = pair.second.substringBeforeLast('\'').split("'")
            for (shot in player1Shots) {
                val isFoul = "[KMWRPO]".toRegex().containsMatchIn(shot)
                val isNine = shot.contains('9') && !isFoul
                val isStalemate = shot.contains('S')
                val isTimeOut = shot.contains('t')
                val isDefense = shot.contains('d')
                chartList.add(
                    ChartShot(
                        isInningStart = isInningStart,
                        isRackStart = isRackStart,
                        inning = inningNumber.toString(),
                        rack = rackNumber.toString(),
                        ballsPocketed = shot,
                        isDefense = isDefense,
                        isFoul = isFoul,
                        isStalemate = isStalemate,
                        isTimeOut = isTimeOut,
                        playerTurn = PlayerTurn.PLAYER1
                    )
                )
                isInningStart = false
                isRackStart = isNine || isStalemate
                if (isRackStart) {
                    rackNumber++
                }
            }
            for (shot in player2Shots) {
                val isFoul = "[KMWRPO]".toRegex().containsMatchIn(shot)
                val isNine = shot.contains('9') && !isFoul
                val isStalemate = shot.contains('S')
                val isTimeOut = shot.contains('t')
                val isDefense = shot.contains('d')
                chartList.add(
                    ChartShot(
                        isInningStart = false,
                        isRackStart = isRackStart,
                        inning = inningNumber.toString(),
                        rack = rackNumber.toString(),
                        ballsPocketed = shot,
                        isDefense = isDefense,
                        isFoul = isFoul,
                        isStalemate = isStalemate,
                        isTimeOut = isTimeOut,
                        playerTurn = PlayerTurn.PLAYER2
                    )
                )
                isRackStart = isNine || isStalemate
                if (isRackStart) {
                    rackNumber++
                }
            }
            inningNumber++
        }
    }
}