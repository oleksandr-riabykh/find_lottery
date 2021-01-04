package com.limestudio.findlottery.presentation.ui.tickets.draws

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.limestudio.findlottery.R
import com.limestudio.findlottery.data.models.Draw
import com.limestudio.findlottery.extensions.isDayBeforeFuture
import com.limestudio.findlottery.extensions.showAlert
import com.limestudio.findlottery.extensions.toDateFormat
import java.util.*

class DrawsAdapter(
    private val onClickItem: (item: Draw) -> Unit,
    private val onDeleteClick: (item: Draw) -> Unit
) :
    RecyclerView.Adapter<HomeViewHolder>() {

    private var mListOfItems = arrayListOf<Draw>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_draw, parent, false)

        return HomeViewHolder(view)
    }

    fun setData(items: List<Draw>) {
        mListOfItems.clear()
        mListOfItems.addAll(items)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = mListOfItems.size

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val item = mListOfItems[position]
        holder.bind(item, onClickItem, { draw, _position ->
            onDeleteClick(draw)
            removeItem(_position)
        })
    }

    private fun removeItem(position: Int) {
        mListOfItems.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, mListOfItems.size)
    }
}

@Suppress("DEPRECATION")
class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val dateText: TextView? = view.findViewById(R.id.drawDate)
    private val statusText: TextView? = view.findViewById(R.id.status)
    private val deleteButton: ImageButton? = view.findViewById(R.id.deleteButton)

    @SuppressLint("SetTextI18n")
    fun bind(
        item: Draw,
        onClickItem: (item: Draw) -> Unit,
        onDeleteClick: (item: Draw, position: Int) -> Unit
    ) {
        val date = Date(item.timestamp)
        dateText?.text =
            date.toDateFormat(itemView.context.resources.configuration.locale)
        val numberSuffix =
            if (date.isDayBeforeFuture()) "" else "(${item.numberWinTickets})"
        val numberPrefix =
            if (date.isDayBeforeFuture()) itemView.context.resources.getText(R.string.upcoming_t) else itemView.context.resources.getText(
                R.string.win_tickets_number
            )
        statusText?.text = "$numberPrefix $numberSuffix"
        itemView.setOnClickListener {
            onClickItem(item)
        }
        deleteButton?.setOnClickListener {
            showAlert(
                R.string.remove_message_draw,
                android.R.string.ok,
                android.R.string.cancel,
                { onDeleteClick(item, position) },
                {})
        }
    }
}