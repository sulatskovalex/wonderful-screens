package com.sulatskovalex.screensexample.container

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.sulatskovalex.screens.ContainerScreen
import com.sulatskovalex.screensexample.CONTAINER_SCREEN_FIRST
import com.sulatskovalex.screensexample.R
import kotlinx.android.synthetic.main.screen_container.view.*

class MainContainerScreen(presenter: MainContainerPresenter, override val screenTag: String)
  : ContainerScreen<MainContainerScreen, MainContainerPresenter, Unit>(presenter) {
  override val firstScreenArg = Any()
  override val firstScreenTag: String = CONTAINER_SCREEN_FIRST

  override fun createViewWithContainer(inflater: LayoutInflater, parent: ViewGroup): View =
      inflater.inflate(R.layout.screen_container, parent, false)

  override fun container(createdView: View): ViewGroup = createdView.container

}