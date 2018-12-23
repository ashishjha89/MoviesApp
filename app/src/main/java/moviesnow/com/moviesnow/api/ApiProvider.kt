package moviesnow.com.moviesnow.api

import android.support.annotation.VisibleForTesting

object ApiProvider {

    private var mockApi: MoviesApi? = null

    private val moviesApi = MoviesApiImpl()

    fun getMoviesApi() : MoviesApi = mockApi?: moviesApi

    @VisibleForTesting
    fun initializeMockApi(api: MoviesApi) {
        mockApi = api
    }
}