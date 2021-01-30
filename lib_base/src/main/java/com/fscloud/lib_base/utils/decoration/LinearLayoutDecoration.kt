package com.fscloud.lib_base.utils.decoration

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LinearLayoutDecoration(var space: Int, private var mEdgeSpace:Int) :
    RecyclerView.ItemDecoration() {
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
    }


    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
    }


    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        var layoutManager = parent.layoutManager
        val childPosition = parent.getChildAdapterPosition(view)
        val itemCount = parent.adapter!!.itemCount
        if (layoutManager is LinearLayoutManager){
            var linearLayoutManager:LinearLayoutManager = layoutManager
            if (linearLayoutManager.orientation == LinearLayout.HORIZONTAL){
                handleHorizontal(outRect, view, parent,childPosition,itemCount)
            }else{
                handleVertical(outRect, view, parent,childPosition,itemCount)
            }
        }
    }

    private fun handleVertical(outRect: Rect, view: View, parent: RecyclerView,childPosition:Int,itemCount:Int) {
        //纵向布局
        when (childPosition) {
            0 -> {
                // 第一个要设置PaddingTop
                outRect.set(0, mEdgeSpace, 0, space)
            }
            itemCount - 1 -> {
                // 最后一个要设置PaddingBottom
                outRect.set(0, 0, 0, mEdgeSpace)
            }
            else -> {
                outRect.set(0, 0, 0, space)

            }
        }

    }

    private fun handleHorizontal(outRect: Rect, view: View, parent: RecyclerView,childPosition:Int,itemCount:Int) {
        when (childPosition) {
            0 -> {
                // 第一个要设置PaddingLeft
                outRect.set(mEdgeSpace, 0, space, 0);
            }
            itemCount - 1 -> {
                // 最后一个设置PaddingRight
                outRect.set(0, 0, mEdgeSpace, 0);
            }
            else -> {
                outRect.set(0, 0, space, 0);
            }
        }
    }
}