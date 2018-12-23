package moviesnow.com.moviesnow.movielist

import android.content.Intent
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.runner.AndroidJUnit4
import moviesnow.com.moviesnow.MatcherUtils.withRecyclerView
import moviesnow.com.moviesnow.R
import moviesnow.com.moviesnow.api.ApiProvider
import moviesnow.com.moviesnow.api.MockMoviesApi
import moviesnow.com.moviesnow.models.CurrentMovies
import moviesnow.com.moviesnow.models.Movie
import moviesnow.com.moviesnow.movieslist.MovieListActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MovieListActivityTest {

    @Rule
    @JvmField
    val activityRule = IntentsTestRule(MovieListActivity::class.java, true, false)

    @Before
    fun setup() {
        ApiProvider.initializeMockApi(api = MockMoviesApi())
    }

    @Test
    fun activityShouldOpenWithMovies() {
        val currentMovies = CurrentMovies(
            page = 1,
            totalPages = 1,
            results = listOf(
                Movie(title = "First Movie", posterPath = "Poster1", backdropPath = "Backdrop1"),
                Movie(title = "Second Movie", posterPath = "Poster2", backdropPath = "Backdrop2")
            )
        )
        (ApiProvider.getMoviesApi() as MockMoviesApi).currentMovies = currentMovies

        // Start activity
        activityRule.launchActivity(Intent())

        onView(withRecyclerView(R.id.moviesListView).atPositionOnView(0, R.id.movieImageView)).perform(click())

        onView(withText("First Movie")).check(matches(isDisplayed()))
    }

}