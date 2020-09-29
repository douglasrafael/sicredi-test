package com.fsdevelopment.sicreditestapp.utils

import android.content.res.ColorStateList
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import coil.ImageLoader
import coil.load
import com.fsdevelopment.sicreditestapp.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber
import java.util.*

object BindingImages : KoinComponent {
    private val imageLoader: ImageLoader by inject()

    @JvmStatic
    @BindingAdapter("imageUrl")
    fun loadImage(view: ImageView, url: String?) {
        view.load(url, imageLoader) {
            placeholder(R.drawable.placeholder)
            error(R.drawable.placeholder)
        }
    }
}

@BindingAdapter(value = ["dateLong", "dateFormat"], requireAll = true)
fun setTextDateFormat(tv: TextView, date: Long, format: String) {
    tv.text = Date(date).toString(format)
}

@BindingAdapter("changeIconFavorite")
fun setIconFavorite(bt: FloatingActionButton, isFavorite: Boolean?) {
    when (isFavorite) {
        true -> {
            bt.setImageResource(R.drawable.ic_action_favorite)
            bt.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(bt.context, R.color.colorFavorite)
            )
        }
        else -> {
            bt.setImageResource(R.drawable.ic_action_not_favorite)
            bt.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(bt.context, R.color.colorAccent)
            )
        }
    }
}

@BindingAdapter("changeIconFavorite")
fun setIconFavorite(img: ImageView, isFavorite: Boolean?) {
    img.setColorFilter(ContextCompat.getColor(img.context, R.color.colorFavorite))
    when (isFavorite) {
        true -> img.setImageResource(R.drawable.ic_action_favorite)
        else -> img.setImageResource(R.drawable.ic_action_not_favorite)
    }
}