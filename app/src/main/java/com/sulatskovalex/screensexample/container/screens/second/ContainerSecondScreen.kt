package com.sulatskovalex.screensexample.container.screens.second

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.sulatskovalex.screens.ChildScreen
import com.sulatskovalex.screensexample.R
import kotlinx.android.synthetic.main.screen_container_second.view.*

class ContainerSecondScreen(presenter: ContainerSecondPresenter, override val screenTag: String)
  : ChildScreen<ContainerSecondScreen, ContainerSecondPresenter, Unit>(presenter) {

  override fun createView(inflater: LayoutInflater, parent: ViewGroup): View {
    val view = inflater.inflate(R.layout.screen_container_second, parent, false)

    view.second_forward.setOnClickListener { presenter.onForwardClick() }
    view.second_replace.setOnClickListener { presenter.onReplaceClick() }
    view.second_back.setOnClickListener { presenter.onBackClick() }

    return view
  }
}