package com.mathgeniusguide.nineballscorekeeper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mathgeniusguide.nineballscorekeeper.adapters.HistoryAdapter
import com.mathgeniusguide.nineballscorekeeper.databinding.HistoryFragmentBinding

class HistoryFragment: Fragment() {
    private lateinit var mainActivity: MainActivity
    private var _binding: HistoryFragmentBinding? = null
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
        _binding = HistoryFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.historyRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.historyRecyclerView.adapter = HistoryAdapter(mainActivity.gameList, requireContext(), mainActivity)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

