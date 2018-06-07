package com.ecosnap.adapters

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

abstract class EndlessRecyclerOnScrollListener(val mLinearLayoutManager: LinearLayoutManager) : RecyclerView.OnScrollListener() {
    private var prevTotal: Int = 0
    private var loading: Boolean = true
    private var visibleThreshold: Int = 5
    private var firstVisibleItem: Int = -1
    private var visibleItemCount: Int = -1
    private var totalItemCount: Int = -1
    private var currentPage: Int = 1

    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        this.visibleItemCount = recyclerView!!.childCount
        this.totalItemCount = mLinearLayoutManager.itemCount
        this.firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition()

        if (this.loading) {
            if (totalItemCount > prevTotal) {
                this.loading = false
                this.prevTotal = this.totalItemCount
            }
        }

        if (!loading && (this.totalItemCount - this.visibleItemCount)
                <= (this.firstVisibleItem + this.visibleThreshold)) {
            currentPage++
            onLoadMore(currentPage)
            loading = true
        }

    }

    abstract fun onLoadMore(current_page: Int)
}

