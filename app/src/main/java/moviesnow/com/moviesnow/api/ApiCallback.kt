package moviesnow.com.moviesnow.api

import retrofit2.Call
import retrofit2.Response
import java.net.HttpURLConnection

abstract class ApiCallback<T> : retrofit2.Callback<T> {

    override fun onResponse(call: Call<T>, response: Response<T>) {
        if (response.isSuccessfulOrNotModified()) {
            onSuccess(call, response.body(), response.wasModified(), response.etag())
            return
        }
        onError(call, NetworkError(response.errorBody(), response.code()))
    }

    override fun onFailure(call: Call<T>, t: Throwable?) {
        onError(call, NetworkError(t))
    }

    abstract fun onSuccess(call: Call<T>, data: T?, wasModified: Boolean, etag: String?)
    abstract fun onError(call: Call<T>, error: NetworkError)
}

private fun Response<*>.etag(): String? = headers().get("Etag")
private fun Response<*>.wasModified(): Boolean = code() != HttpURLConnection.HTTP_NOT_MODIFIED
private fun Response<*>.isSuccessfulOrNotModified() : Boolean = isSuccessful || code() == HttpURLConnection.HTTP_NOT_MODIFIED
