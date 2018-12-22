package moviesnow.com.moviesnow.api

import android.arch.lifecycle.LiveData
import moviesnow.com.moviesnow.models.CurrentMovies

interface MoviesApi {

    fun getCurrentMovies(pageNumber: Int): LiveData<Resource<CurrentMovies>>

    fun getMovieSearchResult(pageNumber: Int, query: String): LiveData<Resource<CurrentMovies>>
}