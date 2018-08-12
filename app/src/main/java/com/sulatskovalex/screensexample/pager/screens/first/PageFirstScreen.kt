package com.sulatskovalex.screensexample.pager.screens.first

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.sulatskovalex.screens.Screen
import com.sulatskovalex.screensexample.R

class PageFirstScreen(presenter: PageFirstPresenter, override val screenTag: String)
  : Screen<PageFirstScreen, PageFirstPresenter, Unit>(presenter) {

  override fun createView(inflater: LayoutInflater, parent: ViewGroup): View =
      inflater.inflate(R.layout.screen_page_first, parent, false)
}