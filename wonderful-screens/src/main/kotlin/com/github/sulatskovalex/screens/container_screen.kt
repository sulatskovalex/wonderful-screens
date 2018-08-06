package com.github.sulatskovalex.screens

import android.support.annotation.CallSuper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class ContainerScreen<S : ContainerScreen<S, P, A>, P : Presenter<P, S, A>, A : Any>(presenter: P)
  : Screen<S, P, A>(presenter), BackPressedHandler {
  abstract val firstScreenTag: String
  private val router = Router()

  abstract fun createViewWithContainer(inflater: LayoutInflater, parent: ViewGroup): View

  abstract fun container(createdView: View): ViewGroup

  final override fun createView(inflater: LayoutInflater, parent: ViewGroup): View {
    val view = createViewWithContainer(inflater, parent)
    router.attachToContainer(container(view))
    router.setRoot(firstScreenTag)
    return view
  }

  override fun setArg(arg: A) {
    if (arg::class.java == presenter.argumentClass) {
      super.setArg(arg)
    }
    (router.current as? Screen<*, *, A>?)?.setArg(arg)
  }

  @CallSuper
  override fun resume() {
    super.resume()
    router.onResume()
  }

  @CallSuper
  override fun pause() {
    super.pause()
    router.onPause()
  }

  @CallSuper
  override fun destroy() {
    super.destroy()
    router.onDestroy()
  }

  @CallSuper
  override fun <A : Any> onBackPressed(arg: A): Boolean = router.handleBack(arg)
}


abstract class InnerContainerScreen<S : InnerContainerScreen<S, P, A>, P : InnerPresenter<P, S, A>, A : Any>(presenter: P)
  : ContainerScreen<S, P, A>(presenter) {

  internal fun setInnerRouter(innerRouter: Router) {
    presenter.innerRouter = innerRouter
  }
}

abstract class InnerScreen<S : InnerScreen<S, P, A>, P : InnerPresenter<P, S, A>, A : Any>(presenter: P)
  : Screen<S, P, A>(presenter) {

  internal fun setInnerRouter(innerRouter: Router) {
    presenter.innerRouter = innerRouter
  }
}

open class InnerPresenter<P : InnerPresenter<P, S, A>, S : Screen<S, P, A>, A : Any>(router: Router)
  : Presenter<P, S, A>(router) {
  lateinit var innerRouter: Router
    internal set
}
