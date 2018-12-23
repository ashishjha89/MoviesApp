package moviesnow.com.moviesnow.movielist

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNull
import moviesnow.com.moviesnow.api.MoviesApi
import moviesnow.com.moviesnow.api.Resource
import moviesnow.com.moviesnow.api.ResourceStatus
import moviesnow.com.moviesnow.models.CurrentMovies
import moviesnow.com.moviesnow.models.Movie
import moviesnow.com.moviesnow.movieslist.MoviesCache
import moviesnow.com.moviesnow.movieslist.MoviesRepo
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
@PrepareForTest(MoviesRepo::class)
class MoviesRepoTest {

    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var moviesApi: MoviesApi

    @Test
    fun getCurrentMovies_PageOne() {
        // Mock stuff
        val moviesInPageOne = MutableLiveData<Resource<CurrentMovies>>()
        given(moviesApi.getCurrentMovies(pageNumber = 1)).willReturn(moviesInPageOne)

        // Initialize Repo
        val moviesCache = MoviesCache()
        val repo = MoviesRepo(moviesApi, moviesCache)

        // Call method
        val movies = repo.getCurrentMovies(pageNumber = 1)
        movies.observeForever { }

        run {
            moviesInPageOne.value = Resource.loading()
            assertEquals(ResourceStatus.LOADING, movies.value?.status)
        }

        run {
            moviesInPageOne.value = Resource.error()
            assertEquals(ResourceStatus.ERROR, movies.value?.status)
        }

        run {
            // Movies without valid images
            val currentMoviesRes = CurrentMovies(
                page = 1,
                totalPages = 2,
                results = listOf(Movie(title = "First Movie"))
            )
            moviesInPageOne.value = Resource.success(data = currentMoviesRes)
            assertEquals(ResourceStatus.SUCCESS, movies.value?.status)
            assertEquals(0, movies.value?.data?.results?.size)
        }

        run {
            // Movies with only poster images
            val currentMoviesRes = CurrentMovies(
                page = 1,
                totalPages = 2,
                results = listOf(
                    Movie(
                        title = "First Movie",
                        posterPath = "Poster"
                    )
                )
            )
            moviesInPageOne.value = Resource.success(data = currentMoviesRes)
            assertEquals(ResourceStatus.SUCCESS, movies.value?.status)
            // Movies should be filtered out
            assertEquals(0, movies.value?.data?.results?.size)
        }

        run {
            // Movies with only backdrop images
            val currentMoviesRes = CurrentMovies(
                page = 1,
                totalPages = 2,
                results = listOf(
                    Movie(
                        title = "First Movie",
                        backdropPath = "Backdrop"
                    )
                )
            )
            moviesInPageOne.value = Resource.success(data = currentMoviesRes)
            assertEquals(ResourceStatus.SUCCESS, movies.value?.status)
            // Movies should be filtered out
            assertEquals(0, movies.value?.data?.results?.size)
        }

        run {
            // Movies with only valid images
            val currentMoviesRes = CurrentMovies(
                page = 1,
                totalPages = 2,
                results = listOf(
                    Movie(
                        title = "First Movie",
                        posterPath = "Poster",
                        backdropPath = "Backdrop"
                    )
                )
            )
            moviesInPageOne.value = Resource.success(data = currentMoviesRes)
            assertEquals(ResourceStatus.SUCCESS, movies.value?.status)
            // Now Movies should be included
            assertEquals(1, movies.value?.data?.results?.size)
            assertEquals(currentMoviesRes, movies.value?.data)
            assertEquals(currentMoviesRes, moviesCache.cachedMovies)
        }
    }

    @Test
    fun getCurrentMovies_PageTwo() {
        // Mock stuff
        val moviesInPageTwo = MutableLiveData<Resource<CurrentMovies>>()
        given(moviesApi.getCurrentMovies(pageNumber = 2)).willReturn(moviesInPageTwo)
        val cachedPageOneMovies = CurrentMovies(page = 1, totalPages = 2, results = listOf(Movie(title = "First Movie", posterPath = "Poster", backdropPath = "Backdrop")))

        // Initialize Repo
        val moviesCache = MoviesCache().apply { cachedMovies = cachedPageOneMovies }
        val repo = MoviesRepo(moviesApi, moviesCache)

        // Call method with page=2
        val movies = repo.getCurrentMovies(pageNumber = 2)
        movies.observeForever { }

        // Movies with only valid images
        val currentMoviesRes = CurrentMovies(
            page = 2,
            totalPages = 2,
            results = listOf(
                Movie(
                    title = "Second Movie",
                    posterPath = "Poster",
                    backdropPath = "Backdrop"
                )
            )
        )
        moviesInPageTwo.value = Resource.success(data = currentMoviesRes)
        assertEquals(ResourceStatus.SUCCESS, movies.value?.status)
        // Old + New Movies should be appended
        assertEquals(2, movies.value?.data?.results?.size)
        assertEquals("First Movie", movies.value?.data?.results?.get(0)?.title)
        assertEquals("Second Movie", movies.value?.data?.results?.get(1)?.title)
        assertEquals("First Movie", moviesCache.cachedMovies?.results?.get(0)?.title)
        assertEquals("Second Movie", moviesCache.cachedMovies?.results?.get(1)?.title)
    }

    @Test
    fun getMovieSearchResultTest() {
        // Mock stuff
        val moviesInPageOne = MutableLiveData<Resource<CurrentMovies>>()
        given(moviesApi.getMovieSearchResult(pageNumber = 1, query = "Batman")).willReturn(moviesInPageOne)
        val cachedPageOneMovies = CurrentMovies(page = 1, totalPages = 2, results = listOf(Movie(title = "Old Movie", posterPath = "Poster", backdropPath = "Backdrop")))

        // Initialize Repo
        val moviesCache = MoviesCache().apply { cachedMovies = cachedPageOneMovies }
        val repo = MoviesRepo(moviesApi, moviesCache)

        // Call method
        val movies = repo.getMovieSearchResult(pageNumber = 1, query = "Batman")
        movies.observeForever { }

        val currentMoviesRes = CurrentMovies(
            page = 1,
            totalPages = 2,
            results = listOf(Movie(title = "First Movie", posterPath = "Poster", backdropPath = "Backdrop"))
        )
        moviesInPageOne.value = Resource.success(data = currentMoviesRes)
        assertEquals(ResourceStatus.SUCCESS, movies.value?.status)
        assertEquals("First Movie", movies.value?.data?.results?.get(0)?.title)
        assertEquals("First Movie", moviesCache.cachedMovies?.results?.get(0)?.title)
    }
}