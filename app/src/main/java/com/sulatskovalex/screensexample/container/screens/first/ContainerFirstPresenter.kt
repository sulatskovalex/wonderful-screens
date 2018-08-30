package com.sulatskovalex.screensexample.container.screens.first

import com.github.sulatskovalex.screens.ChildPresenter
import com.github.sulatskovalex.screens.Router
import com.sulatskovalex.screensexample.CONTAINER_SCREEN_SECOND

class ContainerFirstPresenter(router: Router)
  : ChildPresenter<ContainerFirstPresenter, ContainerFirstScreen, Unit>(router) {

  fun onBackClick() {
    rootRouter.back()
  }

  fun onReplaceClick() {
    chlidRouter.replace(CONTAINER_SCREEN_SECOND)
  }
}