package com.github.sulatskovalex.screens

import android.view.View
import android.view.ViewGroup

abstract class ContainerScreen<Self : ContainerScreen<Self, P, A>, P : ContainerPresenter<P, Self, A>, A : Any>(
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
    presenter.innerRouter = delegate
    return view
  }

  override fun resume() {
    super.resume()
    delegate.onResume()
  }

  override fun pause() {
    super.pause()
    delegate.onPause()
  }

  override fun destroy() {
    super.destroy()
    delegate.onDestroy()
  }

  abstract fun container(view: View): ViewGroup

  override fun onBackPressed(): Boolean = delegate.handleBack()
}

open class ContainerPresenter<Self : ContainerPresenter<Self, S, A>, S : ContainerScreen<S, Self, A>, A : Any>(
    router: Router)
  : Presenter<Self, S, A>(router) {
  lateinit var innerRouter: Router
}

abstract class InnerScreen<Self : InnerScreen<Self, P, A>, P : InnerPresenter<P, Self, A>, A : Any>(
    presenter: P)
  : Screen<Self, P, A>(presenter) {

  fun setInnerRouter(router: Router) {
    presenter.innerRouter = router
  }

}

open class InnerPresenter<Self : InnerPresenter<Self, S, A>, S : Screen<S, Self, A>, A : Any>(router: Router)
  : Presenter<Self, S, A>(router) {
  lateinit var innerRouter: Router
}

abstract class InnerContainerScreen<Self : InnerContainerScreen<Self, P, A>, P : InnerContainerPresenter<P, Self, A>, A : Any>(
    presenter: P)
  : ContainerScreen<Self, P, A>(presenter)

open class InnerContainerPresenter<Self : InnerContainerPresenter<Self, S, A>, S : InnerContainerScreen<S, Self, A>, A : Any>(
    router: Router)
  : ContainerPresenter<Self, S, A>(router)

