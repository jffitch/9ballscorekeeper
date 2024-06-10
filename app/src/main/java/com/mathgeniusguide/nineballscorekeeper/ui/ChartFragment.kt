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

        mainActivity.calculateChartList()

        binding.chartRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.chartRecyclerView.adapter = ChartAdapter(mainActivity.chartList, requireContext(), mainActivity)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

