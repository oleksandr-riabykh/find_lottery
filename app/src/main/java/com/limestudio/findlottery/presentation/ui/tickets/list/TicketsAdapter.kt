package com.limestudio.findlottery.presentation.ui.tickets.list

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.limestudio.findlottery.R
import com.limestudio.findlottery.data.models.Ticket
import com.limestudio.findlottery.extensions.showAlert
import com.limestudio.findlottery.extensions.toDateFormat
import java.util.*


class TicketAdapter(
    private val onClickItem: (item: Ticket) -> Unit,
    private val onDeleteItem: (item: Ticket) -> Unit
) :
    RecyclerView.Adapter<HomeViewHolder>() {

    private var mListOfItems = arrayListOf<Ticket>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_ticket, parent, false)

        return HomeViewHolder(view)
    }

    fun setData(items: List<Ticket>) {
        mListOfItems.clear()
        mListOfItems.addAll(items)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = mListOfItems.size

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val item = mListOfItems[position]
        holder.bind(item, onClickItem, { ticket, positionItem ->
            onDeleteItem(ticket)
            removeItem(positionItem)
        })
    }

    fun removeItem(position: Int) {
        mListOfItems.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, mListOfItems.size)
    }
}

@Suppress("DEPRECATION")
class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val numberText: TextView? = view.findViewById(R.id.number)
    private val setText: TextView? = view.findViewById(R.id.set)
    private val dateText: TextView? = view.findViewById(R.id.date)
    private val deleteButton: ImageButton? = view.findViewById(R.id.deleteButton)

    fun bind(
        item: Ticket,
        onClickItem: (item: Ticket) -> Unit,
        onDeleteClick: (item: Ticket, position: Int) -> Unit
    ) {
        numberText?.text = item.numbers
        val sourceString = "Progress: <b>${item.progress}</b> - Set: <b>${item.set}</b> "
        setText?.text = Html.fromHtml(sourceString)
        dateText?.text =
            Date(item.timestamp).toDateFormat(itemView.context.resources.configuration.locale)
        val stringBuilder = StringBuilder()
        itemView.setOnClickListener {
            onClickItem(item)
        }
        deleteButton?.setOnClickListener {
            showAlert(
                R.string.remove_message_ticket,
                android.R.string.ok,
                android.R.string.cancel,
                { onDeleteClick(item, position) },
                {})
        }
    }
}