package com.sulatskovalex.screensexample.container

import com.github.sulatskovalex.screens.ContainerPresenter
import com.github.sulatskovalex.screens.Router
import com.sulatskovalex.screensexample.container.screens.first.ContainerFirstScreen

class MainContainerPresenter(router: Router)
  : ContainerPresenter<MainContainerPresenter, MainContainerScreen>(router) {
  override fun onCreate() {
    super.onCreate()
    childRouter.setArgsTo(ContainerFirstScreen.Tag, Any())
  }
}
