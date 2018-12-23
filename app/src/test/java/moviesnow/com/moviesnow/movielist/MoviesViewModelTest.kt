package moviesnow.com.moviesnow.movielist

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import junit.framework.Assert.assertEquals
import moviesnow.com.moviesnow.api.Resource
import moviesnow.com.moviesnow.api.ResourceStatus
import moviesnow.com.moviesnow.models.CurrentMovies
import moviesnow.com.moviesnow.models.Movie
import moviesnow.com.moviesnow.movieslist.MoviesRepo
import moviesnow.com.moviesnow.movieslist.MoviesViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
@PrepareForTest(MoviesViewModel::class, MoviesRepo::class)
class MoviesViewModelTest {

    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var moviesRepo: MoviesRepo

    @Test
    fun initializationShouldLoadMovies() {
        // Mock stuff
        val moviesInPageOne = MutableLiveData<Resource<CurrentMovies>>()
        given(moviesRepo.getCurrentMovies(pageNumber = 1)).willReturn(moviesInPageOne)

        // Initialize ViewModel
        val viewModel = MoviesViewModel(moviesRepo)
        observeData(viewModel)

        run {
            moviesInPageOne.value = Resource.loading()
            assertEquals(ResourceStatus.LOADING, viewModel.movieList.value?.status)
        }

        run {
            moviesInPageOne.value = Resource.error()
            assertEquals(ResourceStatus.ERROR, viewModel.movieList.value?.status)
        }

        run {
            val currentMoviesRes = CurrentMovies(
                page = 1,
                totalPages = 2,
                results = listOf(Movie(title = "First Movie"))
            )
            moviesInPageOne.value = Resource.success(data = currentMoviesRes)
            assertEquals(ResourceStatus.SUCCESS, viewModel.movieList.value?.status)
            assertEquals(currentMoviesRes, viewModel.movieList.value?.data)
        }
    }

    @Test
    fun loadNextPage_ShouldRequestRepoWithNextPage() {
        // Mock stuff
        val moviesInPageOne = MutableLiveData<Resource<CurrentMovies>>()
        given(moviesRepo.getCurrentMovies(pageNumber = 1)).willReturn(moviesInPageOne)
        val moviesInPageTwo = MutableLiveData<Resource<CurrentMovies>>()
        given(moviesRepo.getCurrentMovies(pageNumber = 2)).willReturn(moviesInPageTwo)
        val currentMoviesInPageOne = CurrentMovies(page = 1, totalPages = 2, results = listOf(Movie(title = "First Movie")))
        val currentMoviesInPageTwo = CurrentMovies(page = 2, totalPages = 2, results = listOf(Movie(title = "First Movie"), Movie(title = "Second Movie")))

        // Initialize ViewModel
        val viewModel = MoviesViewModel(moviesRepo)
        observeData(viewModel)

        // Constructor should request repo pageNumber=1
        verify(moviesRepo).getCurrentMovies(pageNumber = 1)
        moviesInPageOne.value = Resource.success(data = currentMoviesInPageOne)

        // Assert values resulting from loadNextPage by constructor
        assertEquals(ResourceStatus.SUCCESS, viewModel.movieList.value?.status)
        assertEquals(currentMoviesInPageOne, viewModel.movieList.value?.data)


        // Call method to load next page
        viewModel.loadNextPage()

        // Explicit call to loadNextPage should request repo with next pageNumber (2 in this case)
        verify(moviesRepo).getCurrentMovies(pageNumber = 2)
        moviesInPageTwo.value = Resource.success(data = currentMoviesInPageTwo)

        // Assert values resulting from explicit call to nextPage
        assertEquals(ResourceStatus.SUCCESS, viewModel.movieList.value?.status)
        assertEquals("First Movie", viewModel.movieList.value?.data?.results?.get(0)?.title)
        assertEquals("Second Movie", viewModel.movieList.value?.data?.results?.get(1)?.title)
    }

    @Test
    fun searchMovieShouldRequestWithQuery() {
        // Mock stuff
        val moviesInPageOne = MutableLiveData<Resource<CurrentMovies>>()
        given(moviesRepo.getMovieSearchResult(pageNumber = 1, query = "Batman")).willReturn(moviesInPageOne)

        // Initialize ViewModel
        val viewModel = MoviesViewModel(moviesRepo)
        observeData(viewModel)

        // Call method
        viewModel.searchMovie(query = "Batman")

        // Repo requested with pageNumber=1 and query
        verify(moviesRepo).getMovieSearchResult(pageNumber = 1, query = "Batman")

        // Assert values in Page 1
        val currentMoviesInPageOne = CurrentMovies(page = 1, totalPages = 2, results = listOf(Movie(title = "Batman Begins")))
        moviesInPageOne.value = Resource.success(data = currentMoviesInPageOne)
        assertEquals(ResourceStatus.SUCCESS, viewModel.movieList.value?.status)
        assertEquals(currentMoviesInPageOne, viewModel.movieList.value?.data)

        // Now call loadMore
        viewModel.loadNextPage()

        // Repo requested with pageNumber=2 and query=Batman
        verify(moviesRepo).getMovieSearchResult(pageNumber = 2, query = "Batman")
    }

    private fun observeData(viewModel: MoviesViewModel) {
        viewModel.movieList.observeForever {  }
    }
}