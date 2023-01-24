package com.lakbay.pamayanan.utils

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.max

class DotIndicatorDecorator : RecyclerView.ItemDecoration() {
    private val colorActive = 0xFFE5B94F
    private val colorInactive = 0xFFDBC591

    companion object {
        val DP = Resources.getSystem().displayMetrics.density
    }

    private val indicatorHeight = DP * 45
    private val indicatorHeightDotHidden =  DP * 40
    private val indicatorStrokeWidth =  DP * 10
    private val indicatorItemLength = DP * 10
    private val indicatorItemPadding = DP * 16
    private val interpolator = AccelerateDecelerateInterpolator()
    private val paint = Paint()

    init {
        paint.strokeWidth = indicatorStrokeWidth
        paint.style = Paint.Style.STROKE
        paint.isAntiAlias = true
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)

        val itemCount = parent.adapter!!.itemCount

        if(itemCount >= 2) {
            val totalLength = indicatorItemLength * itemCount
            val paddingBetweenItems = max(0, itemCount - 1) * indicatorItemPadding
            val indicatorTotalWidth = totalLength + paddingBetweenItems
            val indicatorStartX = (parent.width - indicatorTotalWidth) / 2F

            val indicatorPosY = parent.height - indicatorHeight / 2F

            drawInactiveIndicators(c, indicatorStartX, indicatorPosY, itemCount)

            val layoutManager = parent.layoutManager as LinearLayoutManager
            val activePosition = layoutManager.findFirstVisibleItemPosition()
            if(activePosition == RecyclerView.NO_POSITION) return

            val activeChild = layoutManager.findViewByPosition(activePosition)
            val left = activeChild!!.left
            val width = activeChild.width

            val progress =  interpolator.getInterpolation(left * -1 / width.toFloat())

            drawHighlights(c, indicatorStartX, indicatorPosY, activePosition, progress)
        }
    }

    private fun drawInactiveIndicators(c: Canvas, indicatorStartX: Float, indicatorPosY: Float, itemCount: Int) {
        paint.color = colorInactive.toInt()

        val itemWidth = indicatorItemLength + indicatorItemPadding

        var start = indicatorStartX
        for(i in 1..itemCount) {
            c.drawCircle(start, indicatorPosY, indicatorItemLength / 2F, paint)
            start += itemWidth
        }
    }

    private fun drawHighlights(c: Canvas, indicatorStartX: Float, indicatorPosyY: Float,
                               highlightPosition: Int, progress: Float) {
        paint.color = colorActive.toInt()

        val itemWidth = indicatorItemLength + indicatorItemPadding

        val highlightStart = indicatorStartX + itemWidth * highlightPosition

        if(progress == 0f) {
            c.drawCircle(highlightStart, indicatorPosyY, indicatorItemLength / 2F, paint)
        } else {
            val partialLength = indicatorItemLength * progress + indicatorItemPadding + progress
            c.drawCircle(highlightStart * partialLength, indicatorPosyY, indicatorItemLength / 2F, paint)
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = if (parent.adapter!!.itemCount >= 2)
            indicatorHeight.toInt() else indicatorHeightDotHidden.toInt()
    }
}