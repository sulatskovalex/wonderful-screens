package com.sulatskovalex.screensexample.pager

import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.sulatskovalex.screens.PagerScreen
import com.sulatskovalex.screensexample.PAGE_SCREEN_FIRST
import com.sulatskovalex.screensexample.PAGE_SCREEN_SECOND
import com.sulatskovalex.screensexample.PAGE_SCREEN_THIRD
import com.sulatskovalex.screensexample.R
import kotlinx.android.synthetic.main.screen_pager.view.*

class MainPagerScreen(presenter: MainPagerPresenter, override val screenTag: String)
  : PagerScreen<MainPagerScreen, MainPagerPresenter, Unit>(presenter) {
  override val firstScreenArg = Any()
  override val screenTags = arrayOf(PAGE_SCREEN_FIRST, PAGE_SCREEN_SECOND, PAGE_SCREEN_THIRD)
  override val firstScreenTag: String = PAGE_SCREEN_FIRST

  override fun createViewWithPager(inflater: LayoutInflater, parent: ViewGroup): View {
    val view = inflater.inflate(R.layout.screen_pager, parent, false)

    view.first_page.setOnClickListener { presenter.onFirstPageClick() }
    view.second_page.setOnClickListener { presenter.onSecondPageClick() }
    view.third_page.setOnClickListener { presenter.onThirdPageClick() }

    return view
  }

  override fun pager(createdView: View): ViewPager {
    return createdView.findViewById(R.id.pager_list)
  }
}