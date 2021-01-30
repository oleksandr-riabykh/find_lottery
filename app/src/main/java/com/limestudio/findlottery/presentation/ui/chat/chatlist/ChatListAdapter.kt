package com.limestudio.findlottery.presentation.ui.chat.chatlist

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.limestudio.findlottery.R
import com.limestudio.findlottery.data.models.Ticket
import com.limestudio.findlottery.presentation.ui.chat.OnChatInteractionListener
import com.squareup.picasso.Picasso

class ChatListAdapter(
    private val mListener: OnChatInteractionListener?
) :
    RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder>() {

    class ChatListViewHolder(
        val mView: View,
        val titleTextView: TextView,
        val descriptionTextView: TextView,
        val imageView: ImageView
    ) : RecyclerView.ViewHolder(mView)

    private val mOnClickListener: View.OnClickListener
    private val mDataset: ArrayList<Ticket> = arrayListOf()

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Ticket
            mListener?.onPostClicked(item)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChatListViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_post, parent, false) as View
        return ChatListViewHolder(
            itemView,
            itemView.findViewById(R.id.item_title),
            itemView.findViewById(R.id.item_description),
            itemView.findViewById(R.id.item_image)
        )
    }

    override fun onBindViewHolder(holder: ChatListViewHolder, position: Int) {
        with(holder.mView) {
            tag = mDataset[position]
            setOnClickListener(mOnClickListener)
        }
        holder.titleTextView.text = mDataset[position].timestamp.toString()
        holder.descriptionTextView.text = "mDataset[position].date.platformFromHtml()"
        try {
            Picasso.get().load(mDataset[position].timestamp.toString())
                .placeholder(R.drawable.ic_camera)
                .into(holder.imageView)
        } catch (e: IllegalArgumentException) {
            Log.e("imageLoading", "mDataset[position].image = null", e)
        }
    }

    override fun getItemCount() = mDataset.size

    fun addData(data: ArrayList<Ticket>) {
        mDataset.clear()
        mDataset.addAll(data)
        notifyDataSetChanged()
    }
}