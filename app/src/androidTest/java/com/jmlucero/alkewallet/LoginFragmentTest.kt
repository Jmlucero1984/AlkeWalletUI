package com.jmlucero.alkewallet


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4

import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Before
import org.junit.Rule

import androidx.fragment.app.testing.launchFragmentInContainer


import androidx.test.filters.MediumTest

import com.jmlucero.alkewallet.fragments.AuthSelectionFragment


@MediumTest
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class LoginFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun loginButton_isDisplayed() {
        // Crear el fragment con Hilt support
        val scenario = launchFragmentInContainer<AuthSelectionFragment>()

        // Verificar que el botón existe y está visible
        onView(withId(R.id.btnLogin))
            .check(matches(isDisplayed()))
    }
}