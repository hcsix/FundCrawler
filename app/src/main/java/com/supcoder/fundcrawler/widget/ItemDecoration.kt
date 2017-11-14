package com.supcoder.fundcrawler.widget

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * ItemDecoration
 * RecyclerView 分割线
 *
 * @author lee
 * @date 2017/11/13
 */
class ItemDecoration: RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
        outRect?.set(0, 0, 0, 10)
    }
}
