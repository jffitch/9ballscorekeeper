package com.mathgeniusguide.nineballscorekeeper.adapters

import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.mathgeniusguide.nineballscorekeeper.MainActivity
import com.mathgeniusguide.nineballscorekeeper.R
import com.mathgeniusguide.nineballscorekeeper.databinding.HistoryItemBinding
import com.mathgeniusguide.nineballscorekeeper.util.getGameDetails

class HistoryAdapter(
    private val items: List<String>,
    private val context: Context,
    private val mainActivity: MainActivity
) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.history_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val string = items[position]
        val gameDetails = getGameDetails(string)
        holder.binding.playerNames.text = String.format(
            context.getString(R.string.player_vs_player),
            gameDetails.player1Name,
            gameDetails.player2Name
        )
        holder.binding.scores.text = String.format(
            context.getString(R.string.score_score),
            gameDetails.gameStats.player1Stats.score,
            gameDetails.gameStats.player2Stats.score
        )
        holder.binding.location.text = String.format(
            context.getString(R.string.at_location_on_date),
            gameDetails.description["Location"],
            gameDetails.description["Date"]
        )
        holder.binding.innings.text =
            String.format(context.getString(R.string.x_innings), gameDetails.gameStats.innings)
        holder.binding.historyItem.visibility = View.VISIBLE
        holder.binding.historyItem.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setPositiveButton("Load") { _, _ ->
                val loadBuilder = AlertDialog.Builder(context)
                loadBuilder.setPositiveButton("Load") { _, _ ->
                    mainActivity.updateGameString(string)
                    Toast.makeText(
                        context,
                        context.getString(R.string.game_loaded),
                        Toast.LENGTH_LONG
                    ).show()
                }
                loadBuilder.setNegativeButton("Back", null)
                loadBuilder.setMessage(context.getString(R.string.load_are_you_sure))
                loadBuilder.create().show()
            }
            builder.setNegativeButton("Delete") { _, _ ->
                val deleteBuilder = AlertDialog.Builder(context)
                deleteBuilder.setPositiveButton("Delete") { _, _ ->
                    mainActivity.removeFromGameList(position)
                    Toast.makeText(
                        context,
                        context.getString(R.string.game_deleted),
                        Toast.LENGTH_LONG
                    ).show()
                    notifyDataSetChanged()
                }
                deleteBuilder.setNegativeButton("Back", null)
                deleteBuilder.setMessage(context.getString(R.string.delete_are_you_sure))
                deleteBuilder.create().show()
            }
            builder.setNeutralButton("Back", null)
            builder.setMessage(context.getString(R.string.load_or_delete))
            builder.create().show()
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = HistoryItemBinding.bind(view)
    }
}