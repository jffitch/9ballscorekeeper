package com.mathgeniusguide.nineballscorekeeper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mathgeniusguide.nineballscorekeeper.adapters.StatAdapter
import com.mathgeniusguide.nineballscorekeeper.databinding.PlayerStatsFragmentBinding
import com.mathgeniusguide.nineballscorekeeper.objects.stats.Stat

class PlayerStatsFragment: Fragment() {
    private lateinit var mainActivity: MainActivity
    private var _binding: PlayerStatsFragmentBinding? = null
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
        _binding = PlayerStatsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val statList = mutableListOf<Stat>()
        with (mainActivity.gameDetails.gameStats) {
            statList.addAll(listOf(
                Stat("Score"),
                Stat("Score", player1Stats.score.toString(), player2Stats.score.toString()),
                Stat("Best Streak", player1Stats.streaks.streak.toString(), player2Stats.streaks.streak.toString()),
                Stat("Best Turn", player1Stats.streaks.turnStreak.toString(), player2Stats.streaks.turnStreak.toString()),
                Stat("Current Streak", player1Stats.streaks.currentStreak.toString(), player2Stats.streaks.currentStreak.toString()),
                Stat("Current Turn", player1Stats.streaks.currentTurnStreak.toString(), player2Stats.streaks.currentTurnStreak.toString()),
                Stat("Dead Balls", player1Stats.deadBalls.toString(), player2Stats.deadBalls.toString()),
                Stat("Shots Taken", player1Stats.shotsTaken.toString(), player2Stats.shotsTaken.toString()),
                Stat("9 Balls"),
                Stat("Legal 9", player1Stats.achievements.nines.toString(), player2Stats.achievements.nines.toString()),
                Stat("Early 9", player1Stats.achievements.earlyNine.toString(), player2Stats.achievements.earlyNine.toString()),
                Stat("9 On Break", player1Stats.achievements.nineOnBreak.toString(), player2Stats.achievements.nineOnBreak.toString()),
                Stat("Perfect Rack", player1Stats.achievements.perfectRack.toString(), player2Stats.achievements.perfectRack.toString()),
                Stat("Break And Run", player1Stats.achievements.breakAndRun.toString(), player2Stats.achievements.breakAndRun.toString()),
                Stat("Illegal 9", player1Stats.achievements.illegalNine.toString(), player2Stats.achievements.illegalNine.toString()),
                Stat("Breaks"),
                Stat("Dry Break", player1Stats.achievements.pointsOnBreak[0].toString(), player2Stats.achievements.pointsOnBreak[0].toString()),
                Stat("Break Score 1", player1Stats.achievements.pointsOnBreak[1].toString(), player2Stats.achievements.pointsOnBreak[1].toString()),
                Stat("Break Score 2", player1Stats.achievements.pointsOnBreak[2].toString(), player2Stats.achievements.pointsOnBreak[2].toString()),
                Stat("Break Score 3", player1Stats.achievements.pointsOnBreak[3].toString(), player2Stats.achievements.pointsOnBreak[3].toString()),
                Stat("Break Score 4", player1Stats.achievements.pointsOnBreak[4].toString(), player2Stats.achievements.pointsOnBreak[4].toString()),
                Stat("Break Score 5", player1Stats.achievements.pointsOnBreak[5].toString(), player2Stats.achievements.pointsOnBreak[5].toString()),
                Stat("Break Score 6", player1Stats.achievements.pointsOnBreak[6].toString(), player2Stats.achievements.pointsOnBreak[6].toString()),
                Stat("Break Score 7", player1Stats.achievements.pointsOnBreak[7].toString(), player2Stats.achievements.pointsOnBreak[7].toString()),
                Stat("Break Score 8", player1Stats.achievements.pointsOnBreak[8].toString(), player2Stats.achievements.pointsOnBreak[8].toString()),
                Stat("Break Score 9", player1Stats.achievements.pointsOnBreak[9].toString(), player2Stats.achievements.pointsOnBreak[9].toString()),
                Stat("Break Score 10", player1Stats.achievements.pointsOnBreak[10].toString(), player2Stats.achievements.pointsOnBreak[10].toString()),
                Stat("Scratch On Break", player1Stats.achievements.scratchOnBreak.toString(), player2Stats.achievements.scratchOnBreak.toString()),
                Stat("Miscue On Break", player1Stats.achievements.miscueBreak.toString(), player2Stats.achievements.miscueBreak.toString()),
                Stat("Bad Break", player1Stats.fouls.badBreak.toString(), player2Stats.fouls.badBreak.toString()),
                Stat("Defenses"),
                Stat("Total", player1Stats.achievements.defense.total.toString(), player2Stats.achievements.defense.total.toString()),
                Stat("Success", player1Stats.achievements.defense.success.toString(), player2Stats.achievements.defense.success.toString()),
                Stat("Failure", player1Stats.achievements.defense.failure.toString(), player2Stats.achievements.defense.failure.toString()),
                Stat("Eclipses"),
                Stat("Eclipse", player1Stats.achievements.eclipse.toString(), player2Stats.achievements.eclipse.toString()),
                Stat("Intentional Eclipse", player1Stats.achievements.intentionalEclipse.toString(), player2Stats.achievements.intentionalEclipse.toString()),
                Stat("Eclipse Outcomes"),
                Stat("Eclipse Escape", player1Stats.achievements.eclipseEscape.toString(), player2Stats.achievements.eclipseEscape.toString()),
                Stat("Eclipse Pocket", player1Stats.achievements.eclipsePocket.toString(), player2Stats.achievements.eclipsePocket.toString()),
                Stat("Eclipse Return", player1Stats.achievements.eclipseReturn.toString(), player2Stats.achievements.eclipseReturn.toString()),
                Stat("Self Eclipses"),
                Stat("Self Eclipse", player1Stats.achievements.selfEclipse.toString(), player2Stats.achievements.selfEclipse.toString()),
                Stat("Self Eclipse Escape", player1Stats.achievements.selfEclipseEscape.toString(), player2Stats.achievements.selfEclipseEscape.toString()),
                Stat("Self Eclipse Pocket", player1Stats.achievements.selfEclipsePocket.toString(), player2Stats.achievements.selfEclipsePocket.toString()),
                Stat("Self Eclipse Return", player1Stats.achievements.selfEclipseReturn.toString(), player2Stats.achievements.selfEclipseReturn.toString()),
                Stat("Banks"),
                Stat("Total", player1Stats.achievements.bank.total.toString(), player2Stats.achievements.bank.total.toString()),
                Stat("Success", player1Stats.achievements.bank.success.toString(), player2Stats.achievements.bank.success.toString()),
                Stat("Failure", player1Stats.achievements.bank.failure.toString(), player2Stats.achievements.bank.failure.toString()),
                Stat("BIH Blunders"),
                Stat("Ball In Hand Miss", player1Stats.achievements.ballInHandMiss.toString(), player2Stats.achievements.ballInHandMiss.toString()),
                Stat("Ball In Hand Return", player1Stats.achievements.ballInHandReturn.toString(), player2Stats.achievements.ballInHandReturn.toString()),
            ))
            if (mainActivity.gameDetails.player1MatchPoints != -1 && mainActivity.gameDetails.player2MatchPoints != -1) {
                statList.addAll(
                    listOf(
                        Stat("Time Outs"),
                        Stat(
                            "Total Time Outs",
                            player1Stats.achievements.timeOut.toString(),
                            player2Stats.achievements.timeOut.toString()
                        ),
                        Stat(
                            "Defense On Time Out",
                            player1Stats.achievements.timeOutDefense.toString(),
                            player2Stats.achievements.timeOutDefense.toString()
                        ),
                        Stat(
                            "Foul On Time Out",
                            player1Stats.achievements.timeOutFoul.toString(),
                            player2Stats.achievements.timeOutFoul.toString()
                        ),
                        Stat(
                            "Miss On Time Out",
                            player1Stats.achievements.timeOutMiss.toString(),
                            player2Stats.achievements.timeOutMiss.toString()
                        ),
                        Stat(
                            "Pocket On Time Out",
                            player1Stats.achievements.timeOutPocket.toString(),
                            player2Stats.achievements.timeOutPocket.toString()
                        ),
                    )
                )
            }
            statList.addAll(listOf(
                Stat("Fouls"),
                Stat("Total Fouls", player1Stats.fouls.total().toString(), player2Stats.fouls.total().toString()),
                Stat("Scratch", player1Stats.fouls.scratch.toString(), player2Stats.fouls.scratch.toString()),
                Stat("Miss", player1Stats.fouls.miss.toString(), player2Stats.fouls.miss.toString()),
                Stat("Wrong Ball First", player1Stats.fouls.wrongBallFirst.toString(), player2Stats.fouls.wrongBallFirst.toString()),
                Stat("No Rail", player1Stats.fouls.noRail.toString(), player2Stats.fouls.noRail.toString()),
                Stat("Jump Off Table", player1Stats.fouls.jumpOffTable.toString(), player2Stats.fouls.jumpOffTable.toString()),
                Stat("Other Fouls", player1Stats.fouls.other.toString(), player2Stats.fouls.other.toString()),
                Stat(
                    "Foul-Inning Ratio",
                    String.format(getString(R.string.percent), 100 * player1Stats.fouls.total().toDouble() / mainActivity.gameDetails.gameStats.innings.toDouble()),
                    String.format(getString(R.string.percent), 100 * player2Stats.fouls.total().toDouble() / mainActivity.gameDetails.gameStats.innings.toDouble())
                ),
                Stat(
                    "Foul-Point Ratio",
                    String.format(getString(R.string.percent), 100 * player1Stats.fouls.total().toDouble() / player1Stats.score.toDouble()),
                    String.format(getString(R.string.percent), 100 * player2Stats.fouls.total().toDouble() / player2Stats.score.toDouble())
                ),
                Stat("Miscues"),
                Stat("Miscue", player1Stats.achievements.miscue.toString(), player2Stats.achievements.miscue.toString()),
                Stat("Miscue And Hit", player1Stats.achievements.miscueHit.toString(), player2Stats.achievements.miscueHit.toString()),
                Stat("Miscue And Pocket", player1Stats.achievements.miscuePocket.toString(), player2Stats.achievements.miscuePocket.toString()),
                Stat("Other"),
                Stat("Lucky Shot Points", player1Stats.achievements.lucky.toString(), player2Stats.achievements.lucky.toString())
            ))
        }
        binding.playerStatsRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.playerStatsRecyclerView.adapter = StatAdapter(statList, requireContext())
        binding.statLabels.statPlayer1.text = mainActivity.gameDetails.player1Name
        binding.statLabels.statPlayer2.text = mainActivity.gameDetails.player2Name
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

