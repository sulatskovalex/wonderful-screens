package com.github.sulatskovalex.screens

import android.support.annotation.CallSuper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class ContainerScreen<
    S : ContainerScreen<S, P, A>,
    P : ContainerPresenter<P, S, A>,
    A : Any>(presenter: P)
  : Screen<S, P, A>(presenter), BackPressedHandler {
  abstract val firstScreenTag: String
  abstract val firstScreenArg: Any
  private val router = Router()

  abstract fun createViewWithContainer(inflater: LayoutInflater, parent: ViewGroup): View

  abstract fun container(createdView: View): ViewGroup

  final override fun createView(inflater: LayoutInflater, parent: ViewGroup): View {
    val view = createViewWithContainer(inflater, parent)
    router.attachToContainer(container(view))
    router.setRoot(firstScreenTag, firstScreenArg)
    presenter.childRouter = router
    return view
  }

  override fun setArg(arg: A) {
    if (arg::class.java == presenter.argumentClass) {
      super.setArg(arg)
    }
    (router.stack.lastOrNull() as? Screen<*, *, A>?)?.setArg(arg)
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


open class ContainerPresenter<P : ContainerPresenter<P, S, A>, S : ContainerScreen<S, P, A>, A : Any>(router: Router)
  : Presenter<P, S, A>(router) {
  lateinit var childRouter: Router
    internal set
}

open class ChildPresenter<P : ChildPresenter<P, S, A>, S : ChildScreen<S, P, A>, A : Any>(router: Router)
  : Presenter<P, S, A>(router) {
  lateinit var childRouter: Router
    internal set
}

abstract class ChildScreen<S : ChildScreen<S, P, A>, P : ChildPresenter<P, S, A>, A : Any>(presenter: P)
  : Screen<S, P, A>(presenter) {
  internal fun setChildRouter(childRouter: Router) {
    presenter.childRouter = childRouter
  }
}
