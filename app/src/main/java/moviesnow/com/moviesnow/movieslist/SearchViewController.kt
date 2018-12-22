package moviesnow.com.moviesnow.movieslist

import android.support.v7.widget.SearchView
import android.view.MenuItem

class SearchViewController(
    private val movieSearchHandler: MovieSearchHandler,
    private val viewModel: MoviesViewModel
) {

    fun setSearchView(searchView: SearchView, searchMenuItem: MenuItem) {
        if (viewModel.lastSearchQuery.isNotEmpty()) {
            searchView.isIconified = false
            searchMenuItem.expandActionView()
            searchView.setQuery(viewModel.lastSearchQuery, false)
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (!searchView.isIconified) {
                    searchView.isIconified = true
                }
                searchMenuItem.collapseActionView()
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                movieSearchHandler.setQueryString(query)
                return false
            }
        })
    }
}