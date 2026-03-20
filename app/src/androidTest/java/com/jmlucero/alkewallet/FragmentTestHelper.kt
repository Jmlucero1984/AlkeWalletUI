package com.jmlucero.alkewallet

import android.R
import android.content.Intent
import androidx.fragment.app.Fragment

import androidx.test.core.app.ApplicationProvider

import android.os.Bundle
import androidx.fragment.app.testing.FragmentScenario

import androidx.test.core.app.ActivityScenario

import dagger.hilt.android.testing.HiltAndroidRule
import org.junit.rules.TestRule

inline fun <reified T : Fragment> launchFragmentInHiltContainer(
    fragmentArgs: Bundle? = null,
    themeResId: Int? = null,
    crossinline action: T.() -> Unit = {}
): FragmentScenario<T> {
    return FragmentScenario.launchInContainer(
        T::class.java,
        fragmentArgs,
        themeResId ?: R.style.Theme_Light
    )
}
