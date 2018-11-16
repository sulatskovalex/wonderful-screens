package com.sulatskovalex.screensexample.start_main

import com.github.sulatskovalex.screens.Presenter
import com.github.sulatskovalex.screens.Router
import com.sulatskovalex.screensexample.container.MainContainerScreen
import com.sulatskovalex.screensexample.pager.MainPagerScreen

class MainPresenter(router: Router) : Presenter<MainPresenter, MainScreen>(router) {

  fun onContainerClick() {
    rootRouter.forward(MainContainerScreen.Tag)
  }

  fun onForwardClick() {
    rootRouter.forward(MainScreen.Tag)
  }

  fun onPagerClick() {
    rootRouter.forward(MainPagerScreen.Tag)
  }

  fun onRootClick() {
    rootRouter.setRoot(MainPagerScreen.Tag)
  }
}