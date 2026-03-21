package com.jmlucero.alkewallet


import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.espresso.matcher.ViewMatchers.isClickable
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4

import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Before
import org.junit.Rule


import androidx.test.filters.MediumTest
import com.jmlucero.alkewallet.data.api.RetrofitClient
import com.jmlucero.alkewallet.data.room.db.DatabaseModule
import com.jmlucero.alkewallet.di.AppModule

import com.jmlucero.alkewallet.fragments.AuthSelectionFragment
import com.jmlucero.alkewallet.fragments.LoginPageFragment
import com.jmlucero.alkewallet.utils.launchFragmentInHiltContainerFullControl
import com.jmlucero.alkewallet.viewmodel.AuthViewModel
import dagger.hilt.android.testing.UninstallModules
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.Assert.assertTrue
import javax.inject.Inject


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
        //val scenario = launchFragmentInContainer<AuthSelectionFragment>()
        val navController = mockk<NavController>(relaxed = true)
        val scenario = launchFragmentInHiltContainerFullControl<AuthSelectionFragment>(
            themeResId = androidx.appcompat.R.style.Base_Theme_AppCompat,
            fragmentArgs = null,
            initialState = Lifecycle.State.RESUMED,
            instantiate = { AuthSelectionFragment().also { fragment ->
                    fragment.viewLifecycleOwnerLiveData.observeForever { owner ->
                        if (owner != null) {
                            Navigation.setViewNavController(
                                fragment.requireView(),
                                navController
                            )
                        }
                    }
                }
            }
        )
        // Verificar que el botón existe y está visible
        onView(withId(R.id.btnLogin))
            .check(matches(isDisplayed())).perform(click())
        Thread.sleep(1000) // 👀 te deja ver el resultado
        verify {
            navController.navigate(R.id.action_auth_to_login)
        }

        //val navController = mockk<NavController>(relaxed = true)
    }

    @Test
    fun loginButton_to_loginFragment() {
        val navController = mockk<NavController>(relaxed = true)
        val scenario = launchFragmentInHiltContainerFullControl<AuthSelectionFragment>(
            themeResId = androidx.appcompat.R.style.Base_Theme_AppCompat,
            fragmentArgs = null,
            initialState = Lifecycle.State.RESUMED,
            instantiate = { AuthSelectionFragment().also { fragment ->
                fragment.viewLifecycleOwnerLiveData.observeForever { owner ->
                    if (owner != null) {
                        Navigation.setViewNavController(
                            fragment.requireView(),
                            navController
                        )
                    }
                }
            }
            }
        )
        scenario.onFragment { fragment ->
            fragment.viewLifecycleOwnerLiveData.observeForever { owner ->
                if (owner != null) {
                    Navigation.setViewNavController(fragment.requireView(), navController)
                }
            }
        }

        // Simular navegación capturando el destino
        val destination = slot<NavDestination>()
        every { navController.navigate(capture(destination)) } answers {
            // Simular la navegación real
            val navDestination = destination.captured
            // Aquí podrías lanzar el nuevo fragment si quieres
        }

        onView(withId(R.id.btnLogin)).perform(click())

        // Verificar que se llamó a navigate con el ID correcto
        verify { navController.navigate(R.id.action_auth_to_login) }

        // Opción: Verificar que el fragmento destino existe en el gráfico de navegación
        val graph = navController.navInflater.inflate(R.navigation.nav_graph)
        val destinationId = R.id.loginFragment // o el ID de tu fragmento destino
        val destinationExists = graph.findNode(destinationId)
        println("********************************")
        println(destinationExists)

        //assertTrue(destinationExists.isActionPresent(R.id.action_auth_to_login))
    }


    @Test
    fun loginButton_showsLoginScreen() {
        // Usar ActivityScenario con tu Activity real
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        // Crear el ViewModel mockeado

        Thread.sleep(1000)

        // Hacer clic en el botón de login
        onView(withId(R.id.btnLogin))
            .check(matches(isDisplayed()))
            .check(matches(isEnabled()))
            .check(matches(isClickable()))
            .perform(click())


        Thread.sleep(1000)
        // También puedes verificar que elementos del LoginFragment aparecen
        onView(withId(R.id.emailInput)).check(matches(isDisplayed()))
        onView(withId(R.id.passwordInput)).check(matches(isDisplayed()))
    }


}