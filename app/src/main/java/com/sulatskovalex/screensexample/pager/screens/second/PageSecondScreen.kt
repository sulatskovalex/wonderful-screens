package com.sulatskovalex.screensexample.pager.screens.second

import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.sulatskovalex.screens.Screen
import com.sulatskovalex.screensexample.R
import kotlinx.android.synthetic.main.screen_page_second.view.*

class PageSecondScreen(
        presenter: PageSecondPresenter,
        override val screenTag: String = Tag)
  : Screen<PageSecondScreen, PageSecondPresenter>(presenter) {

  companion object {
    const val Tag = "PAGE_SCREEN_SECOND"
  }

  override fun createView(inflater: LayoutInflater, parent: ViewGroup): View {
    val view = inflater.inflate(R.layout.screen_page_second, parent, false)
    view.second_page_list.layoutManager = LinearLayoutManager(activity)
    view.second_page_list.adapter = SecondPageAdapter()
    return view
  }
}