package moviesnow.com.moviesnow.api

import android.arch.lifecycle.MutableLiveData
import retrofit2.Call

class ResponseLiveDataCallback<ResponseType>(
        private val responseDataRes: MutableLiveData<Resource<ResponseType>>
) : ApiCallback<ResponseType>() {

    init {
        responseDataRes.value = Resource.loading()
    }

    override fun onSuccess(call: Call<ResponseType>, data: ResponseType?, wasModified: Boolean, etag: String?) {
        data?.let { responseDataRes.value = Resource.success(data = it) }
                ?: let { responseDataRes.value = Resource.error() }
    }

    override fun onError(call: Call<ResponseType>, error: NetworkError) {
        responseDataRes.value = Resource.error()
    }

}