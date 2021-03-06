package com.kondenko.pocketwaka.ui

import android.content.Context
import android.support.v4.view.ViewCompat
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.kondenko.pocketwaka.R
import com.kondenko.pocketwaka.domain.stats.model.StatsItem
import com.kondenko.pocketwaka.ui.onelinesegmentedchart.OneLineSegmentedChart
import com.kondenko.pocketwaka.ui.onelinesegmentedchart.Segment
import java.util.*

class CardStats(val context: Context, val title: String, val data: List<StatsItem>) {

    val view = CardView(context)

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        val content = inflater.inflate(R.layout.view_card_stats_content, view, true)

        params.gravity = Gravity.CENTER_VERTICAL
        view.useCompatPadding = true
        view.setPadding(8, 8, 8, 8)
        view.layoutParams = params
        ViewCompat.setElevation(view, 2f)

        setupHeader(content)
        setupList(content)
        setupChart(content)
    }

    private fun setupHeader(content: View) {
        val header = content.findViewById<TextView>(R.id.statsCardHeader)
        header.text = title
    }

    private fun setupList(content: View) {
        val list = content.findViewById<RecyclerView>(R.id.statsCardRecyclerView)
        val adapter = CardStatsListAdapter(context, data)
        list.adapter = adapter
        list.layoutManager = object : LinearLayoutManager(context) {
            override fun canScrollVertically() = false
        }
    }

    private fun setupChart(content: View) {
        val chart = content.findViewById<OneLineSegmentedChart>(R.id.chart)
        val segments = ArrayList<Segment>(data.size)
        data.mapTo(segments) { Segment(it.percent!!.toFloat(), it.color, it.name) }
        chart.setSegments(segments)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as CardStats
        if (title != other.title) return false
        if (data != other.data) return false
        return true
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + data.hashCode()
        return result
    }


}