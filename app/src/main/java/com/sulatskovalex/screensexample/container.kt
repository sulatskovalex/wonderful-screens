package com.sulatskovalex.screensexample

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.sulatskovalex.screens.*
import kotlinx.android.synthetic.main.screen_container.view.*
import kotlinx.android.synthetic.main.screen_container_first.view.*
import kotlinx.android.synthetic.main.screen_container_second.view.*

class CScreen(presenter: CPresenter, override val screenTag: String)
  : ContainerScreen<CScreen, CPresenter, Unit>(presenter) {
  override val firstScreenTag: String = CONTAINER1

  override fun createViewWithContainer(inflater: LayoutInflater, parent: ViewGroup): View =
      inflater.inflate(R.layout.screen_container, parent, false)

  override fun container(createdView: View): ViewGroup = createdView.container

}

class CPresenter(router: Router) : ContainerPresenter<CPresenter, CScreen, Unit>(router)

class C1Screen(presenter: C1Presenter, override val screenTag: String)
  : InnerScreen<C1Screen, C1Presenter, Unit>(presenter) {

  override fun createView(inflater: LayoutInflater, parent: ViewGroup): View {
    val view = inflater.inflate(R.layout.screen_container_first, parent, false)
    view.first_replace.setOnClickListener { presenter.onReplaceClick() }
    view.first_back.setOnClickListener { presenter.onBackClick() }
    return view
  }

}

class C1Presenter(router: Router) : InnerPresenter<C1Presenter, C1Screen, Unit>(router) {

  fun onBackClick() {
    router.back()
  }

  fun onReplaceClick() {
    innerRouter.replace(CONTAINER2)
  }
}

class C2Screen(presenter: C2Presenter, override val screenTag: String)
  : InnerScreen<C2Screen, C2Presenter, Unit>(presenter) {

  override fun createView(inflater: LayoutInflater, parent: ViewGroup): View {
    val view = inflater.inflate(R.layout.screen_container_second, parent, false)
    view.second_forward.setOnClickListener { presenter.onForwardClick() }
    view.second_replace.setOnClickListener { presenter.onReplaceClick() }
    view.second_back.setOnClickListener { presenter.onBackClick() }
    return view
  }
}

class C2Presenter(router: Router) : InnerPresenter<C2Presenter, C2Screen, Unit>(router) {

  fun onBackClick() {
    router.back()
  }

  fun onReplaceClick() {
    innerRouter.replace(CONTAINER1)
  }

  fun onForwardClick() {
    innerRouter.forward(CONTAINER1)
  }
}
