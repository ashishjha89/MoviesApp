package moviesnow.com.moviesnow.api

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import moviesnow.com.moviesnow.api.MoviesApi
import moviesnow.com.moviesnow.api.Resource
import moviesnow.com.moviesnow.models.CurrentMovies

class MockMoviesApi : MoviesApi {

    var currentMovies: CurrentMovies? = null

    override fun getCurrentMovies(pageNumber: Int): LiveData<Resource<CurrentMovies>> {
        val currentMoviesRes = MutableLiveData<Resource<CurrentMovies>>()
        if (currentMovies != null) {
            currentMoviesRes.value = Resource.success(data = currentMovies!!)
        }
        return currentMoviesRes
    }

    override fun getMovieSearchResult(pageNumber: Int, query: String): LiveData<Resource<CurrentMovies>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}