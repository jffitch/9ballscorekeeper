package com.mathgeniusguide.nineballscorekeeper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mathgeniusguide.nineballscorekeeper.databinding.SettingsFragmentBinding
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
        binding.gameInfoEditText.doOnTextChanged { text, start, before, count ->
            mainActivity.writeSharedPreferences(SharedPreferencesTarget.GAME_INFO, text.toString())
            val gameString = mainActivity.readSharedPreferences(SharedPreferencesTarget.GAME_STRING)
            val newGameString = gameString.translateGameInfo(text.toString())
            mainActivity.writeSharedPreferences(SharedPreferencesTarget.GAME_STRING, newGameString)
            mainActivity.gameString = newGameString
            mainActivity.gameDetails = getGameDetails(newGameString)
        }
        binding.zeroScoreEditText.setText(mainActivity.readSharedPreferences(SharedPreferencesTarget.ZERO_SCORE))
        binding.zeroScoreEditText.doOnTextChanged { text, start, before, count ->
            mainActivity.writeSharedPreferences(SharedPreferencesTarget.ZERO_SCORE, text.toString())
        }
        binding.restoreDefaultButton.setOnClickListener {
            binding.gameInfoEditText.setText(mainActivity.defaultText())
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
            val builder = AlertDialog.Builder(requireContext())
            builder.setPositiveButton("Reset") { _, _ ->
                mainActivity.resetGame()
            }
            builder.setNegativeButton("Back", null)
            builder.setMessage(getString(R.string.reset_are_you_sure))
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
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

