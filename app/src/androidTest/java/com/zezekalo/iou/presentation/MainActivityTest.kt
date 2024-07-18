package com.zezekalo.iou.presentation

import android.content.res.Resources
import androidx.annotation.IdRes
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.zezekalo.iou.app.matchers.withParentRecursively
import com.zezekalo.iou.presentation.ui.util.extensions.showBoxCoordinate
import com.zezekalo.iou.presentation.ui.util.extensions.showIoU
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@LargeTest
@HiltAndroidTest
class MainActivityTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    private val resources: Resources = getInstrumentation().targetContext.resources

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun initialBoxCoordinates_zero() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        activityScenario.moveToState(Lifecycle.State.RESUMED)

        checkIfAllCoordinateFieldsEqualsZero()

        onView(withId(R.id.unionBoxCoordinatesLayout)).check(matches(not(isDisplayed())))
        onView(withId(R.id.intersectionOverUnion)).check(matches(not(isDisplayed())))
        onView(withId(R.id.groundTruthBox)).check(matches(not(isDisplayed())))
        onView(withId(R.id.predictedBox)).check(matches(not(isDisplayed())))
        onView(withId(R.id.unionBox)).check(matches(not(isDisplayed())))

        activityScenario.close()
    }

    @Test
    fun inputValidBoxCoordinates_showBoxes() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        activityScenario.moveToState(Lifecycle.State.RESUMED)

        onView(withId(R.id.inputButton)).check(matches(isDisplayed()))
        onView(withId(R.id.inputButton)).perform(click())

        onView(withId(R.id.description)).check(matches(isDisplayed()))

        inputNumber(R.id.boxLeftInput, R.id.inputGroundTruthBoxCoordinatesLayout, "3")
        inputNumber(R.id.boxTopInput, R.id.inputGroundTruthBoxCoordinatesLayout, "3")
        inputNumber(R.id.boxRightInput, R.id.inputGroundTruthBoxCoordinatesLayout, "10")
        inputNumber(R.id.boxBottomInput, R.id.inputGroundTruthBoxCoordinatesLayout, "10")

        inputNumber(R.id.boxLeftInput, R.id.inputPredictedBoxCoordinatesLayout, "7")
        inputNumber(R.id.boxTopInput, R.id.inputPredictedBoxCoordinatesLayout, "7")
        inputNumber(R.id.boxRightInput, R.id.inputPredictedBoxCoordinatesLayout, "13")
        inputNumber(R.id.boxBottomInput, R.id.inputPredictedBoxCoordinatesLayout, "13")

        onView(withId(R.id.applyButton)).perform(click())

        onView(withId(R.id.customCoordinatePlateView)).check(matches(isDisplayed()))

        checkNumberInTheField(R.id.boxLeftText, R.id.groundTruthBoxCoordinatesLayout,
            3.showBoxCoordinate(resources.getString(R.string.left_hint)))
        checkNumberInTheField(R.id.boxTopText, R.id.groundTruthBoxCoordinatesLayout,
            3.showBoxCoordinate(resources.getString(R.string.top_hint)))
        checkNumberInTheField(R.id.boxRightText, R.id.groundTruthBoxCoordinatesLayout,
            10.showBoxCoordinate(resources.getString(R.string.right_hint)))
        checkNumberInTheField(R.id.boxBottomText, R.id.groundTruthBoxCoordinatesLayout,
            10.showBoxCoordinate(resources.getString(R.string.bottom_hint)))

        checkNumberInTheField(R.id.boxLeftText, R.id.predictedBoxCoordinatesLayout,
            7.showBoxCoordinate(resources.getString(R.string.left_hint)))
        checkNumberInTheField(R.id.boxTopText, R.id.predictedBoxCoordinatesLayout,
            7.showBoxCoordinate(resources.getString(R.string.top_hint)))
        checkNumberInTheField(R.id.boxRightText, R.id.predictedBoxCoordinatesLayout,
            13.showBoxCoordinate(resources.getString(R.string.right_hint)))
        checkNumberInTheField(R.id.boxBottomText, R.id.predictedBoxCoordinatesLayout,
            13.showBoxCoordinate(resources.getString(R.string.bottom_hint)))

        checkNumberInTheField(R.id.boxLeftText, R.id.unionBoxCoordinatesLayout,
            7.showBoxCoordinate(resources.getString(R.string.left_hint)))
        checkNumberInTheField(R.id.boxTopText, R.id.unionBoxCoordinatesLayout,
            7.showBoxCoordinate(resources.getString(R.string.top_hint)))
        checkNumberInTheField(R.id.boxRightText, R.id.unionBoxCoordinatesLayout,
            10.showBoxCoordinate(resources.getString(R.string.right_hint)))
        checkNumberInTheField(R.id.boxBottomText, R.id.unionBoxCoordinatesLayout,
            10.showBoxCoordinate(resources.getString(R.string.bottom_hint)))

        onView(withId(R.id.intersectionOverUnion))
            .check(matches(
                withText(0.118f.showIoU(resources.getString(R.string.intersection_over_union))))
            )

        onView(withId(R.id.groundTruthBox)).check(matches(isDisplayed()))
        onView(withId(R.id.predictedBox)).check(matches(isDisplayed()))
        onView(withId(R.id.unionBox)).check(matches(isDisplayed()))

        activityScenario.close()
    }

    @Test
    fun inputValidBoxCoordinates_clearButtonClick() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        activityScenario.moveToState(Lifecycle.State.RESUMED)

        onView(withId(R.id.inputButton)).check(matches(isDisplayed()))
        onView(withId(R.id.inputButton)).perform(click())

        onView(withId(R.id.description)).check(matches(isDisplayed()))

        inputNumber(R.id.boxLeftInput, R.id.inputGroundTruthBoxCoordinatesLayout, "3")
        inputNumber(R.id.boxTopInput, R.id.inputGroundTruthBoxCoordinatesLayout, "3")
        inputNumber(R.id.boxRightInput, R.id.inputGroundTruthBoxCoordinatesLayout, "10")
        inputNumber(R.id.boxBottomInput, R.id.inputGroundTruthBoxCoordinatesLayout, "10")

        inputNumber(R.id.boxLeftInput, R.id.inputPredictedBoxCoordinatesLayout, "7")
        inputNumber(R.id.boxTopInput, R.id.inputPredictedBoxCoordinatesLayout, "7")
        inputNumber(R.id.boxRightInput, R.id.inputPredictedBoxCoordinatesLayout, "13")
        inputNumber(R.id.boxBottomInput, R.id.inputPredictedBoxCoordinatesLayout, "13")

        onView(withId(R.id.applyButton)).perform(click())

        onView(withId(R.id.customCoordinatePlateView)).check(matches(isDisplayed()))

        checkNumberInTheField(R.id.boxLeftText, R.id.groundTruthBoxCoordinatesLayout,
            3.showBoxCoordinate(resources.getString(R.string.left_hint)))
        checkNumberInTheField(R.id.boxTopText, R.id.groundTruthBoxCoordinatesLayout,
            3.showBoxCoordinate(resources.getString(R.string.top_hint)))
        checkNumberInTheField(R.id.boxRightText, R.id.groundTruthBoxCoordinatesLayout,
            10.showBoxCoordinate(resources.getString(R.string.right_hint)))
        checkNumberInTheField(R.id.boxBottomText, R.id.groundTruthBoxCoordinatesLayout,
            10.showBoxCoordinate(resources.getString(R.string.bottom_hint)))

        checkNumberInTheField(R.id.boxLeftText, R.id.predictedBoxCoordinatesLayout,
            7.showBoxCoordinate(resources.getString(R.string.left_hint)))
        checkNumberInTheField(R.id.boxTopText, R.id.predictedBoxCoordinatesLayout,
            7.showBoxCoordinate(resources.getString(R.string.top_hint)))
        checkNumberInTheField(R.id.boxRightText, R.id.predictedBoxCoordinatesLayout,
            13.showBoxCoordinate(resources.getString(R.string.right_hint)))
        checkNumberInTheField(R.id.boxBottomText, R.id.predictedBoxCoordinatesLayout,
            13.showBoxCoordinate(resources.getString(R.string.bottom_hint)))

        checkNumberInTheField(R.id.boxLeftText, R.id.unionBoxCoordinatesLayout,
            7.showBoxCoordinate(resources.getString(R.string.left_hint)))
        checkNumberInTheField(R.id.boxTopText, R.id.unionBoxCoordinatesLayout,
            7.showBoxCoordinate(resources.getString(R.string.top_hint)))
        checkNumberInTheField(R.id.boxRightText, R.id.unionBoxCoordinatesLayout,
            10.showBoxCoordinate(resources.getString(R.string.right_hint)))
        checkNumberInTheField(R.id.boxBottomText, R.id.unionBoxCoordinatesLayout,
            10.showBoxCoordinate(resources.getString(R.string.bottom_hint)))

        onView(withId(R.id.intersectionOverUnion))
            .check(matches(
                withText(0.118f.showIoU(resources.getString(R.string.intersection_over_union))))
            )

        onView(withId(R.id.clearButton)).perform(click())

        checkIfAllCoordinateFieldsEqualsZero()

        onView(withId(R.id.unionBoxCoordinatesLayout)).check(matches(not(isDisplayed())))
        onView(withId(R.id.intersectionOverUnion)).check(matches(not(isDisplayed())))
        onView(withId(R.id.groundTruthBox)).check(matches(not(isDisplayed())))
        onView(withId(R.id.predictedBox)).check(matches(not(isDisplayed())))
        onView(withId(R.id.unionBox)).check(matches(not(isDisplayed())))

        activityScenario.close()
    }

    private fun checkIfAllCoordinateFieldsEqualsZero() {
        checkNumberInTheField(R.id.boxLeftText, R.id.groundTruthBoxCoordinatesLayout,
            0.showBoxCoordinate(resources.getString(R.string.left_hint)))
        checkNumberInTheField(R.id.boxTopText, R.id.groundTruthBoxCoordinatesLayout,
            0.showBoxCoordinate(resources.getString(R.string.top_hint)))
        checkNumberInTheField(R.id.boxRightText, R.id.groundTruthBoxCoordinatesLayout,
            0.showBoxCoordinate(resources.getString(R.string.right_hint)))
        checkNumberInTheField(R.id.boxBottomText, R.id.groundTruthBoxCoordinatesLayout,
            0.showBoxCoordinate(resources.getString(R.string.bottom_hint)))

        checkNumberInTheField(R.id.boxLeftText, R.id.predictedBoxCoordinatesLayout,
            0.showBoxCoordinate(resources.getString(R.string.left_hint)))
        checkNumberInTheField(R.id.boxTopText, R.id.predictedBoxCoordinatesLayout,
            0.showBoxCoordinate(resources.getString(R.string.top_hint)))
        checkNumberInTheField(R.id.boxRightText, R.id.predictedBoxCoordinatesLayout,
            0.showBoxCoordinate(resources.getString(R.string.right_hint)))
        checkNumberInTheField(R.id.boxBottomText, R.id.predictedBoxCoordinatesLayout,
            0.showBoxCoordinate(resources.getString(R.string.bottom_hint)))
    }

    private fun inputNumber(@IdRes inputLayoutId: Int, @IdRes parentLayoutId: Int, number: String) {
        onView(
            allOf(
                withId(inputLayoutId),
                withParentRecursively(withId(parentLayoutId))
            )
        ).check(
            matches(isDisplayed())
        ).perform(replaceText(number))
    }

    private fun checkNumberInTheField(@IdRes textViewId: Int, @IdRes parentLayoutId: Int, number: String) {
        onView(
            allOf(
                withId(textViewId),
                withParentRecursively(withId(parentLayoutId))
            )
        ).check(
            matches(allOf(
                isDisplayed(),
                withText(number))
            )
        )
    }
}