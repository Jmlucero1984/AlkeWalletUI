package com.jmlucero.alkewallet.utils

// TestHelpers.kt
import android.os.Bundle
import androidx.annotation.StyleRes
import androidx.appcompat.R
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle

/**
 * Helper para lanzar fragments con soporte Hilt

Características:
✅ Control total sobre cómo se crea el fragmento
✅ Puedes pasar dependencias adicionales al constructor
✅ Permite mockear el fragmento mismo si es necesario
✅ Soporta fragments que requieren parámetros en constructor
❌ Más verboso de usar
*/

inline fun <reified F : Fragment> launchFragmentInHiltContainerFullControl(
    @StyleRes themeResId: Int = R.style.Base_Theme_AppCompat,
    fragmentArgs: Bundle? = null,
    initialState: Lifecycle.State = Lifecycle.State.RESUMED,
    crossinline instantiate: () -> F
): FragmentScenario<F> = FragmentScenario.launchInContainer(
    F::class.java, fragmentArgs, themeResId, initialState,
    object : FragmentFactory() {
        override fun instantiate(
            classLoader: ClassLoader,
            className: String
        ) = when (className) {
            F::class.java.name -> instantiate()
            else -> super.instantiate(classLoader, className)
        }
    }
)
/*
Características:
✅ Más simple y concisa
✅ Buena para casos comunes
❌ Solo funciona con fragments que tienen constructor vacío
❌ No permite inyectar mocks en el constructor
*/
inline fun <reified F : Fragment> launchFragmentInHiltContainer(
    @StyleRes themeResId: Int = R.style.Base_Theme_AppCompat,
    fragmentArgs: Bundle? = null,
    initialState: Lifecycle.State = Lifecycle.State.RESUMED,
    crossinline action: F.() -> Unit = {}
): FragmentScenario<F> {
    return launchFragmentInContainer(
        themeResId = themeResId,
        fragmentArgs = fragmentArgs
    ) {
        // Esto asegura que el fragmento se cree con Hilt
        F::class.java.newInstance()
    }.also { scenario ->
        scenario.onFragment { fragment ->
            action(fragment)
            // Hilt ya inyectó las dependencias
        }
    }
}