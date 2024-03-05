package com.maruf.foody.bindingAdapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import coil.load
import com.maruf.foody.R

class RecipesRowBinding {
    companion object {
        @BindingAdapter("loadImageFromUrl")
        @JvmStatic
        fun ImageView.loadImageFromUrl(imageUrl: String) {
            load(imageUrl) {
                crossfade(600)
                placeholder(R.drawable.ic_error_placeholder)
                error(R.drawable.ic_error_placeholder)
            }
        }

        @BindingAdapter("setNumberOfLikes")
        @JvmStatic
        fun TextView.setNumberOfLikes(likes: Int) {
            text = likes.toString()
        }

        @BindingAdapter("setNumberOfMinutes")
        @JvmStatic
        fun TextView.setNumberOfMinutes(minutes: Int) {
            text = minutes.toString()
        }

        @BindingAdapter("applyVeganColor")
        @JvmStatic
        fun View.applyVeganColor(vegan: Boolean) {
            if (vegan) {
                when (this) {
                    is TextView -> {
                        setTextColor(ContextCompat.getColor(context, R.color.green))
                    }

                    is ImageView -> {
                        setColorFilter(ContextCompat.getColor(context, R.color.green))
                    }
                }
            }
        }


    }
}