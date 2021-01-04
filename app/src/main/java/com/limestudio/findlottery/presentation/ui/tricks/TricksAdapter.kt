package com.limestudio.findlottery.presentation.ui.tricks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.limestudio.findlottery.R
import com.limestudio.findlottery.data.models.Ticket
import kotlinx.android.synthetic.main.item_trick.view.*

class TricksAdapter(
    private val mListener: OnListTrickInteractionListener?
) : RecyclerView.Adapter<TricksAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener
    private val mValues: ArrayList<Ticket> = arrayListOf()

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Ticket
            mListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_trick, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.title.text = item.numbers
        holder.description.text = item.numbers

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    fun setData(data: List<Ticket>?) {
        data?.let { mValues.addAll(it) }
        notifyDataSetChanged()
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val title: TextView = mView.item_title
        val description: TextView = mView.item_description
    }
}