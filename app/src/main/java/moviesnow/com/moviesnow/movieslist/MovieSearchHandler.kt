package moviesnow.com.moviesnow.movieslist

import android.os.Handler
import android.os.Message
import java.lang.ref.WeakReference

class MovieSearchHandler(viewModel: MoviesViewModel) {

    companion object {
        private const val UPDATE_QUERY_INTERVAL = 200L
        private const val SEARCH = 16
    }

    private var handler = QueryHandler(viewModel)

    fun setQueryString(query: String) {
        val msg = handler.obtainMessage().apply {
            what = SEARCH
            obj = query.trim()
        }
        handler.removeMessages(SEARCH)
        handler.sendMessageDelayed(msg, UPDATE_QUERY_INTERVAL)
    }

    private class QueryHandler(viewModel: MoviesViewModel) : Handler() {

        private val actionListenerRef = WeakReference<MoviesViewModel>(viewModel)

        override fun handleMessage(msg: Message) {
            val viewModel = actionListenerRef.get() ?: return
            if (msg.what == SEARCH) {
                val searchQuery = msg.obj as String
                viewModel.searchMovie(searchQuery)
            }
        }
    }
}