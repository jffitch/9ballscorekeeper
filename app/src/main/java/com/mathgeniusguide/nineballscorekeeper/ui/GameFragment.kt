package com.mathgeniusguide.nineballscorekeeper

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.mathgeniusguide.nineballscorekeeper.databinding.GameFragmentBinding
import com.mathgeniusguide.nineballscorekeeper.enums.BallStatus
import com.mathgeniusguide.nineballscorekeeper.enums.PlayerTurn
import com.mathgeniusguide.nineballscorekeeper.enums.ShotCondition
import com.mathgeniusguide.nineballscorekeeper.objects.Shot
import com.mathgeniusguide.nineballscorekeeper.util.undoShot
import java.util.Arrays

class GameFragment : Fragment() {
    private lateinit var mainActivity: MainActivity
    private var _binding: GameFragmentBinding? = null

    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!
    private val selected = arrayOf(false, false, false, false, false, false, false, false, false)
    private lateinit var ballList: List<ImageView>
    private lateinit var ballTextList: List<TextView>
    private var areButtonsEnabled = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = activity as MainActivity
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        ballTextList = listOf(
            binding.ball1Text,
            binding.ball2Text,
            binding.ball3Text,
            binding.ball4Text,
            binding.ball5Text,
            binding.ball6Text,
            binding.ball7Text,
            binding.ball8Text
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
            if (mainActivity.gameDetails.gameOver || !areButtonsEnabled) {
                return@setOnClickListener
            }
            disableButtons()
            if (mainActivity.gameDetails.shotCondition == ShotCondition.BREAK) {
                val miscueString = if (binding.miscueCheckBox.isChecked) "m'" else "'"
                when (binding.foulGroup.checkedRadioButtonId) {
                    R.id.scratch -> makeShot("BK" + miscueString)
                    R.id.offTable -> makeShot("BP" + miscueString)
                    R.id.legalShot -> makeShot("B" + miscueString)
                    else -> Toast.makeText(
                        context,
                        getString(R.string.bad_break_error),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
        binding.undoButton.setOnClickListener {
            if (!areButtonsEnabled) {
                return@setOnClickListener
            }
            disableButtons()
            mainActivity.updateGameString(mainActivity.gameString.undoShot())
            updateScores()
        }
        binding.shotButton.setOnClickListener {
            if (!areButtonsEnabled) {
                return@setOnClickListener
            }
            disableButtons()
            if (mainActivity.gameDetails.gameOver) {
                return@setOnClickListener
            }
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
            if (binding.timeOutCheckBox.isChecked) {
                shotString += 't'
            }
            makeShot(shotString + '\'')
            if (mainActivity.gameDetails.gameOver || mainActivity.gameDetails.shotCondition == ShotCondition.BREAK) {
                mainActivity.speakScore()
            }
        }
        binding.defenseCheckBox.setOnClickListener {
            if (mainActivity.gameDetails.shotCondition == ShotCondition.BREAK) {
                (it as CheckBox).isChecked = false
                Toast.makeText(context, getString(R.string.break_defense_error), Toast.LENGTH_LONG)
                    .show()
            }
        }
        binding.bankCheckBox.setOnClickListener {
            if (mainActivity.gameDetails.shotCondition == ShotCondition.BREAK) {
                (it as CheckBox).isChecked = false
                Toast.makeText(context, getString(R.string.break_bank_error), Toast.LENGTH_LONG)
                    .show()
            }
        }
        binding.luckyCheckBox.setOnClickListener {
            if (mainActivity.gameDetails.shotCondition == ShotCondition.BREAK) {
                (it as CheckBox).isChecked = false
                Toast.makeText(context, getString(R.string.break_lucky_error), Toast.LENGTH_LONG)
                    .show()
            }
        }
        binding.timeOutCheckBox.setOnClickListener {
            if (mainActivity.gameDetails.shotCondition == ShotCondition.BREAK) {
                (it as CheckBox).isChecked = false
                Toast.makeText(context, getString(R.string.break_time_out_error), Toast.LENGTH_LONG)
                    .show()
            }
            val lastRack = mainActivity.gameDetails.rackList.last()
            val timeOuts = if (mainActivity.gameDetails.gameStats.playerTurn == PlayerTurn.PLAYER1)
                lastRack.player1TimeOuts else
                lastRack.player2TimeOuts
            val timeOutLimit = if (mainActivity.gameDetails.gameStats.playerTurn == PlayerTurn.PLAYER1)
                (if (mainActivity.gameDetails.player1Goal <= 25) 2 else 1) else
                (if (mainActivity.gameDetails.player2Goal <= 25) 2 else 1)
            if (timeOuts >= timeOutLimit) {
                (it as CheckBox).isChecked = false
                Toast.makeText(context, getString(
                    if (timeOutLimit == 2) R.string.time_out_limit_two_error else R.string.time_out_limit_one_error
                ), Toast.LENGTH_LONG)
                    .show()
            }
        }

        binding.player1Label.text = mainActivity.gameDetails.player1Name
        binding.player2Label.text = mainActivity.gameDetails.player2Name
        binding.player1Goal.text =
            if (mainActivity.gameDetails.player1Goal == -1) "" else String.format(
                getString(R.string.score_goal),
                mainActivity.gameDetails.player1Goal
            )
        binding.player2Goal.text =
            if (mainActivity.gameDetails.player2Goal == -1) "" else String.format(
                getString(R.string.score_goal),
                mainActivity.gameDetails.player2Goal
            )
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
        with(mainActivity.gameDetails.gameStats) {
            binding.player1Score.text = player1Stats.score.toString()
            binding.player2Score.text = player2Stats.score.toString()
            if (playerTurn == PlayerTurn.PLAYER1) {
                binding.player1Label.setTextColor(resources.getColor(R.color.yourTurn))
                binding.player1Score.setTextColor(resources.getColor(R.color.yourTurn))
                binding.player1Goal.setTextColor(resources.getColor(R.color.yourTurn))
                binding.player2Label.setTextColor(resources.getColor(R.color.notYourTurn))
                binding.player2Score.setTextColor(resources.getColor(R.color.notYourTurn))
                binding.player2Goal.setTextColor(resources.getColor(R.color.notYourTurn))
            }
            if (playerTurn == PlayerTurn.PLAYER2) {
                binding.player1Label.setTextColor(resources.getColor(R.color.notYourTurn))
                binding.player1Score.setTextColor(resources.getColor(R.color.notYourTurn))
                binding.player1Goal.setTextColor(resources.getColor(R.color.notYourTurn))
                binding.player2Label.setTextColor(resources.getColor(R.color.yourTurn))
                binding.player2Score.setTextColor(resources.getColor(R.color.yourTurn))
                binding.player2Goal.setTextColor(resources.getColor(R.color.yourTurn))
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
        for (index in ballTextList.indices) {
            ballTextList[index].text = when (mainActivity.gameDetails.ballStatus[index]) {
                BallStatus.ON_TABLE -> ""
                BallStatus.SCORED_THIS_TURN -> getString(R.string.this_turn)
                BallStatus.DEAD -> getString(R.string.dead)
                BallStatus.PLAYER1 -> mainActivity.gameDetails.player1Name
                    ?: getString(R.string.player1)

                BallStatus.PLAYER2 -> mainActivity.gameDetails.player2Name
                    ?: getString(R.string.player2)
            }
        }
        binding.ballInHandTextView.visibility =
            if (mainActivity.gameDetails.shotCondition == ShotCondition.BALL_IN_HAND) View.VISIBLE else View.GONE
        binding.badBreakButton.visibility =
            if (mainActivity.gameDetails.shotCondition == ShotCondition.BREAK) View.VISIBLE else View.GONE
        if (mainActivity.gameDetails.player1MatchPoints == -1 || mainActivity.gameDetails.player2MatchPoints == -1) {
            binding.mp1Label.visibility = View.GONE
            binding.mp1Score.visibility = View.GONE
            binding.mp2Label.visibility = View.GONE
            binding.mp2Score.visibility = View.GONE
            binding.timeOutLabel.visibility = View.GONE
            binding.timeOutCheckBox.visibility = View.GONE
        } else {
            binding.mp1Label.visibility = View.VISIBLE
            binding.mp1Score.visibility = View.VISIBLE
            binding.mp2Label.visibility = View.VISIBLE
            binding.mp2Score.visibility = View.VISIBLE
            binding.timeOutLabel.visibility = View.VISIBLE
            binding.timeOutCheckBox.visibility = View.VISIBLE
            binding.mp1Score.text = mainActivity.gameDetails.player1MatchPoints.toString()
            binding.mp2Score.text = mainActivity.gameDetails.player2MatchPoints.toString()
        }
        selected.fill(false)
        binding.foulGroup.check(R.id.legalShot)
        binding.defenseCheckBox.isChecked = false
        binding.eclipseCheckBox.isChecked = false
        binding.bankCheckBox.isChecked = false
        binding.luckyCheckBox.isChecked = false
        binding.miscueCheckBox.isChecked = false
        binding.timeOutCheckBox.isChecked = false
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
        if (string.contains('W') && mainActivity.gameDetails.ballStatus.none { it == BallStatus.ON_TABLE }) {
            Toast.makeText(
                context,
                getString(R.string.wrong_first_on_nine_error),
                Toast.LENGTH_LONG
            ).show()
            return
        }
        if (string.contains('b') && string.contains('d')) {
            Toast.makeText(context, getString(R.string.bank_and_defense_error), Toast.LENGTH_LONG)
                .show()
            return
        }
        val playerTurn = mainActivity.gameDetails.gameStats.playerTurn
        val shotCondition = mainActivity.gameDetails.shotCondition
        if (string.contains('m') && !string.contains('B') && shotCondition == ShotCondition.BREAK) {
            Toast.makeText(context, getString(R.string.miscue_good_break_error), Toast.LENGTH_LONG)
                .show()
            return
        }
        val ballStatus = Arrays.copyOf(mainActivity.gameDetails.ballStatus, 9)
        val shot = Shot(string, playerTurn, shotCondition, ballStatus)
        val newString = when {
            shot.isTurnEnd && playerTurn == PlayerTurn.PLAYER1 -> string + ','
            shot.isTurnEnd && playerTurn == PlayerTurn.PLAYER2 -> string + ';'
            else -> string
        }
        mainActivity.updateGameString(mainActivity.gameString + newString)
        when {
            shot.shotPlayerStats.achievements.breakAndRun == 1 -> mainActivity.speak(
                String.format(
                    getString(R.string.congrats_break_and_run),
                    if (playerTurn == PlayerTurn.PLAYER1)
                        mainActivity.gameDetails.pronunciation1
                    else
                        mainActivity.gameDetails.pronunciation2
                )
            )

            shot.shotPlayerStats.achievements.perfectRack == 1 -> mainActivity.speak(
                String.format(
                    getString(R.string.congrats_perfect_rack),
                    if (playerTurn == PlayerTurn.PLAYER1)
                        mainActivity.gameDetails.pronunciation1
                    else
                        mainActivity.gameDetails.pronunciation2
                )
            )

            shot.shotPlayerStats.achievements.nineOnBreak == 1 -> mainActivity.speak(
                String.format(
                    getString(R.string.congrats_nine_on_break),
                    if (playerTurn == PlayerTurn.PLAYER1)
                        mainActivity.gameDetails.pronunciation1
                    else
                        mainActivity.gameDetails.pronunciation2
                )
            )
        }
        if (shot.shotPlayerStats.score != 0) {
            if (mainActivity.gameDetails.announceStreak1 > 0 && mainActivity.gameDetails.gameStats.player1Stats.streaks.currentStreak >= mainActivity.gameDetails.announceStreak1) {
                mainActivity.speak(
                    String.format(
                        getString(R.string.current_streak),
                        mainActivity.gameDetails.pronunciation1,
                        mainActivity.gameDetails.gameStats.player1Stats.streaks.currentStreak
                    )
                )
            }
            if (mainActivity.gameDetails.announceStreak2 > 0 && mainActivity.gameDetails.gameStats.player2Stats.streaks.currentStreak >= mainActivity.gameDetails.announceStreak2) {
                mainActivity.speak(
                    String.format(
                        getString(R.string.current_streak),
                        mainActivity.gameDetails.pronunciation2,
                        mainActivity.gameDetails.gameStats.player2Stats.streaks.currentStreak
                    )
                )
            }
        }
        updateScores()
    }

    fun enableButtons() {
        areButtonsEnabled = true
        binding.shotButton.alpha = 1.toFloat()
        binding.undoButton.alpha = 1.toFloat()
        binding.badBreakButton.alpha = 1.toFloat()
    }

    fun disableButtons() {
        areButtonsEnabled = false
        binding.shotButton.alpha = 0.25.toFloat()
        binding.undoButton.alpha = 0.25.toFloat()
        binding.badBreakButton.alpha = 0.25.toFloat()
        object : CountDownTimer(1000, 1000) {
            override fun onTick(p0: Long) {}
            override fun onFinish() {
                enableButtons()
            }
        }.start()
    }
}

