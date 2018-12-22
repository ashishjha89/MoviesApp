package moviesnow.com.moviesnow.api

/**
 * Decorator for the data (resource) returned from Repo. Data is decorated with status and message
 */
data class Resource<out T>(val status: ResourceStatus,
                           val data: T?,
                           val message: String?) {
    companion object {

        @JvmStatic
        fun <T> success(data: T) = Resource(ResourceStatus.SUCCESS, data, null)

        @JvmStatic
        fun <T> error(msg: String? = null, data: T? = null) = Resource(ResourceStatus.ERROR, data, msg)

        @JvmStatic
        fun <T> loading(data: T? = null) = Resource(ResourceStatus.LOADING, data, null)
    }

}