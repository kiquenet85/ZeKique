package com.example.zemogatest.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * @author n.diazgranados
 * A simple decorator to generate margins without the 2x efect generated in XML files.
 */
class MarginItemDecoration(
    private val marginTop: Int = 0,
    private val marginBottom: Int = 0,
    private val marginSides: Int = 0
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView, state: RecyclerView.State
    ) {

        if (marginTop == 0 && marginBottom == 0) {
            applyOnlySideMargins(outRect, view, parent, state)
        } else {
            applyTopAtBeginningAndOtherMargins(outRect, view, parent, state)
        }
    }

    fun applyOnlySideMargins(
        outRect: Rect, view: View,
        parent: RecyclerView, state: RecyclerView.State
    ) {
        with(outRect) {
            left = marginSides
            right = marginSides
        }
    }

    fun applyTopAtBeginningAndOtherMargins(
        outRect: Rect, view: View,
        parent: RecyclerView, state: RecyclerView.State
    ) {
        with(outRect) {
            top = marginTop
            left = marginSides
            right = marginSides
            bottom = marginBottom
        }
    }
}
