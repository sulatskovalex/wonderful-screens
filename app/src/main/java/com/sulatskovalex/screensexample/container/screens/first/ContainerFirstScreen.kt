package com.sulatskovalex.screensexample.container.screens.first

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.sulatskovalex.screens.ChildScreen
import com.sulatskovalex.screensexample.R
import kotlinx.android.synthetic.main.screen_container_first.view.*

class ContainerFirstScreen(
        presenter: ContainerFirstPresenter,
        override val screenTag: String = Tag)
  : ChildScreen<ContainerFirstScreen, ContainerFirstPresenter>(presenter) {

  companion object {
    const val Tag = "CONTAINER_SCREEN_FIRST"
  }

  override fun createView(inflater: LayoutInflater, parent: ViewGroup): View {
    val view = inflater.inflate(R.layout.screen_container_first, parent, false)

    view.first_replace.setOnClickListener { presenter.onReplaceClick(view.first_edit_argument.text.toString()) }
    view.first_forward.setOnClickListener { presenter.onForwardClick(view.first_edit_argument.text.toString()) }
    view.first_screen_toolbar.setNavigationOnClickListener { presenter.onBackClick() }
    return view
  }

  fun showArg(arg: String) {
    view.first_argument.text = arg
  }
}