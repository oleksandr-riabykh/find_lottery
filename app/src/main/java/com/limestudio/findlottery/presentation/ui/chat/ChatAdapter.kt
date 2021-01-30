package com.limestudio.findlottery.presentation.ui.chat

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.limestudio.findlottery.data.models.Message


class ChatAdapter(options: FirebaseRecyclerOptions<Message>)
    : FirebaseRecyclerAdapter<Message, RecyclerView.ViewHolder>(options)
{

    companion object {
        private const val SELLER = 1
        private const val BUYER = 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    inner class ChatSellerViewHolder(sellerItem: View) : RecyclerView.ViewHolder(sellerItem) {
        fun bind() {

        }
    }

    inner class ChatBuyerViewHolder(buyerItem: View) : RecyclerView.ViewHolder(buyerItem) {
        fun bind() {
            
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, model: Message) {
        TODO("Not yet implemented")
    }
}