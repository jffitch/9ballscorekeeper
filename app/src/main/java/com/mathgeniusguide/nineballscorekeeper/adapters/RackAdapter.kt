package com.mathgeniusguide.nineballscorekeeper.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mathgeniusguide.nineballscorekeeper.R
import com.mathgeniusguide.nineballscorekeeper.databinding.RackItemBinding
import com.mathgeniusguide.nineballscorekeeper.objects.Rack

class RackAdapter (private val items: List<Rack>, private val context: Context) : RecyclerView.Adapter<RackAdapter.ViewHolder> () {
    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.rack_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val i = items[position]
        holder.binding.rackNumberTextView.text = i.index.toString()
        holder.binding.player1TextView.text = i.player1.toString()
        holder.binding.player1TotalTextView.text = i.player1Total.toString()
        holder.binding.player2TextView.text = i.player2.toString()
        holder.binding.player2TotalTextView.text = i.player2Total.toString()
        holder.binding.deadBallsTextView.text = i.deadBalls.toString()
        holder.binding.deadBallsTotalTextView.text = i.deadBallsTotal.toString()
        holder.binding.inningsTextView.text = i.innings.toString()
        holder.binding.inningsTotalTextView.text = i.inningsTotal.toString()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = RackItemBinding.bind(view)
    }
}