package moviesnow.com.moviesnow.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_movie_detail.*
import moviesnow.com.moviesnow.R
import moviesnow.com.moviesnow.api.loadBackdropImage
import moviesnow.com.moviesnow.models.Movie

class MovieDetailActivity : AppCompatActivity() {

    companion object {

        private const val MOVIE = "Movie"

        @JvmStatic
        fun getLaunchIntent(context: Context, movie: Movie) =
            Intent(context, MovieDetailActivity::class.java).apply {
                putExtra(MOVIE, movie)
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        supportActionBar?.setTitle(R.string.movieDetail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val movie = intent.getSerializableExtra(MOVIE) as Movie
        movieTitleView.text = movie.title
        movieOverviewView.text = movie.overview
        val isExtraWide = resources.configuration.screenWidthDp > 720

        loadBackdropImage(movie.backdropPath?:return, backdropImageView, isExtraWide)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

}