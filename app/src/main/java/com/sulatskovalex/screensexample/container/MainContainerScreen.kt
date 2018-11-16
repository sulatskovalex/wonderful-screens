package com.sulatskovalex.screensexample.container

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.sulatskovalex.screens.ContainerScreen
import com.sulatskovalex.screensexample.R
import com.sulatskovalex.screensexample.container.screens.first.ContainerFirstScreen
import kotlinx.android.synthetic.main.screen_container.view.*

class MainContainerScreen(
        presenter: MainContainerPresenter,
        override val screenTag: String = Tag)
  : ContainerScreen<MainContainerScreen, MainContainerPresenter>(presenter) {

  companion object {
    const val Tag = "CONTAINER_SCREEN_MAIN"
  }

  override val firstScreenArg = Any()
  override val firstScreenTag: String = ContainerFirstScreen.Tag

  override fun createViewWithContainer(inflater: LayoutInflater, parent: ViewGroup): View =
      inflater.inflate(R.layout.screen_container, parent, false)

  override fun container(createdView: View): ViewGroup = createdView.container

}