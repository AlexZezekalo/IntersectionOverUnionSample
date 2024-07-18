package com.zezekalo.iou.app.matchers

import android.view.View
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher


class WithParentMatcher (
    private val parentMatcher: Matcher<View>,
) : TypeSafeMatcher<View>() {

    override fun describeTo(description: Description) {
        description.appendText("has parent matching: ")
        parentMatcher.describeTo(description)
    }

    override fun matchesSafely(view: View): Boolean {
        var parentMatch = false
        var viewParent = view.parent
        while (viewParent != null) {
            parentMatch = parentMatcher.matches(viewParent)
            if (parentMatch) break
            viewParent = viewParent.parent
        }
        return parentMatch
    }
}

fun withParentRecursively(parentMatcher: Matcher<View>): Matcher<View> {
    return WithParentMatcher(parentMatcher)
}