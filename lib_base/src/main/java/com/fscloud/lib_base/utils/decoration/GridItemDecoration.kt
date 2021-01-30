package com.fscloud.lib_base.utils.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * @ProjectName : kaidianbao
 * @Author : JackCui
 * @Time : 2020/9/18 11:28
 * @Description : grid 边距
 */
class GridItemDecoration(var spanCount: Int, var space: Int, var includeEdge: Boolean) : RecyclerView.ItemDecoration() {

    init {
        this.space = space / 2
    }


    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        if (spanCount > 0) {
            val position: Int = parent.getChildAdapterPosition(view)
            val column = position % spanCount
            if (includeEdge) {
                outRect.left = space - column * space / spanCount // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * space / spanCount // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = space
                }
                outRect.bottom = space // item bottom
            } else {
                outRect.left = column * space / spanCount // column * ((1f / spanCount) * spacing)
                outRect.right = space - (column + 1) * space / spanCount // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = space // item top
                }
            }
        } else {
            throw RuntimeException("列数必须大于0")
        }

    }
}