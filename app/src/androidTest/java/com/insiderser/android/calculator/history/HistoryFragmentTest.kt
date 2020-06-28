package com.insiderser.android.calculator.history

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openContextualActionModeOverflowMenu
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeRight
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.insiderser.android.calculator.R
import com.insiderser.android.calculator.test.ExpressionsHistoryEntityProvider
import com.insiderser.android.calculator.test.rules.AppDatabaseRule
import com.insiderser.android.calculator.test.rules.MainActivityRule
import com.insiderser.android.calculator.ui.MainActivity
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Locale

@RunWith(AndroidJUnit4::class)
class HistoryFragmentTest {

    @get:Rule
    val databaseRule = AppDatabaseRule()

    lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun setUp() {
        ExpressionsHistoryEntityProvider.list.forEach { entity ->
            databaseRule.db.historyDao.insertOne(entity)
        }

        val intent = MainActivityRule.getIntent(R.id.history_dest)
        scenario = launchActivity(intent)
    }

    @After
    fun tearDown() {
        scenario.close()
    }

    @Test
    fun checkHistoryListIsDisplayingItems() {
        checkEnglishLocale()

        ExpressionsHistoryEntityProvider.list.forEach { entity ->
            onView(withId(R.id.history_list))
                .check(matches(hasDescendant(withText(entity.expression))))
        }
    }

    @Test
    fun whenClickingOnExpression_shouldOpenCalculatorScreenWithSelectedExpression() {
        checkEnglishLocale()

        val expression = ExpressionsHistoryEntityProvider.item1.expression

        onView(withExpression(expression))
            .check(matches(isDisplayed()))
            .perform(click())

        checkInCalculatorFragment()

        onView(withId(R.id.expression))
            .check(matches(withText(expression)))
    }

    @Test
    fun whenSwipingOutExpression_shouldRemoveItFromList() {
        checkEnglishLocale()

        val expression = ExpressionsHistoryEntityProvider.item1.expression

        onView(withExpression(expression))
            .check(matches(isDisplayed()))
            .perform(swipeRight())

        // Espresso idling checker doesn't know anything about IO dispatcher,
        // so we have to wait until all work is completed (which we don't know when will happen).
        Thread.sleep(1000)

        onView(withExpression(expression))
            .check(doesNotExist())
    }

    @Test
    fun whenClickingOnClearHistoryContextMenu_listShouldBeEmpty() {
        openContextualActionModeOverflowMenu()

        onView(withText(R.string.history_clear_all))
            .check(matches(isCompletelyDisplayed()))
            .perform(click())

        onView(withId(R.id.history_list))
            .check(matches(isEmpty()))
    }

    private fun isEmpty() = not(hasDescendant(isDisplayed()))

    private fun withExpression(expression: String) = allOf(
        isDescendantOfA(withId(R.id.history_list)),
        withText(expression)
    )

    private fun checkEnglishLocale() {
        if (Locale.getDefault().language != Locale("en").language) {
            throw IllegalStateException("Cannot test on locales other than English")
        }
    }

    private fun checkInCalculatorFragment() {
        onView(withText("5")).check(matches(isCompletelyDisplayed()))
        onView(withText("=")).check(matches(isCompletelyDisplayed()))
        onView(withText("÷")).check(matches(isCompletelyDisplayed()))
    }
}
