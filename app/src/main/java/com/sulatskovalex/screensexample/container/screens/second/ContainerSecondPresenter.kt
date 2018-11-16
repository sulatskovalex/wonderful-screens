package com.sulatskovalex.screensexample.container.screens.second

import com.github.sulatskovalex.screens.ChildPresenter
import com.github.sulatskovalex.screens.Router
import com.github.sulatskovalex.screens.hideKeyboard
import com.sulatskovalex.screensexample.container.screens.first.ContainerFirstScreen
import com.sulatskovalex.screensexample.container.screens.first.FirstContainerArg

class SecondContainerArg(val arg: String)

class ContainerSecondPresenter(router: Router)
  : ChildPresenter<ContainerSecondPresenter, ContainerSecondScreen>(router) {

  override fun onCreate() {
    super.onCreate()
    (argument as? SecondContainerArg)?.apply {
      screen.showArg("argument is: $arg")
    }
  }

  fun onBackClick() {
    screen.hideKeyboard()
    rootRouter.back()
  }

  fun onBackWithArgClick(str: String) {
    screen.hideKeyboard()
    rootRouter.back(FirstContainerArg(str))
  }

  fun onReplaceClick(str: String) {
    screen.hideKeyboard()
    childRouter.replace(ContainerFirstScreen.Tag, FirstContainerArg(str))
  }

  fun onForwardClick(str: String) {
    screen.hideKeyboard()
    childRouter.forward(ContainerFirstScreen.Tag, FirstContainerArg(str))
  }
}