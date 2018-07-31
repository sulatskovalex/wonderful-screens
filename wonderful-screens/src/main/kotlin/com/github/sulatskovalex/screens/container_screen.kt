package com.github.sulatskovalex.screens

import android.support.annotation.CallSuper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class ContainerScreen<Self : ContainerScreen<Self, P, A>, P : ContainerPresenter<P, Self, A>, A : Any>(
    presenter: P)
  : Screen<Self, P, A>(presenter), BackPressedHandler {
  abstract val firstScreenTag: String
  private val router = Router()

  abstract fun createViewWithContainer(inflater: LayoutInflater, parent: ViewGroup): View

  abstract fun container(createdView: View): ViewGroup

  final override fun createView(inflater: LayoutInflater, parent: ViewGroup): View {
    val view = createViewWithContainer(inflater, parent)
    router.attachToContainer(container(view))
    router.setRoot(firstScreenTag)
    presenter.innerRouter = router
    return view
  }

  override fun setArg(arg: A) {
    if(arg::class.java == presenter.argumentClass) {
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
  override fun<A: Any> onBackPressed(arg: A): Boolean = router.handleBack(arg)
}

open class ContainerPresenter<Self : ContainerPresenter<Self, S, A>, S : ContainerScreen<S, Self, A>, A : Any>(router: Router)
  : Presenter<Self, S, A>(router) {
  lateinit var innerRouter: Router
  internal set
}

abstract class InnerScreen<Self : InnerScreen<Self, P, A>, P : InnerPresenter<P, Self, A>, A : Any>(presenter: P)
  : Screen<Self, P, A>(presenter) {

  fun setInnerRouter(router: Router) {
    presenter.innerRouter = router
  }
}

open class InnerPresenter<Self : InnerPresenter<Self, S, A>, S : Screen<S, Self, A>, A : Any>(router: Router)
  : Presenter<Self, S, A>(router) {
  lateinit var innerRouter: Router
    internal set
}
