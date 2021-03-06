package com.kondenko.pocketwaka.screens.stats


import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kondenko.pocketwaka.Const
import com.kondenko.pocketwaka.R
import com.ogaclejapan.smarttablayout.utils.v4.Bundler
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_stats_container.*
import timber.log.Timber

class FragmentStats : Fragment() {

    private val refreshEvents = PublishSubject.create<Any>()

    private var areTabsElevated = false

    private var scrollSubscription: Disposable? = null

    private var refreshSubscription: Disposable? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
            = inflater.inflate(R.layout.fragment_stats_container, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = FragmentPagerItemAdapter(
                childFragmentManager,
                FragmentPagerItems.with(activity)
                        .addFragment(R.string.stats_tab_7_days, Const.STATS_RANGE_7_DAYS)
                        .addFragment(R.string.stats_tab_30_days, Const.STATS_RANGE_30_DAYS)
                        .addFragment(R.string.stats_tab_6_months, Const.STATS_RANGE_6_MONTHS)
                        .addFragment(R.string.stats_tab_1_year, Const.STATS_RANGE_1_YEAR)
                        .create()
        )
        stats_viewpager_content.adapter = adapter
        stats_smarttablayout_ranges.setViewPager(stats_viewpager_content)
        stats_viewpager_content.post {
            onFragmentSelected(adapter.getPage(stats_viewpager_content.currentItem) as FragmentStatsTab)
        }
        stats_smarttablayout_ranges.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                Timber.i("Selected page $position")
                val selectedFragment = adapter.getPage(position) as FragmentStatsTab?
                if (selectedFragment == null) Timber.d("$selectedFragment at position $position is null")
                selectedFragment?.let { fragment -> onFragmentSelected(fragment) }
            }
        })
    }

    private fun onFragmentSelected(fragment: FragmentStatsTab) {
        if (fragment.isScrollviewOnTop() && areTabsElevated) animateTabs(elevated = false)
        else if (!fragment.isScrollviewOnTop() && !areTabsElevated) animateTabs(elevated = true)
        refreshSubscription?.dispose()
        scrollSubscription?.dispose()
        refreshSubscription = fragment.subscribeToRefreshEvents(refreshEvents)
        scrollSubscription = fragment.scrollDirection().subscribe { scrollDirection ->
            animateTabs(elevated = scrollDirection === ScrollingDirection.Down)
        }
    }

    private fun animateTabs(elevated: Boolean) {
        areTabsElevated = elevated
        // Tabs background color
        val colorGray = ContextCompat.getColor(context!!, R.color.color_background_gray)
        val colorPrimaryLight = ContextCompat.getColor(context!!, android.R.color.white)
        val colorAnim = ValueAnimator()
        stats_smarttablayout_ranges.background
        with(colorAnim) {
            @Suppress("UsePropertyAccessSyntax")
            setDuration(Const.DEFAULT_ANIM_DURATION)
            setIntValues(if (elevated) colorGray else colorPrimaryLight, if (elevated) colorPrimaryLight else colorGray)
            setEvaluator(ArgbEvaluator())
            addUpdateListener { valueAnimator ->
                stats_smarttablayout_ranges.setBackgroundColor(valueAnimator.animatedValue as Int)
            }
            start()
        }
        // Custom tabs elevation
        stats_view_shadow.animate().alpha(if (elevated) Const.MAX_SHADOW_OPACITY else 0f).start()
    }

    fun subscribeToRefreshEvents(refreshEvents: Observable<Any>) {
        refreshEvents.subscribeWith(this.refreshEvents)
    }

    private fun FragmentPagerItems.Creator.addFragment(@StringRes title: Int, range: String) = this.add(title, FragmentStatsTab::class.java, Bundler().putString(FragmentStatsTab.ARG_RANGE, range).get())

}
