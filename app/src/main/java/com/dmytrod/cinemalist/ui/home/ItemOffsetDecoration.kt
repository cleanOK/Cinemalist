package com.dmytrod.cinemalist.ui.home

import android.graphics.Rect
import android.util.DisplayMetrics
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

class ItemOffsetDecoration(private val displayMetrics: DisplayMetrics) :
    RecyclerView.ItemDecoration() {

    companion object {
        const val LEFT = 0
        const val TOP = 0
        const val RIGHT = 0
        const val BOTTOM = 16
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.set(
            (displayMetrics.density * LEFT).roundToInt(),
            (displayMetrics.density * TOP).roundToInt(),
            (displayMetrics.density * RIGHT).roundToInt(),
            (displayMetrics.density * BOTTOM).roundToInt()
        )
    }
}