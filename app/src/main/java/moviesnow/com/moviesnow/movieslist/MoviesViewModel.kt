package moviesnow.com.moviesnow.movieslist

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import moviesnow.com.moviesnow.api.AbsentLiveData
import moviesnow.com.moviesnow.api.Resource
import moviesnow.com.moviesnow.api.ResourceStatus
import moviesnow.com.moviesnow.models.CurrentMovies

class MoviesViewModel(private val moviesRepo: MoviesRepo) : ViewModel() {

    private class LoadMovies(val pageNumber: Int, val query: String = "") {
        fun isSearchRequest() = query.isNotEmpty()
    }

    private val moviesRequest = MutableLiveData<LoadMovies>()

    val movieList: LiveData<Resource<CurrentMovies>> =
        Transformations.switchMap(moviesRequest) {
            getMovies(movieRequest = it)
        }

    var lastSearchQuery: String = ""

    init {
        loadNextPage()
    }

    fun loadNextPage() {
        if (movieList.value?.status == ResourceStatus.LOADING) return
        val currentPage = moviesRequest.value?.pageNumber ?: 0
        val query = moviesRequest.value?.query ?: ""
        moviesRequest.value = LoadMovies(pageNumber = currentPage + 1, query = query)
        lastSearchQuery = query
    }

    fun searchMovie(query: String) {
        moviesRequest.value = LoadMovies(pageNumber = 1, query = query)
        lastSearchQuery = query
    }

    private fun getMovies(movieRequest: MoviesViewModel.LoadMovies?): LiveData<Resource<CurrentMovies>> =
        when {
            movieRequest == null -> AbsentLiveData.create()

            movieRequest.isSearchRequest() ->
                moviesRepo.getMovieSearchResult(movieRequest.pageNumber, movieRequest.query)

            else -> moviesRepo.getCurrentMovies(pageNumber = movieRequest.pageNumber)
        }

}