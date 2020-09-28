package com.fsdevelopment.sicreditestapp.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.ImageLoader
import coil.load
import com.fsdevelopment.sicreditestapp.R
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