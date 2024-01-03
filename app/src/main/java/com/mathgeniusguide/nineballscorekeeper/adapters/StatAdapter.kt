package com.mathgeniusguide.nineballscorekeeper.adapters

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mathgeniusguide.nineballscorekeeper.R
import com.mathgeniusguide.nineballscorekeeper.databinding.StatsItemBinding
import com.mathgeniusguide.nineballscorekeeper.objects.stats.Stat

class StatAdapter (private val items: List<Stat>, private val context: Context) : RecyclerView.Adapter<StatAdapter.ViewHolder> () {
    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.stats_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val i = items[position]
        holder.binding.statLabel.text = i.statLabel
        holder.binding.statPlayer1.text = i.player1Stat
        holder.binding.statPlayer2.text = i.player2Stat
        if (i.player1Stat.isEmpty() && i.player2Stat.isEmpty()) {
            holder.binding.statLabel.typeface = Typeface.DEFAULT_BOLD
            holder.binding.statLabel.textSize = 18.toFloat()
        } else {
            holder.binding.statLabel.typeface = Typeface.DEFAULT
            holder.binding.statLabel.textSize = 14.toFloat()
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = StatsItemBinding.bind(view)
    }
}