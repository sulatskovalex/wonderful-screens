package com.sulatskovalex.screensexample

import android.view.View
import android.view.ViewGroup
import com.sulatskovalex.screens.Presenter
import com.sulatskovalex.screens.Router
import com.sulatskovalex.screens.Screen
import com.sulatskovalex.screens.inflate
import kotlinx.android.synthetic.main.screen_main.view.*

class MainScreen(presenter: MainPresenter) : Screen<MainScreen, MainPresenter, Unit>(presenter) {
  override fun createView(parent: ViewGroup): View {
    val view: View = inflate(parent, R.layout.screen_main)
    view.forward.setOnClickListener { presenter.onForwardClick() }
    view.forwardContainer.setOnClickListener { presenter.onContainerClick() }
    view.forwardPager.setOnClickListener { presenter.onPagerClick() }
    view.setRoot.setOnClickListener { presenter.onRootClick() }
    return view
  }
}

class MainPresenter(router: Router) : Presenter<MainPresenter, MainScreen, Unit>(router) {
  fun onContainerClick() {
    router.forward(CONTAINER)
  }

  fun onForwardClick() {
    router.forward(MAIN)
  }

  fun onPagerClick() {
    router.forward(PAGER)
  }

  fun onRootClick() {
    router.setRoot(PAGER)
  }
}
