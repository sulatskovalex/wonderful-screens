package com.sulatskovalex.screensexample.container.screens.first

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.sulatskovalex.screens.InnerScreen
import com.sulatskovalex.screensexample.R
import kotlinx.android.synthetic.main.screen_container_first.view.*

class ContainerFirstScreen(presenter: ContainerFirstPresenter, override val screenTag: String)
  : InnerScreen<ContainerFirstScreen, ContainerFirstPresenter, Unit>(presenter) {

  override fun createView(inflater: LayoutInflater, parent: ViewGroup): View {
    val view = inflater.inflate(R.layout.screen_container_first, parent, false)

    view.first_replace.setOnClickListener { presenter.onReplaceClick() }
    view.first_back.setOnClickListener { presenter.onBackClick() }

    return view
  }
}