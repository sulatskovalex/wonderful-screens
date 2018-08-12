package com.sulatskovalex.screensexample.start_main

import com.github.sulatskovalex.screens.Presenter
import com.github.sulatskovalex.screens.Router
import com.sulatskovalex.screensexample.CONTAINER_SCREEN_MAIN
import com.sulatskovalex.screensexample.MAIN_SCREEN
import com.sulatskovalex.screensexample.PAGER_SCREEN_MAIN

class MainPresenter(router: Router) : Presenter<MainPresenter, MainScreen, Unit>(router) {

  fun onContainerClick() {
    router.forward(CONTAINER_SCREEN_MAIN)
  }

  fun onForwardClick() {
    router.forward(MAIN_SCREEN)
  }

  fun onPagerClick() {
    router.forward(PAGER_SCREEN_MAIN)
  }

  fun onRootClick() {
    router.setRoot(PAGER_SCREEN_MAIN)
  }
}