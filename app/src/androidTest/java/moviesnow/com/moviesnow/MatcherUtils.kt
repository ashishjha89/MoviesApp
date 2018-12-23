package moviesnow.com.moviesnow

import android.content.res.Resources
import android.support.v7.widget.RecyclerView
import android.view.View
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

object MatcherUtils {

    fun withRecyclerView(recyclerViewId: Int) = RecyclerViewMatcher(recyclerViewId)

    class RecyclerViewMatcher(private val recyclerViewId: Int) {

        fun atPositionOnView(position: Int, targetViewId: Int): Matcher<View> {

            return object : TypeSafeMatcher<View>() {
                var resources: Resources? = null
                var childView: View? = null

                override fun describeTo(description: Description) {
                    var idDescription = Integer.toString(recyclerViewId)
                    resources?.let {
                        idDescription = try {
                            it.getResourceName(recyclerViewId)
                        } catch (var4: Resources.NotFoundException) {
                            String.format("%s (resource name not found)", recyclerViewId)
                        }
                    }

                    description.appendText("with id: $idDescription")
                }

                public override fun matchesSafely(view: View): Boolean {

                    this.resources = view.resources

                    if (childView == null) {
                        val recyclerView = view.rootView.findViewById<RecyclerView>(recyclerViewId)
                        if (recyclerView != null && recyclerView.id == recyclerViewId) {
                            childView = recyclerView.findViewHolderForAdapterPosition(position).itemView
                        } else {
                            return false
                        }
                    }

                    return if (targetViewId == -1) {
                        view === childView
                    } else {
                        view === childView!!.findViewById<View>(targetViewId)
                    }

                }
            }
        }
    }
}
