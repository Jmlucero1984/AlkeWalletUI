package com.jmlucero.alkewallet

import android.text.SpannableString
import com.jmlucero.alkewallet.utils.DecimalDigitsInputFilter
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(manifest=Config.NONE)
@RunWith(RobolectricTestRunner::class)
class DecimalFilterTest {
    private lateinit var filter: DecimalDigitsInputFilter

    @Before
    fun setup() {
        filter = DecimalDigitsInputFilter(10, 2)
    }

    @Test
    fun allow_integer_input() {
        val result = filter.filter(
            "123",
            0,
            3,
            SpannableString(""),
            0,
            0
        )

        assertNull(result)
    }

    @Test
    fun allow_two_decimal_places() {
        val result = filter.filter(
            "123.45",
            0,
            6,
            SpannableString(""),
            0,
            0
        )

        assertNull(result)
    }

    @Test
    fun reject_third_decimal_digit_when_typing() {
        val dest = SpannableString("123.45")
        val source = "6"

        val result = filter.filter(
            source,
            0,
            1,
            dest,
            dest.length,
            dest.length
        )

        assertEquals("", result)
    }

    @Test
    fun allow_second_decimal_digit_when_typing() {
        val dest = SpannableString("123.4")
        val source = "5"

        val result = filter.filter(
            source,
            0,
            1,
            dest,
            dest.length,
            dest.length
        )

        assertNull(result)
    }

    @Test
    fun reject_multiple_dots() {
        val result = filter.filter(
            "123..4",
            0,
            6,
            SpannableString(""),
            0,
            0
        )

        assertEquals("", result)
    }

    @Test
    fun allow_deleting_characters() {
        val dest = SpannableString("123.45")

        val result = filter.filter(
            "", // borrar
            0,
            0,
            dest,
            5,
            6
        )

        assertNull(result)
    }
}