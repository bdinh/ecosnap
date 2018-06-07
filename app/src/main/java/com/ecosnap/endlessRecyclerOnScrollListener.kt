//package com.ecosnap
//
//import android.support.v7.widget.LinearLayoutManager
//import android.support.v7.widget.RecyclerView
//import com.ecosnap.Adapters.CustomSectionHistoryViewHolder
//import com.ecosnap.Adapters.SectionHistoryRecyclerViewAdapter
//
////abstract class EndlessRecyclerOnScrollListener(val mLinearLayoutManager: LinearLayoutManager) : RecyclerView.OnScrollListener() {
////    private var prevTotal: Int = 0
////    private var loading: Boolean = true
////    private var visibleThreshold: Int = 5
////    private var firstVisibleItem: Int = -1
////    private var visibleItemCount: Int = -1
////    private var totalItemCount: Int = -1
////
////    private var currentPage: Int = 1
////
////    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
////        super.onScrolled(recyclerView, dx, dy)
////
////        this.visibleItemCount = recyclerView!!.childCount
////        this.totalItemCount = mLinearLayoutManager.itemCount
////        this.firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition()
////
////        if (this.loading) {
////            if (totalItemCount > prevTotal) {
////                this.loading = false
////                this.prevTotal = this.totalItemCount
////            }
////        }
////
////        if (!loading && (this.totalItemCount - this.visibleItemCount)
////                <= (this.firstVisibleItem + this.visibleThreshold)) {
////            currentPage++
////            onLoadMore(currentPage)
////            loading = true
////        }
////
////    }
////
////    abstract fun onLoadMore(current_page: Int)
////}
//
//class OnScrollListener(val layoutManager: LinearLayoutManager, val adapter: RecyclerView.Adapter<CustomSectionHistoryViewHolder>, val dataList: MutableList<Int>) : RecyclerView.OnScrollListener() {
//    var previousTotal = 0
//    var loading = true
//    val visibleThreshold = 10
//    var firstVisibleItem = 0
//    var visibleItemCount = 0
//    var totalItemCount = 0
//
//    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//        super.onScrolled(recyclerView, dx, dy)
//
//        visibleItemCount = recyclerView.childCount
//        totalItemCount = layoutManager.itemCount
//        firstVisibleItem = layoutManager.findFirstVisibleItemPosition()
//
//        if (loading) {
//            if (totalItemCount > previousTotal) {
//                loading = false
//                previousTotal = totalItemCount
//            }
//        }
//
//        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
//            val initialSize = dataList.size
//            updateDataList(dataList)
//            val updatedSize = dataList.size
//            recyclerView.post { adapter.notifyItemRangeInserted(initialSize, updatedSize) }
//            loading = true
//        }
//    }
//}