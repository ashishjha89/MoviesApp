package moviesnow.com.moviesnow.api

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import moviesnow.com.moviesnow.models.CurrentMovies

class MoviesApiImpl : MoviesApi {

    private val apiService = RetrofitFactory.getRetrofitService()

    override fun getCurrentMovies(pageNumber: Int): LiveData<Resource<CurrentMovies>> {
        val currentMovies = MutableLiveData<Resource<CurrentMovies>>()
        apiService.getCurrentMovies(pageNumber).enqueue(ResponseLiveDataCallback(currentMovies))
        return currentMovies
    }

    override fun getMovieSearchResult(pageNumber: Int, query: String): LiveData<Resource<CurrentMovies>> {
        val searchResult = MutableLiveData<Resource<CurrentMovies>>()
        apiService.getMovieSearchResult(pageNumber, query).enqueue(ResponseLiveDataCallback(searchResult))
        return searchResult
    }
}