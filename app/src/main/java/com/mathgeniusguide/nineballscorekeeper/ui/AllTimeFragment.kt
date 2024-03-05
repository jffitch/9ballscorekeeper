package com.mathgeniusguide.nineballscorekeeper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mathgeniusguide.nineballscorekeeper.adapters.StatAdapter
import com.mathgeniusguide.nineballscorekeeper.databinding.AllTimeFragmentBinding
import com.mathgeniusguide.nineballscorekeeper.databinding.PlayerStatsFragmentBinding
import com.mathgeniusguide.nineballscorekeeper.objects.stats.AllTimeStats
import com.mathgeniusguide.nineballscorekeeper.objects.stats.Stat
import com.mathgeniusguide.nineballscorekeeper.util.getGameDetails

class AllTimeFragment : Fragment() {
    private lateinit var mainActivity: MainActivity
    private var _binding: AllTimeFragmentBinding? = null
    private lateinit var player1AllTimeStats: AllTimeStats
    private lateinit var player2AllTimeStats: AllTimeStats

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
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
        _binding = AllTimeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.player1Name.setText(mainActivity.gameDetails.player1Name)
        binding.player2Name.setText(mainActivity.gameDetails.player2Name)
        updateRecyclerView()
        binding.player1Name.doOnTextChanged { text, start, before, count ->
            updateRecyclerView()
        }
        binding.player2Name.doOnTextChanged { text, start, before, count ->
            updateRecyclerView()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun updateRecyclerView() {
        val statList = mutableListOf<Stat>()
        player1AllTimeStats = AllTimeStats(binding.player1Name.text.toString())
        player2AllTimeStats = AllTimeStats(binding.player2Name.text.toString())
        for (game in mainActivity.gameList) {
            player1AllTimeStats.add(getGameDetails(game))
            player2AllTimeStats.add(getGameDetails(game))
        }
        statList.addAll(
            listOf(
                Stat("Games Played"),
                Stat(
                    "Games Played",
                    player1AllTimeStats.gamesPlayed.toString(),
                    player2AllTimeStats.gamesPlayed.toString()
                ),
                Stat(
                    "Innings Range",
                    String.format(
                        getString(R.string.score_score),
                        player1AllTimeStats.minInnings,
                        player1AllTimeStats.maxInnings
                    ),
                    String.format(
                        getString(R.string.score_score),
                        player2AllTimeStats.minInnings,
                        player2AllTimeStats.maxInnings
                    )
                ),
                Stat(
                    "Innings Average",
                    String.format(
                        "%.2f",
                        player1AllTimeStats.totalInnings.toDouble() / player1AllTimeStats.gamesPlayed.toDouble()
                    ),
                    String.format(
                        "%.2f",
                        player2AllTimeStats.totalInnings.toDouble() / player2AllTimeStats.gamesPlayed.toDouble()
                    )
                ),
                Stat("Score"),
                Stat(
                    "Score Range",
                    String.format(
                        getString(R.string.score_score),
                        player1AllTimeStats.minScore,
                        player1AllTimeStats.maxScore
                    ),
                    String.format(
                        getString(R.string.score_score),
                        player2AllTimeStats.minScore,
                        player2AllTimeStats.maxScore
                    )
                ),
                Stat(
                    "Score Average",
                    String.format(
                        "%.2f",
                        player1AllTimeStats.totalScore.toDouble() / player1AllTimeStats.gamesPlayed.toDouble()
                    ),
                    String.format(
                        "%.2f",
                        player2AllTimeStats.totalScore.toDouble() / player2AllTimeStats.gamesPlayed.toDouble()
                    )
                ),
                Stat(
                    "Score 0-9",
                    player1AllTimeStats.score_0_9.toString(),
                    player2AllTimeStats.score_0_9.toString()
                ),
                Stat(
                    "Score 10-19",
                    player1AllTimeStats.score_10_19.toString(),
                    player2AllTimeStats.score_10_19.toString()
                ),
                Stat(
                    "Score 20-29",
                    player1AllTimeStats.score_20_29.toString(),
                    player2AllTimeStats.score_20_29.toString()
                ),
                Stat(
                    "Score 30-39",
                    player1AllTimeStats.score_30_39.toString(),
                    player2AllTimeStats.score_30_39.toString()
                ),
                Stat(
                    "Score 40-49",
                    player1AllTimeStats.score_40_49.toString(),
                    player2AllTimeStats.score_40_49.toString()
                ),
                Stat(
                    "Score 50-59",
                    player1AllTimeStats.score_50_59.toString(),
                    player2AllTimeStats.score_50_59.toString()
                ),
                Stat(
                    "Score 60-69",
                    player1AllTimeStats.score_60_69.toString(),
                    player2AllTimeStats.score_60_69.toString()
                ),
                Stat(
                    "Score 70-79",
                    player1AllTimeStats.score_70_79.toString(),
                    player2AllTimeStats.score_70_79.toString()
                ),
                Stat(
                    "Score 80-89",
                    player1AllTimeStats.score_80_89.toString(),
                    player2AllTimeStats.score_80_89.toString()
                ),
                Stat(
                    "Score 90-99",
                    player1AllTimeStats.score_90_99.toString(),
                    player2AllTimeStats.score_90_99.toString()
                ),
                Stat(
                    "Score Over 100",
                    player1AllTimeStats.score_100_plus.toString(),
                    player2AllTimeStats.score_100_plus.toString()
                ),
                Stat(
                    "Streak Range",
                    String.format(
                        getString(R.string.score_score),
                        player1AllTimeStats.minStreak,
                        player1AllTimeStats.maxStreak
                    ),
                    String.format(
                        getString(R.string.score_score),
                        player2AllTimeStats.minStreak,
                        player2AllTimeStats.maxStreak
                    )
                ),
                Stat(
                    "Streak Average",
                    String.format(
                        "%.2f",
                        player1AllTimeStats.totalStreak.toDouble() / player1AllTimeStats.gamesPlayed.toDouble()
                    ),
                    String.format(
                        "%.2f",
                        player2AllTimeStats.totalStreak.toDouble() / player2AllTimeStats.gamesPlayed.toDouble()
                    )
                ),
                Stat(
                    "Turn Streak Range",
                    String.format(
                        getString(R.string.score_score),
                        player1AllTimeStats.minTurnStreak,
                        player1AllTimeStats.maxTurnStreak
                    ),
                    String.format(
                        getString(R.string.score_score),
                        player2AllTimeStats.minTurnStreak,
                        player2AllTimeStats.maxTurnStreak
                    )
                ),
                Stat(
                    "Turn Streak Average",
                    String.format(
                        "%.2f",
                        player1AllTimeStats.totalTurnStreak.toDouble() / player1AllTimeStats.gamesPlayed.toDouble()
                    ),
                    String.format(
                        "%.2f",
                        player2AllTimeStats.totalTurnStreak.toDouble() / player2AllTimeStats.gamesPlayed.toDouble()
                    )
                ),
                Stat("9 Balls"),
                Stat(
                    "9 Ball Average",
                    String.format(
                        "%.2f",
                        player1AllTimeStats.totalNines.toDouble() / player1AllTimeStats.gamesPlayed.toDouble()
                    ),
                    String.format(
                        "%.2f",
                        player2AllTimeStats.totalNines.toDouble() / player2AllTimeStats.gamesPlayed.toDouble()
                    )
                ),
                Stat(
                    "Breaks And Runs",
                    player1AllTimeStats.totalBreakRun.toString(),
                    player2AllTimeStats.totalBreakRun.toString()
                ),
                Stat(
                    "Perfect Racks",
                    player1AllTimeStats.totalPerfectRack.toString(),
                    player2AllTimeStats.totalPerfectRack.toString()
                ),
                Stat(
                    "9s On Break",
                    player1AllTimeStats.totalNineBreak.toString(),
                    player2AllTimeStats.totalNineBreak.toString()
                ),
                Stat("Fouls"),
                Stat(
                    "Fouls Range",
                    String.format(
                        getString(R.string.score_score),
                        player1AllTimeStats.minFouls,
                        player1AllTimeStats.maxFouls
                    ),
                    String.format(
                        getString(R.string.score_score),
                        player2AllTimeStats.minFouls,
                        player2AllTimeStats.maxFouls
                    )
                ),
                Stat(
                    "Fouls Average",
                    String.format(
                        "%.2f",
                        player1AllTimeStats.totalFouls.toDouble() / player1AllTimeStats.gamesPlayed.toDouble()
                    ),
                    String.format(
                        "%.2f",
                        player2AllTimeStats.totalFouls.toDouble() / player2AllTimeStats.gamesPlayed.toDouble()
                    )
                ),
                Stat(
                    "Foul-Inning Ratio Range",
                    String.format(
                        getString(R.string.percent_range),
                        player1AllTimeStats.minFoulInningRatio * 100,
                        player1AllTimeStats.maxFoulInningRatio * 100
                    ),
                    String.format(
                        getString(R.string.percent_range),
                        player2AllTimeStats.minFoulInningRatio * 100,
                        player2AllTimeStats.maxFoulInningRatio * 100
                    ),
                ),
                Stat(
                    "Foul-Inning Ratio Avg",
                    String.format(
                        getString(R.string.percent),
                        player1AllTimeStats.totalFouls.toDouble() / player1AllTimeStats.totalInnings.toDouble() * 100
                    ),
                    String.format(
                        getString(R.string.percent),
                        player2AllTimeStats.totalFouls.toDouble() / player2AllTimeStats.totalInnings.toDouble() * 100
                    )
                ),
                Stat(
                    "Foul-Point Ratio Range",
                    String.format(
                        getString(R.string.percent_range),
                        player1AllTimeStats.minFoulPointRatio * 100,
                        player1AllTimeStats.maxFoulPointRatio * 100
                    ),
                    String.format(
                        getString(R.string.percent_range),
                        player2AllTimeStats.minFoulPointRatio * 100,
                        player2AllTimeStats.maxFoulPointRatio * 100
                    ),
                ),
                Stat(
                    "Foul-Point Ratio Avg",
                    String.format(
                        getString(R.string.percent),
                        player1AllTimeStats.totalFouls.toDouble() / player1AllTimeStats.totalScore.toDouble() * 100
                    ),
                    String.format(
                        getString(R.string.percent),
                        player2AllTimeStats.totalFouls.toDouble() / player2AllTimeStats.totalScore.toDouble() * 100
                    )
                ),
                Stat(
                    "More Fouls Than Points",
                    player1AllTimeStats.moreFoulsThanPoints.toString(),
                    player2AllTimeStats.moreFoulsThanPoints.toString()
                ),
            )
        )
        binding.allTimeStatsRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.allTimeStatsRecyclerView.adapter = StatAdapter(statList, requireContext())
    }
}

