package com.sulatskovalex.screensexample.pager

import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.sulatskovalex.screens.PagerScreen
import com.sulatskovalex.screensexample.R
import com.sulatskovalex.screensexample.pager.screens.first.PageFirstScreen
import com.sulatskovalex.screensexample.pager.screens.second.PageSecondScreen
import com.sulatskovalex.screensexample.pager.screens.third.PageThirdScreen
import kotlinx.android.synthetic.main.screen_pager.view.*

class MainPagerScreen(
        presenter: MainPagerPresenter,
        override val screenTag: String = Tag)
  : PagerScreen<MainPagerScreen, MainPagerPresenter>(presenter) {
  companion object {
    const val Tag = "PAGER_SCREEN_MAIN"
  }
  override val firstScreenArg = Any()
  override val screenTags = arrayOf(PageFirstScreen.Tag, PageSecondScreen.Tag, PageThirdScreen.Tag)
  override val firstScreenTag: String = PageFirstScreen.Tag

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