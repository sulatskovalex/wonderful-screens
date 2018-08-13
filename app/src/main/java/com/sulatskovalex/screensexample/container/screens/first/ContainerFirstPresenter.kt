package com.sulatskovalex.screensexample.container.screens.first

import com.github.sulatskovalex.screens.InnerPresenter
import com.github.sulatskovalex.screens.Router
import com.sulatskovalex.screensexample.CONTAINER_SCREEN_SECOND

class ContainerFirstPresenter(router: Router) : InnerPresenter<ContainerFirstPresenter, ContainerFirstScreen, Unit>(
    router) {

  fun onBackClick() {
    router.back()
  }

  fun onReplaceClick() {
    innerRouter.replace(CONTAINER_SCREEN_SECOND)
  }
}