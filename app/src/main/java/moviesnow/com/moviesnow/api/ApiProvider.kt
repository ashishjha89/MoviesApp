package moviesnow.com.moviesnow.api

import android.support.annotation.VisibleForTesting

object ApiProvider {

    var mockApi: MoviesApi? = null

    val moviesApi = MoviesApiImpl()

    @VisibleForTesting
    fun initializeMockApi(api: MoviesApi) {
        mockApi = api
    }
}