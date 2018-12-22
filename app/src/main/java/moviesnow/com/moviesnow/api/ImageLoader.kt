package moviesnow.com.moviesnow.api

import android.widget.ImageView
import com.squareup.picasso.Picasso

private const val baseImageUrl = "https://image.tmdb.org/t/p/"

fun loadPosterImage(imagePath: String, imageView: ImageView) {
    val width = "w342"
    val imageUrl = "$baseImageUrl$width$imagePath"
    loadImage(imageUrl, imageView)
}

fun loadBackdropImage(imagePath: String, imageView: ImageView, isExtraWide: Boolean) {
    val width = if (isExtraWide) "w1280" else "w780"
    val imageUrl = "$baseImageUrl$width$imagePath"
    loadImage(imageUrl, imageView)
}

private fun loadImage(imageUrl: String, imageView: ImageView) {
    Picasso.get().load(imageUrl).into(imageView)
}