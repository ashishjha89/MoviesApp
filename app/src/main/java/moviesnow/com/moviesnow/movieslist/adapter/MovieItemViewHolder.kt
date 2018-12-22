package moviesnow.com.moviesnow.movieslist.adapter

import android.view.View
import android.widget.ImageView
import kotlinx.android.synthetic.main.movie_item.view.*

class MovieItemViewHolder(itemView: View, movieItemClickListener: ItemClickListener) : BaseMoviesViewHolder(itemView) {
    val movieView : ImageView = itemView.movieImageView

    init {
        movieView.setOnClickListener { movieItemClickListener.onItemClicked(position = adapterPosition) }
    }

    interface ItemClickListener {
        fun onItemClicked(position: Int)
    }
}