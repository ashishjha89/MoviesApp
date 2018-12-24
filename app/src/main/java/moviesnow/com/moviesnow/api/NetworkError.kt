package moviesnow.com.moviesnow.api

import okhttp3.ResponseBody

class NetworkError : Exception {

    companion object {

        private const val SC_INTERNAL_SERVER_ERROR = 500
    }

    var responseBody: ResponseBody? = null

    val statusCode: Int

    constructor(exceptionMessage: String) : super(exceptionMessage) {
        statusCode = SC_INTERNAL_SERVER_ERROR
    }

    constructor(cause: Throwable?) : super(cause) {
        statusCode = SC_INTERNAL_SERVER_ERROR
    }

    constructor(responseBody: ResponseBody?, code: Int) : super() {
        this.responseBody = responseBody
        statusCode = code
    }
}


