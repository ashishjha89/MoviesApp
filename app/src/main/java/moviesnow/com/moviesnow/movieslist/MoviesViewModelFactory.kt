package moviesnow.com.moviesnow.movieslist

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import moviesnow.com.moviesnow.api.ApiProvider

class MoviesViewModelFactory : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = MoviesViewModel(
        moviesRepo = MoviesRepo(moviesApi = ApiProvider.getMoviesApi(), moviesCache = MoviesCache())
    ) as T
}