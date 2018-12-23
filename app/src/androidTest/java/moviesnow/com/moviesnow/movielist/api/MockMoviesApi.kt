package moviesnow.com.moviesnow.movielist.api

import android.arch.lifecycle.LiveData
import moviesnow.com.moviesnow.api.MoviesApi
import moviesnow.com.moviesnow.api.Resource
import moviesnow.com.moviesnow.models.CurrentMovies

class MockMoviesApi : MoviesApi {

    var currentMovies: CurrentMovies? = null

    override fun getCurrentMovies(pageNumber: Int): LiveData<Resource<CurrentMovies>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getMovieSearchResult(pageNumber: Int, query: String): LiveData<Resource<CurrentMovies>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}