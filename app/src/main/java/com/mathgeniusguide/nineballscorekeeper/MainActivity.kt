package com.mathgeniusguide.nineballscorekeeper

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.mathgeniusguide.nineballscorekeeper.databinding.ActivityMainBinding
import com.mathgeniusguide.nineballscorekeeper.enums.SharedPreferencesTarget
import com.mathgeniusguide.nineballscorekeeper.objects.GameDetails
import com.mathgeniusguide.nineballscorekeeper.util.getGameDetails
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
    lateinit var gameDetails: GameDetails
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

    private fun writeGameList() {
        val editor = getPreferences(Context.MODE_PRIVATE).edit()
        editor.putStringSet("game_list", gameList.toSet())
        editor.apply()
    }

    fun addToGameList(gameString: String) {
        gameList.add(gameString)
        gameList.sortBy { getGameDetails(it).description["Date"] }
        writeGameList()
    }

    fun removeFromGameList(index: Int) {
        gameList.removeAt(index)
        writeGameList()
    }

    fun readGameList() {
        val pref = getPreferences(Context.MODE_PRIVATE)
        gameList = (pref.getStringSet("game_list", setOf())?.toList()
            ?: listOf()).sortedBy { getGameDetails(it).description["Date"] }.toMutableList()
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
    }

    fun speak(speech: String) {
        textToSpeech.speak(speech, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    fun readScore() {
        val player1Score = gameDetails.gameStats.player1Stats.score
        val player2Score = gameDetails.gameStats.player2Stats.score
        val player1Name = gameDetails.player1Name
        val player2Name = gameDetails.player2Name
        speak(
            String.format(
                getString(R.string.spoken_score),
                player1Name,
                if (player1Score == 0) getString(R.string.goose_egg) else player1Score,
                player2Name,
                if (player2Score == 0) getString(R.string.goose_egg) else player2Score
            )
        )
    }
}