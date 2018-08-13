package com.sulatskovalex.screensexample.pager

import com.github.sulatskovalex.screens.PagerPresenter
import com.github.sulatskovalex.screens.Router
import com.sulatskovalex.screensexample.PAGE_SCREEN_FIRST
import com.sulatskovalex.screensexample.PAGE_SCREEN_SECOND
import com.sulatskovalex.screensexample.PAGE_SCREEN_THIRD

class MainPagerPresenter(router: Router) :
    PagerPresenter<MainPagerPresenter, MainPagerScreen, Unit>(router) {

  fun onFirstPageClick() {
    openTab(PAGE_SCREEN_FIRST)
  }

  fun onSecondPageClick() {
    openTab(PAGE_SCREEN_SECOND)
  }

  fun onThirdPageClick() {
    openTab(PAGE_SCREEN_THIRD)
  }
}