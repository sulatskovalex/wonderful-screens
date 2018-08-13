package com.sulatskovalex.screensexample.pager.screens.third

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.sulatskovalex.screens.Screen
import com.sulatskovalex.screensexample.R

class PageThirdScreen(presenter: PageThirdPresenter, override val screenTag: String)
  : Screen<PageThirdScreen, PageThirdPresenter, Unit>(presenter) {

  override fun createView(inflater: LayoutInflater, parent: ViewGroup): View =
      inflater.inflate(R.layout.screen_page_third, parent, false)
}