package com.mathgeniusguide.nineballscorekeeper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mathgeniusguide.nineballscorekeeper.adapters.ChartAdapter
import com.mathgeniusguide.nineballscorekeeper.adapters.HistoryAdapter
import com.mathgeniusguide.nineballscorekeeper.databinding.ChartFragmentBinding
import com.mathgeniusguide.nineballscorekeeper.enums.PlayerTurn
import com.mathgeniusguide.nineballscorekeeper.objects.ChartShot

class ChartFragment: Fragment() {
    private lateinit var mainActivity: MainActivity
    private var _binding: ChartFragmentBinding? = null
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
        _binding = ChartFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.player1Label.text = mainActivity.gameDetails.player1Name
        binding.player2Label.text = mainActivity.gameDetails.player2Name
        val chartList = mutableListOf<ChartShot>()
        var isRackStart = true
        var rackNumber = mainActivity.gameDetails.startRacks
        var inningNumber = mainActivity.gameDetails.startInnings
        for (pair in mainActivity.gameDetails.innings) {
            var isInningStart = true
            val player1Shots = pair.first.substringBeforeLast('\'').split("'")
            val player2Shots = pair.second.substringBeforeLast('\'').split("'")
            for (shot in player1Shots) {
                val isFoul = "[KMWRPO]".toRegex().containsMatchIn(shot)
                val isNine = shot.contains('9') && !isFoul
                val isStalemate = shot.contains('S')
                val isTimeOut = shot.contains('t')
                val isDefense = shot.contains('d')
                chartList.add(ChartShot(
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
                ))
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
                chartList.add(ChartShot(
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
                ))
                isRackStart = isNine || isStalemate
                if (isRackStart) {
                    rackNumber++
                }
            }
            inningNumber++
        }

        binding.chartRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.chartRecyclerView.adapter = ChartAdapter(chartList, requireContext(), mainActivity)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

