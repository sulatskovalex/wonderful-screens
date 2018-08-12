package com.sulatskovalex.screensexample.container.screens.second

import com.github.sulatskovalex.screens.InnerPresenter
import com.github.sulatskovalex.screens.Router
import com.sulatskovalex.screensexample.CONTAINER_SCREEN_FIRST

class ContainerSecondPresenter(router: Router) : InnerPresenter<ContainerSecondPresenter, ContainerSecondScreen, Unit>(
    router) {

  fun onBackClick() {
    router.back()
  }

  fun onReplaceClick() {
    innerRouter.replace(CONTAINER_SCREEN_FIRST)
  }

  fun onForwardClick() {
    innerRouter.forward(CONTAINER_SCREEN_FIRST)
  }
}