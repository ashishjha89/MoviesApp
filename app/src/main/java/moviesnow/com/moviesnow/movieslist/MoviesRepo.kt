package moviesnow.com.moviesnow.movieslist

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import moviesnow.com.moviesnow.api.MoviesApi
import moviesnow.com.moviesnow.api.Resource
import moviesnow.com.moviesnow.api.ResourceStatus
import moviesnow.com.moviesnow.models.CurrentMovies
import moviesnow.com.moviesnow.models.Movie

class MoviesRepo(private val moviesApi: MoviesApi, private val moviesCache: MoviesCache) {

    fun getCurrentMovies(pageNumber: Int): LiveData<Resource<CurrentMovies>> {
        val moviesFromApi = moviesApi.getCurrentMovies(pageNumber)
        return Transformations.map(moviesFromApi) {
            getAggregatedMovies(moviesFromApi = it)
        }
    }

    fun getMovieSearchResult(pageNumber: Int, query: String): LiveData<Resource<CurrentMovies>> {
        if (pageNumber == 0) {
            moviesCache.cachedMovies = null
        }
        val moviesFromApi = moviesApi.getMovieSearchResult(pageNumber, query)
        return Transformations.map(moviesFromApi) {
            getAggregatedMovies(moviesFromApi = it)
        }
    }

    private fun getAggregatedMovies(moviesFromApi: Resource<CurrentMovies>): Resource<CurrentMovies> {
        if (moviesFromApi.status == ResourceStatus.SUCCESS) {
            val aggregatedMovies = Resource.success(
                data = getAllLoadedMovies(
                    newMovies = moviesFromApi.data!!,
                    oldMovies = moviesCache.cachedMovies
                )
            )
            moviesCache.cachedMovies = aggregatedMovies.data
            return aggregatedMovies
        }
        return moviesFromApi
    }

    private fun getAllLoadedMovies(newMovies: CurrentMovies, oldMovies: CurrentMovies?): CurrentMovies {
        val shouldAppendMovies = newMovies.page > 1
        val allMovieList = ArrayList<Movie>()
        if (shouldAppendMovies) {
            val oldMovieList = oldMovies?.results ?: ArrayList()
            allMovieList.addAll(oldMovieList)
        }
        allMovieList.addAll(getMoviesWithImages(newMovies.results))
        return newMovies.copy(results = allMovieList)
    }

    private fun getMoviesWithImages(newMovies: List<Movie>) : List<Movie> =
        newMovies.filter { !it.backdropPath.isNullOrEmpty() && !it.posterPath.isNullOrEmpty() }

}