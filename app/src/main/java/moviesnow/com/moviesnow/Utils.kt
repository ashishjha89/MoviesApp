package moviesnow.com.moviesnow

import android.content.Context
import android.net.ConnectivityManager

fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return connectivityManager.activeNetworkInfo?.isConnected == true
}