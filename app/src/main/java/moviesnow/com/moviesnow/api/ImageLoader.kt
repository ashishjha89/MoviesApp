package moviesnow.com.moviesnow.api

import android.support.annotation.DrawableRes
import android.widget.ImageView
import com.squareup.picasso.Picasso
import moviesnow.com.moviesnow.R

private const val baseImageUrl = "https://image.tmdb.org/t/p/"

fun loadPosterImage(imagePath: String, imageView: ImageView) {
    val width = "w342"
    val imageUrl = "$baseImageUrl$width$imagePath"
    loadImage(imageUrl, imageView, R.mipmap.ic_launcher)
}

fun loadBackdropImage(imagePath: String, imageView: ImageView, isExtraWide: Boolean) {
    val width = if (isExtraWide) "w1280" else "w780"
    val imageUrl = "$baseImageUrl$width$imagePath"
    loadImage(imageUrl, imageView, R.drawable.ic_launcher_background)
}

private fun loadImage(imageUrl: String, imageView: ImageView, @DrawableRes errorImageId: Int) {
    Picasso.get().load(imageUrl).error(errorImageId).into(imageView)
}