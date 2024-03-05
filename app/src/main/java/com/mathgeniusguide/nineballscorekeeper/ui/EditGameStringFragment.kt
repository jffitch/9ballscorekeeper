package com.mathgeniusguide.nineballscorekeeper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.mathgeniusguide.nineballscorekeeper.databinding.EditGameStringFragmentBinding
import com.mathgeniusguide.nineballscorekeeper.databinding.SettingsFragmentBinding
import com.mathgeniusguide.nineballscorekeeper.enums.SharedPreferencesTarget
import com.mathgeniusguide.nineballscorekeeper.util.getGameDetails
import com.mathgeniusguide.nineballscorekeeper.util.translateGameInfo

class EditGameStringFragment: Fragment() {
    private lateinit var mainActivity: MainActivity
    private var _binding: EditGameStringFragmentBinding? = null
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
        _binding = EditGameStringFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.editChoice.setOnCheckedChangeListener { radioGroup, id ->
            binding.thisString.visibility = if (id == R.id.thisGame) View.VISIBLE else View.GONE
            binding.stringList.visibility = if (id == R.id.gameList) View.VISIBLE else View.GONE
        }
        binding.saveButton.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setPositiveButton("Save") { _, _ ->
                when (binding.editChoice.checkedRadioButtonId) {
                    R.id.thisGame -> {
                        val string = binding.thisString.text.toString()
                        mainActivity.writeSharedPreferences(
                            SharedPreferencesTarget.GAME_INFO,
                            string.substringBefore('|', "")
                        )
                        mainActivity.writeSharedPreferences(
                            SharedPreferencesTarget.GAME_STRING,
                            string
                        )
                        mainActivity.gameString = string
                        mainActivity.gameDetails = getGameDetails(string)
                    }

                    R.id.gameList -> {
                        val string = binding.stringList.text.toString()
                        mainActivity.gameList =
                            string.split("(\\s*\\n\\s*)+".toRegex()).toMutableList()
                        mainActivity.writeGameList()
                    }
                }
            }
            builder.setNegativeButton("Back", null)
            builder.setMessage(getString(R.string.save_string_are_you_sure))
            builder.create().show()
        }
        binding.thisString.setText(mainActivity.gameString)
        binding.stringList.setText(mainActivity.gameList.joinToString("\n\n"))
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

