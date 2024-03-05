package com.maruf.foody.adapters

import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.maruf.foody.R
import com.maruf.foody.databinding.RecipesRowLayoutBinding
import com.maruf.foody.model.Result
import com.maruf.foody.utils.DiffCallback

class RecipesAdapter : RecyclerView.Adapter<RecipesAdapter.MyViewHolder>() {
    private var recipeList = emptyList<Result>()
    fun updateList(list: MutableList<Result>) {
        val diffCallback = DiffCallback(recipeList, list)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        recipeList = list
        diffResult.dispatchUpdatesTo(this)
    }

    class MyViewHolder(val binding: RecipesRowLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: Result) {
            binding.result = recipe
            binding.executePendingBindings()

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = RecipesRowLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val recipe = recipeList[position]
        holder.bind(recipe)

   /*     holder.binding.apply {
            titleTextView.text = recipe.title
            descriptionTextView.text = recipe.summary
            clockTextView.text = recipe.readyInMinutes.toString()
            // load image
            recipeImageView.load(recipe.image) {
                crossfade(true)
                placeholder(R.drawable.ic_error_placeholder)
            }
            // Get vegan color
            val color = ContextCompat.getColor(leafTextView.context, if (recipe.vegan) R.color.green else R.color.mediumGray)
            leafTextView.setTextColor(color)
            leafImageView.backgroundTintList = ColorStateList.valueOf(color)
            heartTextView.text = recipe.aggregateLikes.toString()
        }*/
    }

    override fun getItemCount(): Int {
        Log.d("TAG", "getItemCount: ${recipeList.size}")
        return recipeList.size
    }


}