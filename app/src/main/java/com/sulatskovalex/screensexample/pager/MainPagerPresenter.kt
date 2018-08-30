package com.sulatskovalex.screensexample.pager

import com.github.sulatskovalex.screens.PagerPresenter
import com.github.sulatskovalex.screens.Router
import com.sulatskovalex.screensexample.PAGE_SCREEN_FIRST
import com.sulatskovalex.screensexample.PAGE_SCREEN_SECOND
import com.sulatskovalex.screensexample.PAGE_SCREEN_THIRD

class MainPagerPresenter(router: Router)
  : PagerPresenter<MainPagerPresenter, MainPagerScreen, Unit>(router) {

  fun onFirstPageClick() {
    pagerRouter.openTab(PAGE_SCREEN_FIRST)
  }

  fun onSecondPageClick() {
    pagerRouter.openTab(PAGE_SCREEN_SECOND)
  }

  fun onThirdPageClick() {
    pagerRouter.openTab(PAGE_SCREEN_THIRD)
  }
}