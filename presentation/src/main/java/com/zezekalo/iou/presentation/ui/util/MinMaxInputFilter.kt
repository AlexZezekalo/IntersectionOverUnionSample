package com.zezekalo.iou.presentation.ui.util

import android.text.InputFilter
import android.text.Spanned

class MinMaxInputFilter(
    private val min: Int,
    private val max: Int,
) : InputFilter {
    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int,
    ): CharSequence? {
        try {
            var newVal = dest.toString().substring(0, dstart) + dest.toString().substring(dend, dest.toString().length)
            newVal = (newVal.substring(0, dstart) + source.toString()) + newVal.substring(dstart, newVal.length)
            val input = newVal.toInt()

            if (isInRange(min, max, input)) {
                return null
            }
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
        return ""
    }

    private fun isInRange(
        min: Int,
        max: Int,
        value: Int,
    ): Boolean {
        return if (max > min) value in min..max else value in max..min
    }
}
