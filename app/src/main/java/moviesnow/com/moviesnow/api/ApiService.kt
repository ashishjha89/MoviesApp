package moviesnow.com.moviesnow.api

import moviesnow.com.moviesnow.models.CurrentMovies
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

private const val API_KEY = "574dd4d65ad8066e55695ccf56e06996"

interface ApiService {

    @GET("movie/now_playing?api_key=$API_KEY&language=en-US")
    fun getCurrentMovies(@Query("page") page: Int): Call<CurrentMovies>

    @GET("search/movie?api_key=$API_KEY&language=en-US")
    fun getMovieSearchResult(@Query("page") page: Int, @Query("query") query: String): Call<CurrentMovies>
}