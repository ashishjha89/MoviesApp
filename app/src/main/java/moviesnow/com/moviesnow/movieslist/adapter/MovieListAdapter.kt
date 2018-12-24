package moviesnow.com.moviesnow.movieslist.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import moviesnow.com.moviesnow.R
import moviesnow.com.moviesnow.api.ResourceStatus
import moviesnow.com.moviesnow.api.loadPosterImage
import moviesnow.com.moviesnow.isNetworkAvailable
import moviesnow.com.moviesnow.models.CurrentMovies
import moviesnow.com.moviesnow.models.Movie
import moviesnow.com.moviesnow.movieslist.MoviesViewModel

class MovieListAdapter(
    private val movieClickListener: MovieClickListener,
    private val viewModel: MoviesViewModel
) : RecyclerView.Adapter<BaseMoviesViewHolder>(), MovieItemViewHolder.ItemClickListener {

    private companion object {
        const val MOVIE_EMPTY = 1
        const val MOVIE_ITEM = 2
    }

    var movies: CurrentMovies? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseMoviesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            MOVIE_ITEM -> MovieItemViewHolder(
                itemView = inflater.inflate(R.layout.movie_item, parent, false),
                movieItemClickListener = this
            )
            MOVIE_EMPTY -> EmptyMoviesViewHolder(
                itemView = inflater.inflate(R.layout.movie_empty, parent, false)
            )
            else -> EmptyMoviesViewHolder(
                itemView = inflater.inflate(R.layout.movie_empty, parent, false)
            )
        }
    }

    override fun getItemCount(): Int {
        val movieList = movies?.results ?: return 1 // EmptyView
        return when (movieList.isEmpty()) {
            true -> 1 // EmptyView
            else -> movieList.size
        }
    }

    override fun onBindViewHolder(holder: BaseMoviesViewHolder, position: Int) {
        when (holder) {
            is MovieItemViewHolder -> setMovieItem(holder, position)
            is EmptyMoviesViewHolder -> setEmptyMovieView(holder)
        }
    }

    override fun onItemClicked(position: Int) {
        val movieList = movies?.results ?: return
        if (movieList.size > position) {
            movieClickListener.onMovieClicked(movie = movieList[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        val movieList = movies?.results ?: return MOVIE_EMPTY
        return when {
            movieList.isNotEmpty() -> MOVIE_ITEM
            else -> MOVIE_EMPTY
        }
    }

    private fun setMovieItem(itemViewHolder: MovieItemViewHolder, position: Int) {
        val movieList = movies?.results ?: return
        if (movieList.size < position + 1) {
            return
        }
        if (shouldLoadMore(position)) {
            viewModel.loadNextPage()
        }
        loadPosterImage(imagePath = movieList[position].posterPath ?: return, imageView = itemViewHolder.movieView)
    }

    private fun setEmptyMovieView(itemViewHolder: EmptyMoviesViewHolder) {
        with(itemViewHolder.emptyMoviesMessageView) {
            visibility = if (viewModel.movieList.value?.status == ResourceStatus.LOADING) View.GONE else View.VISIBLE
            text = if (isNetworkAvailable(context)) context.getString(R.string.noResultFound) else context.getString(R.string.noInternetConnection)
        }
    }

    private fun shouldLoadMore(position: Int): Boolean {
        val currentMovies = movies ?: return false
        return position > 0
                && currentMovies.page > 0
                && currentMovies.page < currentMovies.totalPages
                && position == currentMovies.results.size - 1
    }

    interface MovieClickListener {
        fun onMovieClicked(movie: Movie)
    }
}