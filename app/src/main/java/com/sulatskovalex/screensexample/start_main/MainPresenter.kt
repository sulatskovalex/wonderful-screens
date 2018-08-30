package com.sulatskovalex.screensexample.start_main

import com.github.sulatskovalex.screens.Presenter
import com.github.sulatskovalex.screens.Router
import com.sulatskovalex.screensexample.CONTAINER_SCREEN_MAIN
import com.sulatskovalex.screensexample.MAIN_SCREEN
import com.sulatskovalex.screensexample.PAGER_SCREEN_MAIN

class MainPresenter(router: Router) : Presenter<MainPresenter, MainScreen, Unit>(router) {

  fun onContainerClick() {
    rootRouter.forward(CONTAINER_SCREEN_MAIN)
  }

  fun onForwardClick() {
    rootRouter.forward(MAIN_SCREEN)
  }

  fun onPagerClick() {
    rootRouter.forward(PAGER_SCREEN_MAIN)
  }

  fun onRootClick() {
    rootRouter.setRoot(PAGER_SCREEN_MAIN)
  }
}