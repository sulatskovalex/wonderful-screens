package com.sulatskovalex.screensexample.pager.screens.second

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.sulatskovalex.screens.Screen
import com.sulatskovalex.screensexample.R

class PageSecondScreen(presenter: PageSecondPresenter, override val screenTag: String)
  : Screen<PageSecondScreen, PageSecondPresenter, Unit>(presenter) {

  override fun createView(inflater: LayoutInflater, parent: ViewGroup): View =
      inflater.inflate(R.layout.screen_page_second, parent, false)
}