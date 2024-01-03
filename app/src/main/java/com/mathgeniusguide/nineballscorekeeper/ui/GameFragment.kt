package com.mathgeniusguide.nineballscorekeeper

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.mathgeniusguide.nineballscorekeeper.databinding.GameFragmentBinding
import com.mathgeniusguide.nineballscorekeeper.enums.BallStatus
import com.mathgeniusguide.nineballscorekeeper.enums.PlayerTurn
import com.mathgeniusguide.nineballscorekeeper.enums.ShotCondition
import com.mathgeniusguide.nineballscorekeeper.objects.Shot
import com.mathgeniusguide.nineballscorekeeper.util.undoShot

class GameFragment: Fragment() {
    private lateinit var mainActivity: MainActivity
    private var _binding: GameFragmentBinding? = null
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!
    private val selected = arrayOf(false, false, false, false, false, false, false, false, false)
    private lateinit var ballList: List<ImageView>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = activity as MainActivity
        setHasOptionsMenu(true)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = GameFragmentBinding.inflate(inflater, container, false)
        ballList = listOf(
            binding.ball1,
            binding.ball2,
            binding.ball3,
            binding.ball4,
            binding.ball5,
            binding.ball6,
            binding.ball7,
            binding.ball8,
            binding.ball9
        )
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        for (index in ballList.indices) {
            ballList[index].setOnClickListener {
                if (it.alpha == 1.toFloat()) {
                    toggleArray(index)
                    (it as ImageView).setBackgroundColor(
                        Color.argb(
                            if (selected[index]) 255 else 0,
                            64,
                            64,
                            255
                        )
                    )
                }
            }
        }
        binding.badBreakButton.setOnClickListener {
            if (mainActivity.gameDetails.shotCondition == ShotCondition.BREAK) {
                val miscueString = if (binding.miscueCheckBox.isChecked) "m'" else "'"
                when(binding.foulGroup.checkedRadioButtonId) {
                    R.id.scratch -> makeShot("BK" + miscueString)
                    R.id.offTable -> makeShot("BP" + miscueString)
                    R.id.legalShot -> makeShot("B" + miscueString)
                    else -> Toast.makeText(context, getString(R.string.bad_break_error), Toast.LENGTH_LONG).show()
                }
            }
        }
        binding.undoButton.setOnClickListener {
            mainActivity.updateGameString(mainActivity.gameString.undoShot())
            updateScores()
        }
        binding.shotButton.setOnClickListener {
            var shotString = ""
            for (i in selected.indices) {
                if (selected[i]) {
                    shotString += (i + 1)
                }
            }
            shotString += when (binding.foulGroup.checkedRadioButtonId) {
                R.id.scratch -> 'K'
                R.id.wrongFirst -> 'W'
                R.id.miss -> 'M'
                R.id.noRail -> 'R'
                R.id.offTable -> 'P'
                R.id.otherFoul -> 'O'
                else -> ""
            }
            if (binding.defenseCheckBox.isChecked) {
                shotString += 'd'
            }
            if (binding.eclipseCheckBox.isChecked) {
                shotString += 'e'
            }
            if (binding.bankCheckBox.isChecked) {
                shotString += 'b'
            }
            if (binding.luckyCheckBox.isChecked) {
                shotString += 'l'
            }
            if (binding.miscueCheckBox.isChecked) {
                shotString += 'm'
            }
            makeShot(shotString + '\'')
            if (mainActivity.gameDetails.shotCondition == ShotCondition.BREAK) {
                mainActivity.readScore()
            }
        }
        binding.defenseCheckBox.setOnClickListener {
            if (mainActivity.gameDetails.shotCondition == ShotCondition.BREAK) {
                (it as CheckBox).isChecked = false
            }
        }
        binding.bankCheckBox.setOnClickListener {
            if (mainActivity.gameDetails.shotCondition == ShotCondition.BREAK) {
                (it as CheckBox).isChecked = false
            }
        }
        binding.luckyCheckBox.setOnClickListener {
            if (mainActivity.gameDetails.shotCondition == ShotCondition.BREAK) {
                (it as CheckBox).isChecked = false
            }
        }

