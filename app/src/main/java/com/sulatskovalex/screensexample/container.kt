package com.sulatskovalex.screensexample

import android.view.View
import android.view.ViewGroup
import com.github.sulatskovalex.screens.*
import kotlinx.android.synthetic.main.screen_container.view.*
import kotlinx.android.synthetic.main.screen_container_first.view.*
import kotlinx.android.synthetic.main.screen_container_second.view.*

class CScreen(presenter: CPresenter,
              override val screenTag: String) : ContainerScreen<CScreen, CPresenter, Unit>(presenter) {
  override val firstScreenTag: String = CONTAINER1

  override fun createViewWithContainer(parent: ViewGroup): View =
      inflate(parent, R.layout.screen_container)

  override fun container(view: View): ViewGroup = view.container

}

class CPresenter(router: Router) : ContainerPresenter<CPresenter, CScreen, Unit>(router)

class C1Screen(presenter: C1Presenter,
               override val screenTag: String) : InnerScreen<C1Screen, C1Presenter, Unit>(presenter) {

  override fun createView(parent: ViewGroup): View {
    val view = inflate(parent, R.layout.screen_container_first)
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

class C2Screen(presenter: C2Presenter,
               override val screenTag: String) : InnerScreen<C2Screen, C2Presenter, Unit>(presenter) {
  override fun createView(parent: ViewGroup): View {
    val view = inflate(parent, R.layout.screen_container_second)
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
