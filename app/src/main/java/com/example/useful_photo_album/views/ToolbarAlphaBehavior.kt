package com.example.useful_photo_album.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.example.useful_photo_album.R
import kotlin.math.roundToInt

class ToolbarAlphaBehavior(private val context: Context, private val attrs: AttributeSet) : CoordinatorLayout.Behavior<Toolbar>(context, attrs) {

    private var offset = 0
    private var startOffset = 0
    private var endOffset = 0


    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: Toolbar,
        directTargetChild: View,
        target: View,
        axes: Int
    ): Boolean {
        return true
    }

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: Toolbar,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int
    ) {
        val startOffset = 0
        val endOffset = context.resources.getDimensionPixelOffset(R.dimen.header_height) - child.height

        offset += dyConsumed

        if (offset <= startOffset) {
            child.background.alpha = 0
        } else if (offset in (startOffset + 1) until endOffset) {
            val percent: Float = (offset - startOffset).toFloat() / endOffset
            val alpha = (percent * 255).roundToInt()
            child.background.alpha = alpha
        } else if (offset >= endOffset) {
            child.background.alpha = 255
        }
    }
}