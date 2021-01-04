package com.limestudio.findlottery.presentation.ui.game.categories

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.limestudio.findlottery.R
import com.limestudio.findlottery.data.models.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_category.view.*


class CategoryAdapter(
    private val mListener: OnCategoryInteractionListener?
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener
    private val mValues: ArrayList<User> = arrayListOf()

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as User
            mListener?.onCategoryClicked(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.titleTextView.text = item.phoneNumber
        holder.descriptionTextView.text = item.phoneNumber
        Picasso.get().load(item.phoneNumber).placeholder(R.drawable.ic_camera)
            .into(holder.imageView)
        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    fun setData(games: List<User>) {
        mValues.clear()
        mValues.addAll(games)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val titleTextView: TextView = mView.title
        val descriptionTextView: TextView = mView.description
        val imageView: ImageView = mView.imageView
        val ratingBar: RatingBar = mView.ratingBar
        val progressBar: ProgressBar = mView.progressBar
    }
}
