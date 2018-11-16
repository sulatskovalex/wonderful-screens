package com.sulatskovalex.screensexample.start_main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.sulatskovalex.screens.Screen
import com.sulatskovalex.screensexample.R
import kotlinx.android.synthetic.main.screen_main.view.*

class MainScreen(presenter: MainPresenter, override val screenTag: String= Tag)
  : Screen<MainScreen, MainPresenter>(presenter) {

  companion object {
    const val Tag = "MAIN_SCREEN"
  }

  override fun createView(inflater: LayoutInflater, parent: ViewGroup): View {
    val view: View = inflater.inflate(R.layout.screen_main, parent, false)

    view.forward.setOnClickListener { presenter.onForwardClick() }
    view.forwardContainer.setOnClickListener { presenter.onContainerClick() }
    view.forwardPager.setOnClickListener { presenter.onPagerClick() }
    view.setRoot.setOnClickListener { presenter.onRootClick() }

    return view
  }
}