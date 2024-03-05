package com.mathgeniusguide.nineballscorekeeper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mathgeniusguide.nineballscorekeeper.adapters.RackAdapter
import com.mathgeniusguide.nineballscorekeeper.databinding.RackStatsFragmentBinding

class RackStatsFragment: Fragment() {
    private lateinit var mainActivity: MainActivity
    private var _binding: RackStatsFragmentBinding? = null
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
        _binding = RackStatsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rackRecyclerView.layoutManager = LinearLayoutManager(context)
        val showTimeOuts = mainActivity.gameDetails.player1MatchPoints != -1 && mainActivity.gameDetails.player2MatchPoints != -1
        binding.rackLabels.player1TimeOutsTextView.visibility = if (showTimeOuts) View.VISIBLE else View.GONE
        binding.rackLabels.player2TimeOutsTextView.visibility = if (showTimeOuts) View.VISIBLE else View.GONE
        binding.rackRecyclerView.adapter = RackAdapter(
            mainActivity.gameDetails.rackList,
            showTimeOuts,
            requireContext())
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

