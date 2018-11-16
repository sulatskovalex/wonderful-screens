package com.sulatskovalex.screensexample.container.screens.first

import com.github.sulatskovalex.screens.ChildPresenter
import com.github.sulatskovalex.screens.Router
import com.github.sulatskovalex.screens.hideKeyboard
import com.sulatskovalex.screensexample.container.screens.second.ContainerSecondScreen
import com.sulatskovalex.screensexample.container.screens.second.SecondContainerArg

class FirstContainerArg(val arg: String)

class ContainerFirstPresenter(router: Router)
  : ChildPresenter<ContainerFirstPresenter, ContainerFirstScreen>(router) {

  override fun onCreate() {
    super.onCreate()
    (argument as? FirstContainerArg)?.apply {
      if (arg.isNotEmpty()) {
        screen.showArg("argument is: $arg")
      }
    }
  }

  override fun onArgumentChanged(prevArg: Any, newArg: Any) {
    (newArg as? FirstContainerArg)?.apply {
      if (arg.isNotEmpty()) {
        screen.showArg("back argument is: $arg")
      }
    }
  }

  fun onReplaceClick(str: String) {
    screen.hideKeyboard()
    childRouter.replace(ContainerSecondScreen.Tag, SecondContainerArg(str))
  }

  fun onForwardClick(str: String) {
    screen.hideKeyboard()
    childRouter.forward(ContainerSecondScreen.Tag, SecondContainerArg(str))
  }

  fun onBackClick() {
    rootRouter.back()
  }
}