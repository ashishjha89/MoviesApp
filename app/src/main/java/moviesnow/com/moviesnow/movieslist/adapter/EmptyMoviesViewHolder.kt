package moviesnow.com.moviesnow.movieslist.adapter

import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.movie_empty.view.*

class EmptyMoviesViewHolder(itemView: View) : BaseMoviesViewHolder(itemView) {

    val emptyMoviesMessageView : TextView = itemView.emptyMoviesMessageView
}