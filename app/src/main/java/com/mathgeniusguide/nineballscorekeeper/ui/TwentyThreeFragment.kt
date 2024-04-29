package com.mathgeniusguide.nineballscorekeeper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.mathgeniusguide.nineballscorekeeper.databinding.TwentyThreeFragmentBinding

class TwentyThreeFragment: Fragment() {
    private lateinit var mainActivity: MainActivity
    private var _binding: TwentyThreeFragmentBinding? = null
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
        _binding = TwentyThreeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rankList.doOnTextChanged { text, start, before, count ->
            calculateAllowedRanks()
        }
        binding.ranksRequired.doOnTextChanged { text, start, before, count ->
            calculateAllowedRanks()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun calculateAllowedRanks() {
        val rankList = binding.rankList.text.toString().toList().sortedDescending().joinToString("")
        val ranksRequired = binding.ranksRequired.text.toString().toList().sortedDescending().joinToString("")
        val ranksAllowedList = mutableListOf<String>()
        val ranksNotAllowedList = mutableListOf<String>()
        val rankListLength = rankList.length
        if (rankListLength >= 5) {
            for (i in 0 until rankListLength - 4) {
                for (j in i + 1 until rankListLength - 3) {
                    for (k in j + 1 until rankListLength - 2) {
                        for (l in k + 1 until rankListLength - 1) {
                            for (m in l + 1 until rankListLength) {
                                val rankString =
                                    "" + rankList[i] + rankList[j] + rankList[k] + rankList[l] + rankList[m]
                                if (matchesRequiredRanks(rankString, ranksRequired)) {
                                    if (isAllowed(rankString)) {
                                        if (!ranksAllowedList.contains(rankString)) {
                                            ranksAllowedList.add(rankString)
                                        }
                                    } else {
                                        if (!ranksNotAllowedList.contains(rankString)) {
                                            ranksNotAllowedList.add(rankString)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (rankListLength >= 4 && ranksAllowedList.isEmpty()) {
            for (i in 0 until rankListLength - 3) {
                for (j in i + 1 until rankListLength - 2) {
                    for (k in j + 1 until rankListLength - 1) {
                        for (l in k + 1 until rankListLength) {
                            for (m in listOf(i, j, k, l)) {
                                val rankString =
                                    "" + rankList[i] + rankList[j] + rankList[k] + rankList[l] + "(" + rankList[m] + ")"
                                if (matchesRequiredRanks(rankString, ranksRequired)) {
                                    if (isAllowed(rankString)) {
                                        if (!ranksAllowedList.contains(rankString)) {
                                            ranksAllowedList.add(rankString)
                                        }
                                    } else {
                                        if (!ranksNotAllowedList.contains(rankString)) {
                                            ranksNotAllowedList.add(rankString)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        binding.allowedList1.text = ranksAllowedList.filterIndexed { index, value -> index % 2 == 0 }.joinToString("\n")
        binding.allowedList2.text = ranksAllowedList.filterIndexed { index, value -> index % 2 == 1 }.joinToString("\n")
        binding.notAllowedList1.text = ranksNotAllowedList.filterIndexed { index, value -> index % 2 == 0 }.joinToString("\n")
        binding.notAllowedList2.text = ranksNotAllowedList.filterIndexed { index, value -> index % 2 == 1 }.joinToString("\n")
    }

    fun isAllowed(rankString: String): Boolean {
        var totalRanks = 0
        var seniorCount = 0
        for (i in rankString) {
            val num = i.digitToIntOrNull() ?: 0
            totalRanks += num
            if (num >= 6) {
                seniorCount++
            }
        }
        return seniorCount <= 2 && totalRanks <= 23
    }

    fun matchesRequiredRanks(rankString: String, requiredRanks: String): Boolean {
        val newRankString = rankString.substringBefore('(')
        return requiredRanks.all { requiredRank -> newRankString.count { it == requiredRank } >= requiredRanks.count { it == requiredRank } }
    }
}

