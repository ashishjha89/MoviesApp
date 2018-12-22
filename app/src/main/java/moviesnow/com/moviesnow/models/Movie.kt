package moviesnow.com.moviesnow.models

import java.io.Serializable

class Movie(
    val id: String = "",
    val title: String = "",
    val overview: String = "",
    val posterPath: String? = "",
    val backdropPath: String? = ""
) : Serializable