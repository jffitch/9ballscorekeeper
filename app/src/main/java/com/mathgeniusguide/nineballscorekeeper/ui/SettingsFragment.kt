package com.mathgeniusguide.nineballscorekeeper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import com.mathgeniusguide.nineballscorekeeper.databinding.SettingsFragmentBinding
import com.mathgeniusguide.nineballscorekeeper.enums.DescriptionKey
import com.mathgeniusguide.nineballscorekeeper.enums.SharedPreferencesTarget
import com.mathgeniusguide.nineballscorekeeper.util.getGameDetails
import com.mathgeniusguide.nineballscorekeeper.util.translateGameInfo

class SettingsFragment: Fragment() {
    private lateinit var mainActivity: MainActivity
    private var _binding: SettingsFragmentBinding? = null
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = activity as MainActivity
        setHasOptionsMenu(true)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = SettingsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.gameInfoEditText.setText(mainActivity.readSharedPreferences(SharedPreferencesTarget.GAME_INFO))
        binding.updateInfoButton.setOnClickListener {
            val text = binding.gameInfoEditText.text.toString()
            mainActivity.writeSharedPreferences(SharedPreferencesTarget.GAME_INFO, text)
            val gameString = mainActivity.readSharedPreferences(SharedPreferencesTarget.GAME_STRING)
            val newGameString = gameString.translateGameInfo(text)
            mainActivity.writeSharedPreferences(SharedPreferencesTarget.GAME_STRING, newGameString)
            mainActivity.gameString = newGameString
            mainActivity.gameDetails = getGameDetails(newGameString)
            Toast.makeText(requireContext(), getString(R.string.game_info_updated), Toast.LENGTH_LONG).show()
        }
        binding.zeroScoreEditText.setText(mainActivity.readSharedPreferences(SharedPreferencesTarget.ZERO_SCORE))
        binding.zeroScoreEditText.doOnTextChanged { text, start, before, count ->
            mainActivity.writeSharedPreferences(SharedPreferencesTarget.ZERO_SCORE, text.toString())
        }
        binding.speakName1.setOnClickListener {
            val descriptionMap = mutableMapOf<String, String>()
            for (keyValue in binding.gameInfoEditText.text.toString().split('\n')) {
                if (keyValue.count { it == ':' } != 1) {
                    continue
                }
                val key = keyValue.substringBefore(':').trim()
                val value = keyValue.substringAfter(':').trim()
                descriptionMap.put(key, value)
            }
            mainActivity.speak(when {
                !descriptionMap["Pronunciation 1"].isNullOrEmpty() -> descriptionMap["Pronunciation 1"]!!
                !descriptionMap["Player 1"].isNullOrEmpty() -> descriptionMap["Player 1"]!!
                else -> "Player 1"
            })
        }
        binding.speakName2.setOnClickListener {
            val descriptionMap = mutableMapOf<String, String>()
            for (keyValue in binding.gameInfoEditText.text.toString().split('\n')) {
                if (keyValue.count { it == ':' } != 1) {
                    continue
                }
                val key = keyValue.substringBefore(':').trim()
                val value = keyValue.substringAfter(':').trim()
                descriptionMap.put(key, value)
            }
            mainActivity.speak(when {
                !descriptionMap["Pronunciation 2"].isNullOrEmpty() -> descriptionMap["Pronunciation 2"]!!
                !descriptionMap["Player 2"].isNullOrEmpty() -> descriptionMap["Player 2"]!!
                else -> "Player 2"
            })
        }
        binding.restoreDefaultButton.setOnClickListener {
            binding.gameInfoEditText.setText(mainActivity.defaultText())
        }
        binding.lateStartButton.setOnClickListener {
            var gameInfoText = binding.gameInfoEditText.text.toString()
            for (value in DescriptionKey.values()) {
                if (value.text.startsWith("Start")) {
                    gameInfoText += "\n${value.text}: "
                }
            }
            binding.gameInfoEditText.setText(gameInfoText)
        }
        binding.saveGameButton.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setPositiveButton("Save") { _, _ ->
                mainActivity.addToGameList(mainActivity.gameString)
                binding.gameInfoEditText.setText(mainActivity.defaultText())
                mainActivity.resetGame()
            }
            builder.setNegativeButton("Back", null)
            builder.setMessage(getString(R.string.save_are_you_sure))
            builder.create().show()
        }
        binding.resetGameButton.setOnClickListener {
            val loadedGame = mainActivity.readSharedPreferences(SharedPreferencesTarget.LOADED_GAME)
            val builder = AlertDialog.Builder(requireContext())
            builder.setPositiveButton("Reset") { _, _ ->
                if (loadedGame.isNotEmpty()) {
                    mainActivity.addToGameList(loadedGame)
                }
                mainActivity.resetGame()
            }
            builder.setNegativeButton("Back", null)
            builder.setMessage(
                if (loadedGame.isEmpty())
                    getString(R.string.reset_are_you_sure)
                else
                    getString(R.string.reset_revert_are_you_sure)
            )
            builder.create().show()
        }
        binding.editGameString.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setPositiveButton("Continue") { _, _ ->
                findNavController().navigate(R.id.action_settings_to_edit_game_string)
            }
            builder.setNegativeButton("Back", null)
            builder.setMessage(getString(R.string.edit_string_are_you_sure))
            builder.create().show()
        }
        binding.endAtTournamentWinSwitch.setOnCheckedChangeListener { _ , isChecked ->
            mainActivity.writeSharedPreferences(
                SharedPreferencesTarget.END_AT_TOURNAMENT_WIN,
                if (isChecked) "true" else "false"
            )
        }
        binding.endAtTournamentWinSwitch.isChecked =
            mainActivity.readSharedPreferences(SharedPreferencesTarget.END_AT_TOURNAMENT_WIN) == "true"
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

