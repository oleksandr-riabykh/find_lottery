package com.limestudio.findlottery.presentation.ui.tickets.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.limestudio.findlottery.R
import com.limestudio.findlottery.data.models.Ticket
import com.limestudio.findlottery.extensions.platformFromHtml
import com.limestudio.findlottery.extensions.showAlert
import com.limestudio.findlottery.extensions.toDateFormat
import java.util.*

const val MODE_EDIT = 0
const val MODE_VIEW = 1

class TicketAdapter(
    private val mode: Int,
    private val onClickItem: (item: Ticket) -> Unit,
    private val onDeleteItem: (item: Ticket) -> Unit
) :
    RecyclerView.Adapter<TicketBaseViewHolder>() {

    private var mListOfItems = arrayListOf<Ticket>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketBaseViewHolder {
        return if (mode == MODE_VIEW) TicketSearchViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_ticket_search, parent, false)
        ) else TicketViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_ticket, parent, false)
        )
    }

    fun setData(items: List<Ticket>) {
        mListOfItems.clear()
        mListOfItems.addAll(items)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = mListOfItems.size

    override fun onBindViewHolder(holder: TicketBaseViewHolder, position: Int) {
        val item = mListOfItems[position]
        holder.bind(item, onClickItem, { ticket, positionItem ->
            onDeleteItem(ticket)
            removeItem(positionItem)
        })
    }

    private fun removeItem(position: Int) {
        mListOfItems.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, mListOfItems.size)
    }
}

@Suppress("DEPRECATION")
abstract class TicketBaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    protected val dateText: TextView? = view.findViewById(R.id.date)
    protected val numberText: TextView? = view.findViewById(R.id.number)
    protected val nameText: TextView? = view.findViewById(R.id.name)

    open fun bind(
        item: Ticket,
        onClickItem: (item: Ticket) -> Unit,
        onDeleteClick: (item: Ticket, position: Int) -> Unit
    ) {
    }
}

@Suppress("DEPRECATION")
class TicketViewHolder(view: View) : TicketBaseViewHolder(view) {
    private val deleteButton: ImageButton? = view.findViewById(R.id.deleteButton)
    private val bath: ImageView? = view.findViewById(R.id.bath)

    override fun bind(
        item: Ticket,
        onClickItem: (item: Ticket) -> Unit,
        onDeleteClick: (item: Ticket, position: Int) -> Unit
    ) {
        dateText?.text = item.numbers
        val sourceString = "Progress: <b>${item.progress}</b> - Set: <b>${item.set}</b> "
        numberText?.text = sourceString.platformFromHtml()
        nameText?.text =
            Date(item.timestamp).toDateFormat(itemView.context.resources.configuration.locale)
        itemView.setOnClickListener {
            onClickItem(item)
        }
        deleteButton?.visibility = View.VISIBLE
        bath?.visibility = View.VISIBLE
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

@Suppress("DEPRECATION")
class TicketSearchViewHolder(view: View) : TicketBaseViewHolder(view) {
    private val setText: TextView? = view.findViewById(R.id.set)

    override fun bind(
        item: Ticket,
        onClickItem: (item: Ticket) -> Unit,
        onDeleteClick: (item: Ticket, position: Int) -> Unit
    ) {
        numberText?.text = item.numbers
        nameText?.text = item.userName
        val sourceString = "(Progress: <b>${item.progress}</b> - Set: <b>${item.set}</b>)"
        setText?.text = sourceString.platformFromHtml()
        dateText?.text =
            Date(item.timestamp).toDateFormat(itemView.context.resources.configuration.locale)
        itemView.setOnClickListener {
            onClickItem(item)
        }
    }
}