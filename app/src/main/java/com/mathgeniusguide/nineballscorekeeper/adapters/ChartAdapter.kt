package com.mathgeniusguide.nineballscorekeeper.adapters

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.mathgeniusguide.nineballscorekeeper.MainActivity
import com.mathgeniusguide.nineballscorekeeper.R
import com.mathgeniusguide.nineballscorekeeper.databinding.ChartItemBinding
import com.mathgeniusguide.nineballscorekeeper.enums.PlayerTurn
import com.mathgeniusguide.nineballscorekeeper.objects.ChartShot
import com.mathgeniusguide.nineballscorekeeper.util.getChartShotTitle

class ChartAdapter (private val items: List<ChartShot>, private val context: Context, private val mainActivity: MainActivity) : RecyclerView.Adapter<ChartAdapter.ViewHolder> () {
    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.chart_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val i = items[position]
        holder.binding.inningLabel.text = if (i.isInningStart) i.inning else ""
        holder.binding.rackLabel.text = if (i.isRackStart) i.rack else ""
        holder.binding.ballsPocketed.setBackgroundColor(context.getColor(
            if (i.isFoul) R.color.foul else R.color.partially_transparent
        ))
        val imageResources = listOf(
            R.drawable.ball_1,
            R.drawable.ball_2,
            R.drawable.ball_3,
            R.drawable.ball_4,
            R.drawable.ball_5,
            R.drawable.ball_6,
            R.drawable.ball_7,
            R.drawable.ball_8,
            R.drawable.ball_9,
        )
        val factor = context.resources.displayMetrics.density
        val desiredSize = (30 * factor).toInt()
        val layoutParams = ViewGroup.LayoutParams(desiredSize, desiredSize)
        holder.binding.ballsPocketed.removeAllViews()

        if (i.playerTurn == PlayerTurn.PLAYER1 && i.isTimeOut) {
            val image = ImageView(context)
            image.setImageResource(R.drawable.ic_time_out)
            image.layoutParams = layoutParams
            holder.binding.ballsPocketed.addView(image)
        }
        if (i.playerTurn == PlayerTurn.PLAYER1 && i.isDefense) {
            val image = ImageView(context)
            image.setImageResource(R.drawable.defense)
            image.layoutParams = layoutParams
            holder.binding.ballsPocketed.addView(image)
        }
        if (i.playerTurn == PlayerTurn.PLAYER2 && i.isStalemate) {
            val image = ImageView(context)
            image.setImageResource(R.drawable.ic_circle)
            image.layoutParams = layoutParams
            holder.binding.ballsPocketed.addView(image)
        }
        var anyPocketed = false
        for (n in 1..9) {
            if (i.ballsPocketed.contains(n.toString())) {
                anyPocketed = true
                val image = ImageView(context)
                image.setImageResource(imageResources[n-1])
                image.layoutParams = layoutParams
                holder.binding.ballsPocketed.addView(image)
            }
        }
        if (!anyPocketed) {
            val image = ImageView(context)
            image.setImageResource(R.drawable.ic_circle)
            image.layoutParams = layoutParams
            image.alpha = 0.2f
            holder.binding.ballsPocketed.addView(image)
        }
        if (i.playerTurn == PlayerTurn.PLAYER2 && i.isTimeOut) {
            val image = ImageView(context)
            image.setImageResource(R.drawable.ic_time_out)
            image.layoutParams = layoutParams
            holder.binding.ballsPocketed.addView(image)
        }
        if (i.playerTurn == PlayerTurn.PLAYER2 && i.isDefense) {
            val image = ImageView(context)
            image.setImageResource(R.drawable.defense)
            image.layoutParams = layoutParams
            holder.binding.ballsPocketed.addView(image)
        }
        if (i.playerTurn == PlayerTurn.PLAYER1 && i.isStalemate) {
            val image = ImageView(context)
            image.setImageResource(R.drawable.ic_circle)
            image.layoutParams = layoutParams
            holder.binding.ballsPocketed.addView(image)
        }
        holder.binding.ballsPocketed.gravity = if (i.playerTurn == PlayerTurn.PLAYER1) Gravity.START else Gravity.END
        holder.binding.parent.setOnClickListener {
            var selected = 0
            val alertDialog = AlertDialog.Builder(context)
            alertDialog.setTitle(getChartShotTitle(
                i.inning,
                i.rack,
                i.ballsPocketed,
                if (i.playerTurn == PlayerTurn.PLAYER1) mainActivity.gameDetails.player1Name else mainActivity.gameDetails.player2Name
            ))
            alertDialog.setSingleChoiceItems(context.resources.getStringArray(R.array.add_innings), 0) {_, position ->
                selected = position
            }
            alertDialog.setPositiveButton("Continue") { _, _ ->
                when (selected) {
                    0 -> {}
                    1 -> {}
                    2 -> {}
                }
            }
            alertDialog.setNegativeButton("Back", null)
            alertDialog.create().show()
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ChartItemBinding.bind(view)
    }
}