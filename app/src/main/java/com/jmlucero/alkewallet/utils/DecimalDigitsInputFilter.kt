package com.jmlucero.alkewallet.utils

import android.text.InputFilter
import android.text.Spanned

class DecimalDigitsInputFilter(
    private val digitsBeforeZero: Int,
    private val digitsAfterZero: Int
) : InputFilter {

    private val pattern = Regex(
        "^\\d{0,$digitsBeforeZero}((\\.\\d{0,$digitsAfterZero})?)$"
    )

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence? {

        val newValue = dest.toString()
            .substring(0, dstart) +
                source +
                dest.toString().substring(dend)

        return if (pattern.matches(newValue)) null else ""
    }
}