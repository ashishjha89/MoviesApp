package moviesnow.com.moviesnow.movieslist

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.View
import kotlinx.android.synthetic.main.activity_movie_list.*
import moviesnow.com.moviesnow.R
import moviesnow.com.moviesnow.api.ResourceStatus
import moviesnow.com.moviesnow.detail.MovieDetailActivity
import moviesnow.com.moviesnow.models.CurrentMovies
import moviesnow.com.moviesnow.models.Movie
import moviesnow.com.moviesnow.movieslist.adapter.MovieListAdapter

class MovieListActivity : AppCompatActivity(), MovieListAdapter.MovieClickListener {

    private val viewModel: MoviesViewModel by lazy { ViewModelProviders.of(this, MoviesViewModelFactory()).get(MoviesViewModel::class.java) }

    private val adapter: MovieListAdapter by lazy { MovieListAdapter(movieClickListener = this, viewModel = viewModel) }

    private val movieSearchHandler: MovieSearchHandler by lazy { MovieSearchHandler(viewModel) }

    private val searchViewController: SearchViewController by lazy { SearchViewController(movieSearchHandler, viewModel) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_list)
        initializeAdapter(columns = getColumnCount())
        observeData()
    }

    override fun onMovieClicked(movie: Movie) {
        startActivity(MovieDetailActivity.getLaunchIntent(this, movie))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.movies_menu, menu)
        val searchMenuItem = menu.findItem(R.id.action_search)
        val searchView = searchMenuItem.actionView as SearchView
        searchViewController.setSearchView(searchView, searchMenuItem)
        return true
    }

    private fun observeData() {
        viewModel.movieList.observe(this, Observer { currentMovies ->
            if (currentMovies != null) {
                when (currentMovies.status) {
                    ResourceStatus.LOADING -> progressBar.visibility = View.VISIBLE
                    ResourceStatus.ERROR -> {
                        progressBar.visibility = View.GONE
                        setErrorView()
                    }
                    ResourceStatus.SUCCESS -> {
                        progressBar.visibility = View.GONE
                        updateMovieList(movies = currentMovies.data!!)
                    }
                }
            }
        })
    }

    private fun updateMovieList(movies: CurrentMovies) {
        adapter.notifyDataSetChanged()
        updateLayoutManagerIfNeeded(oldMovies = adapter.movies, newMovies = movies)
        adapter.movies = movies
    }

    private fun setErrorView() {
        Snackbar.make(moviesListView, R.string.somethingWentWrong, Snackbar.LENGTH_LONG).show()
        if (adapter.movies == null || adapter.movies!!.results.isEmpty()) {
            initializeAdapter(columns = 1)
            adapter.notifyDataSetChanged()
        }
    }

    private fun getColumnCount() = Math.ceil(resources.configuration.screenWidthDp.toDouble() / 200).toInt()

    private fun updateLayoutManagerIfNeeded(oldMovies: CurrentMovies?, newMovies: CurrentMovies) {
        if (oldMovies?.results?.size == 0 && newMovies.results.isNotEmpty()) {
            // If oldList was empty and newList has entries -> set column
            initializeAdapter(columns = getColumnCount())
        } else if (newMovies.results.isEmpty()) {
            // If newList is empty
            initializeAdapter(columns = 1)
        }
    }

    private fun initializeAdapter(columns: Int) {
        moviesListView.layoutManager = GridLayoutManager(this, columns)
        moviesListView.adapter = adapter
    }
}
