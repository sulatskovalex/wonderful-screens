package com.github.sulatskovalex.screens

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper

abstract class ContainerScreen<
    S : ContainerScreen<S, P>,
    P : ContainerPresenter<P, S>>(presenter: P)
  : Screen<S, P>(presenter), BackPressedHandler {
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
  override fun onBackPressed(arg: Any): Boolean = router.handleBack(arg)
}


open class ContainerPresenter<P : ContainerPresenter<P, S>, S : ContainerScreen<S, P>>(router: Router)
  : Presenter<P, S>(router) {
  lateinit var childRouter: Router
    internal set
}

open class ChildPresenter<P : ChildPresenter<P, S>, S : ChildScreen<S, P>>(router: Router)
  : Presenter<P, S>(router) {
  lateinit var childRouter: Router
    internal set
}

abstract class ChildScreen<S : ChildScreen<S, P>, P : ChildPresenter<P, S>>(presenter: P)
  : Screen<S, P>(presenter) {
  internal fun setChildRouter(childRouter: Router) {
    presenter.childRouter = childRouter
  }
}
