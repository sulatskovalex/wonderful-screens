package com.sulatskovalex.screensexample.pager

import com.github.sulatskovalex.screens.PagerPresenter
import com.github.sulatskovalex.screens.Router
import com.sulatskovalex.screensexample.pager.screens.first.PageFirstScreen
import com.sulatskovalex.screensexample.pager.screens.second.PageSecondScreen
import com.sulatskovalex.screensexample.pager.screens.third.PageThirdScreen

class MainPagerPresenter(router: Router)
  : PagerPresenter<MainPagerPresenter, MainPagerScreen>(router) {

  fun onFirstPageClick() {
    pagerRouter.openTab(PageFirstScreen.Tag)
  }

  fun onSecondPageClick() {
    pagerRouter.openTab(PageSecondScreen.Tag)
  }

  fun onThirdPageClick() {
    pagerRouter.openTab(PageThirdScreen.Tag)
  }
}