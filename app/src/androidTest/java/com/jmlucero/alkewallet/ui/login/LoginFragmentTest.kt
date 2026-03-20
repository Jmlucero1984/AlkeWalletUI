package com.jmlucero.alkewallet.ui.login



import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.jmlucero.alkewallet.HiltTestActivity
import com.jmlucero.alkewallet.R
import com.jmlucero.alkewallet.fragments.LoginPageFragment
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginFragmentTest {
    @Test
    fun loginButton_isDisplayed() {
        launchFragmentInHiltContainer<LoginPageFragment>()

        onView(withId(R.id.loginButton))
            .check(matches(isDisplayed()))
    }
    inline fun <reified T : Fragment> launchFragmentInHiltContainer(
        fragmentArgs: Bundle? = null
    ) {
        val intent = Intent(
            ApplicationProvider.getApplicationContext(),
            HiltTestActivity::class.java
        )

        ActivityScenario.launch<HiltTestActivity>(intent).onActivity { activity ->
            val fragment = activity.supportFragmentManager.fragmentFactory.instantiate(
                requireNotNull(T::class.java.classLoader),
                T::class.java.name
            )
            fragment.arguments = fragmentArgs
            activity.supportFragmentManager.beginTransaction()
                .add(android.R.id.content, fragment, "")
                .commitNow()
        }
    }
}