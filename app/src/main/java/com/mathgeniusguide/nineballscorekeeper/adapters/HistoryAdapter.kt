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
import com.mathgeniusguide.nineballscorekeeper.enums.DescriptionKey
import com.mathgeniusguide.nineballscorekeeper.enums.SharedPreferencesTarget
import com.mathgeniusguide.nineballscorekeeper.util.getGameDetails
import com.mathgeniusguide.nineballscorekeeper.util.toSpelledOutDate

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
        var loadedGame = mainActivity.readSharedPreferences(SharedPreferencesTarget.LOADED_GAME)
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
        if (gameDetails.isValidApaGoals()) {
            holder.binding.matchScores.visibility = View.VISIBLE
            holder.binding.matchScores.text = String.format(
                context.getString(R.string.score_score),
                gameDetails.player1MatchPoints,
                gameDetails.player2MatchPoints
            )
            if (gameDetails.team1.isNullOrEmpty() || gameDetails.team2.isNullOrEmpty()) {
                holder.binding.teamNames.visibility = View.GONE
            } else {
                holder.binding.teamNames.visibility = View.VISIBLE
                holder.binding.teamNames.text = String.format(
                    context.getString(R.string.player_vs_player),
                    gameDetails.team1,
                    gameDetails.team2
                )
            }
        } else {
            holder.binding.matchScores.visibility = View.GONE
            holder.binding.teamNames.visibility = View.GONE
        }
        holder.binding.location.text = String.format(
            context.getString(R.string.at_location_on_date),
            gameDetails.location,
            gameDetails.date?.toSpelledOutDate()
        )
        holder.binding.innings.text =
            String.format(context.getString(R.string.x_innings), gameDetails.gameStats.innings)
        holder.binding.historyItem.visibility = View.VISIBLE
        holder.binding.historyItem.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setPositiveButton("Load") { _, _ ->
                val loadBuilder = AlertDialog.Builder(context)
                loadBuilder.setPositiveButton("Load") { _, _ ->
                    mainActivity.removeFromGameList(position)
                    if (loadedGame.isNotEmpty()) {
                        mainActivity.addToGameList(loadedGame)
                    }
                    mainActivity.updateGameString(string)
                    loadedGame = string
                    mainActivity.writeSharedPreferences(SharedPreferencesTarget.LOADED_GAME, string)
                    Toast.makeText(
                        context,
                        context.getString(R.string.game_loaded),
                        Toast.LENGTH_LONG
                    ).show()
                    notifyDataSetChanged()
                }
                loadBuilder.setNegativeButton("Back", null)
                loadBuilder.setMessage(context.getString(
                    if (loadedGame.isEmpty())
                        R.string.load_are_you_sure
                    else
                        R.string.load_revert_are_you_sure
                ))
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
                deleteBuilder.setMessage(
                    String.format(
                        context.getString(R.string.delete_are_you_sure),
                        String.format(context.getString(R.string.player_vs_player, gameDetails.player1Name, gameDetails.player2Name)),
                        String.format(context.getString(R.string.at_location_on_date, gameDetails.location, gameDetails.date?.toSpelledOutDate())),
                        String.format(context.getString(R.string.score_score, gameDetails.gameStats.player1Stats.score, gameDetails.gameStats.player2Stats.score)),
                        String.format(context.getString(R.string.x_innings), gameDetails.gameStats.innings)
                    )
                )
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