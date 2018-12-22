package moviesnow.com.moviesnow.api

enum class ResourceStatus {
    /**
     * Represents successful (valid) state of Model received from repo
     */
    SUCCESS,
    /**
     * Represents state when repo returns error or invalid (unexpected values of) model
     */
    ERROR,
    /**
     * Represents repo's background request is ongoing
     */
    LOADING
}
