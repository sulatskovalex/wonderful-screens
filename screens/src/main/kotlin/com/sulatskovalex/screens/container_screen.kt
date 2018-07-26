package com.sulatskovalex.screens

import android.view.View
import android.view.ViewGroup

abstract class ContainerScreen<Self : ContainerScreen<Self, P, A>, P : Presenter<P, Self, A>, A : Any>(
    presenter: P)
  : Screen<Self, P, A>(presenter), BackPressedHandler {
  abstract val firstScreenTag: String
  private val delegate = Router()

  abstract fun createViewWithContainer(parent: ViewGroup): View


  override fun createView(parent: ViewGroup): View {
    val view = createViewWithContainer(parent)
    val container = container(view)
    delegate.attachToContainer(container)
    delegate.setRoot(firstScreenTag)
    return view
  }

  abstract fun container(view: View): ViewGroup

  override fun onBackPressed(): Boolean = delegate.handleBack()
}

abstract class InnerScreen<Self : InnerScreen<Self, P, A>, P : InnerPresenter<P, Self, A>, A : Any>(
    presenter: P)
  : Screen<Self, P, A>(presenter) {
  fun setInnerRouter(router: Router) {
    presenter.innerRouter = router
  }
}

open class InnerPresenter<Self : InnerPresenter<Self, S, A>, S : InnerScreen<S, Self, A>, A : Any>(
    router: Router)
  : Presenter<Self, S, A>(router) {
  lateinit var innerRouter: Router
}
