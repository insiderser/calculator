/*
 * Copyright 2020 Oleksandr Bezushko
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.insiderser.android.calculator.calculator

import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openContextualActionModeOverflowMenu
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isPlatformPopup
import androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.insiderser.android.calculator.R
import com.insiderser.android.calculator.test.rules.MainActivityRule
import com.insiderser.android.calculator.test.rules.TestPreferencesRule
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@RunWith(AndroidJUnit4::class)
class CalculatorFragmentTest {

    @Rule
    @JvmField
    val activityRule = MainActivityRule(R.id.calculator_dest)

    @Rule
    @JvmField
    val preferencesRule = TestPreferencesRule()

    @Test
    fun assert_clickingOnSettingsMenu_navigatesToSettings_then_navigateBack_returnsToCalculatorFragment() {
        // openActionBarOverflowOrOptionsMenu(...) doesn't pass in CI
        openContextualActionModeOverflowMenu()

        onView(withText(R.string.settings_title))
            .inRoot(isPlatformPopup())
            .check(matches(isCompletelyDisplayed()))
            .perform(click())

        checkInSettings()
        pressBack()
        checkInCalculatorFragment()
    }

    private fun checkInSettings() {
        onView(allOf(instanceOf(TextView::class.java), withParent(withId(R.id.toolbar))))
            .check(matches(withText(R.string.settings_title)))
    }

    private fun checkInCalculatorFragment() {
        onView(withText("=")).check(matches(isCompletelyDisplayed()))
        onView(withText("÷")).check(matches(isCompletelyDisplayed()))
    }
}
