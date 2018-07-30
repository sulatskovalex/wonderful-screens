package com.sulatskovalex.screensexample

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.sulatskovalex.screens.Presenter
import com.github.sulatskovalex.screens.Router
import com.github.sulatskovalex.screens.Screen
import kotlinx.android.synthetic.main.screen_main.view.*

class MainScreen(presenter: MainPresenter, override val screenTag: String)
  : Screen<MainScreen, MainPresenter, Unit>(presenter) {

  override fun createView(inflater: LayoutInflater, parent: ViewGroup): View {
    val view: View = inflater.inflate(R.layout.screen_main, parent, false)
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