        binding.player1Label.text = mainActivity.gameDetails.player1Name
        binding.player2Label.text = mainActivity.gameDetails.player2Name
        updateScores()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun toggleArray(index: Int) {
        if (index < 0 || index > 8) {
            return
        }
        selected[index] = !selected[index]
    }

    private fun updateScores() {
        with (mainActivity.gameDetails.gameStats) {
            binding.player1Score.text = player1Stats.score.toString()
            binding.player2Score.text = player2Stats.score.toString()
            if (playerTurn == PlayerTurn.PLAYER1) {
                binding.player1Label.setTextColor(resources.getColor(R.color.yourTurn))
                binding.player1Score.setTextColor(resources.getColor(R.color.yourTurn))
                binding.player2Label.setTextColor(resources.getColor(R.color.notYourTurn))
                binding.player2Score.setTextColor(resources.getColor(R.color.notYourTurn))
            }
            if (playerTurn == PlayerTurn.PLAYER2) {
                binding.player1Label.setTextColor(resources.getColor(R.color.notYourTurn))
                binding.player1Score.setTextColor(resources.getColor(R.color.notYourTurn))
                binding.player2Label.setTextColor(resources.getColor(R.color.yourTurn))
                binding.player2Score.setTextColor(resources.getColor(R.color.yourTurn))
            }
        }
        for (index in ballList.indices) {
            ballList[index].setBackgroundColor(
                Color.argb(
                    0,
                    64,
                    64,
                    255
                )
            )
            if (index != 8) {
                ballList[index].alpha =
                    if (mainActivity.gameDetails.ballStatus[index] == BallStatus.ON_TABLE) 1.toFloat() else 0.25.toFloat()
            }
        }
        binding.ballInHandTextView.visibility = if (mainActivity.gameDetails.shotCondition == ShotCondition.BALL_IN_HAND) View.VISIBLE else View.GONE
        binding.badBreakButton.visibility = if (mainActivity.gameDetails.shotCondition == ShotCondition.BREAK) View.VISIBLE else View.GONE
        selected.fill(false)
        binding.foulGroup.check(R.id.legalShot)
        binding.defenseCheckBox.isChecked = false
        binding.eclipseCheckBox.isChecked = false
        binding.bankCheckBox.isChecked = false
        binding.luckyCheckBox.isChecked = false
        binding.miscueCheckBox.isChecked = false
        binding.gameStringTextView.text = mainActivity.gameString
    }

    private fun makeShot(string: String) {
        if (string.contains("[1-9]".toRegex()) && string.contains('R')) {
            Toast.makeText(context, getString(R.string.no_rail_error), Toast.LENGTH_LONG).show()
            return
        }
        if (string.contains("[1-9]".toRegex()) && string.contains('M')) {
            Toast.makeText(context, getString(R.string.miss_error), Toast.LENGTH_LONG).show()
            return
        }
        val playerTurn = mainActivity.gameDetails.gameStats.playerTurn
        val shotCondition = mainActivity.gameDetails.shotCondition
        if (string.contains('m') && !string.contains('B') && shotCondition == ShotCondition.BREAK) {
            Toast.makeText(context, getString(R.string.miscue_good_break_error), Toast.LENGTH_LONG).show()
            return
        }
        val ballStatus = mainActivity.gameDetails.ballStatus.toMutableList()
        val shot = Shot(string, playerTurn, shotCondition, ballStatus)
        val newString = when {
            shot.isTurnEnd && playerTurn == PlayerTurn.PLAYER1 -> string + ','
            shot.isTurnEnd && playerTurn == PlayerTurn.PLAYER2 -> string + ';'
            else -> string
        }
        mainActivity.updateGameString(mainActivity.gameString + newString)
        updateScores()
    }
}

