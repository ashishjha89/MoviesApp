package moviesnow.com.moviesnow.models

import java.io.Serializable

data class CurrentMovies(val results: List<Movie> = ArrayList(), val page: Int = 0, val totalPages: Int = 0) : Serializable