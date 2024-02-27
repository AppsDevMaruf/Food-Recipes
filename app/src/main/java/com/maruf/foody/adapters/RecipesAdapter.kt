package com.maruf.foody.adapters

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.maruf.foody.R
import com.maruf.foody.databinding.RecipesRowLayoutBinding
import com.maruf.foody.model.Result
import com.maruf.foody.utils.DiffCallback

class RecipesAdapter : RecyclerView.Adapter<RecipesAdapter.MyViewHolder>() {
    private var recipeList = emptyList<Result>()
    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list: MutableList<Result>) {
        val diffCallback = DiffCallback(recipeList, list)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        recipeList = list
        diffResult.dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }
    class MyViewHolder(val binding: RecipesRowLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = RecipesRowLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val recipe = recipeList[position]
        holder.binding.apply {
            titleTextView.text = recipe.title
            descriptionTextView.text = recipe.summary
            clockTextView.text = recipe.readyInMinutes.toString()
            // Get vegan color
            val color = ContextCompat.getColor(leafTextView.context, if (recipe.vegan) R.color.green else R.color.mediumGray)
            leafTextView.setTextColor(color)
            leafImageView.backgroundTintList = ColorStateList.valueOf(color)
            heartTextView.text = recipe.aggregateLikes.toString()
        }



    }

    override fun getItemCount(): Int {
        Log.d("TAG", "getItemCount: ${recipeList.size}")
        return recipeList.size
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

}