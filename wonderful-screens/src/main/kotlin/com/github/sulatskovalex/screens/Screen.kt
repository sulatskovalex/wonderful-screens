package com.github.sulatskovalex.screens

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class Screen<S : Screen<S, P>, P : Presenter<P, S>>(protected val presenter: P) {

  init {
    presenter.screen = this as S
  }

  abstract val screenTag: String
  lateinit var view: android.view.View
    private set
  lateinit var activity: ScreensActivity
    internal set
  protected lateinit var inflater: LayoutInflater
    private set
  internal var state = Initialized
    private set

  internal fun createView(parent: ViewGroup) {
    this.activity = parent.context as ScreensActivity
    this.inflater = LayoutInflater.from(parent.context)
    this.view = createView(inflater, parent)
    this.view.isFocusable = true
    this.view.isClickable = true
    parent.addView(view)
    onViewAdded(view)
  }

  abstract fun createView(inflater: LayoutInflater, parent: ViewGroup): android.view.View

  open fun onViewAdded(view: View) {

  }

  internal open fun setArg(arg: Any) {
    presenter.argument = arg
  }

  internal open fun create() {
    state = Created
    onCreate()
    presenter.onCreate()
  }

  protected open fun onCreate() { }

  internal open fun resume() {
    state = Resumed
    onResume()
    presenter.onResume()
  }

  protected open fun onResume() { }

  internal open fun pause() {
    state = Paused
    onPause()
    presenter.onPause()
  }

  protected open fun onPause() { }

  internal open fun destroy() {
    state = Destroyed
    onDestroy()
    presenter.onDestroy()
  }

  protected open fun onDestroy() {}

  internal companion object {
    internal const val Initialized = 0
    internal const val Created = 1
    internal const val Resumed = 2
    internal const val Paused = 3
    internal const val Destroyed = 4
  }
}

open class Presenter<P : Presenter<P, S>, S : Screen<S, P>>(protected val rootRouter: Router): CoroutineScope {

  private val job = Job()
  override val coroutineContext: CoroutineContext = job + Dispatchers.Main

  lateinit var screen: S
    internal set
  var argument: Any = Unit
    internal set(value) {
      val prev = field
      field = value
      onArgumentChanged(prev, field)
    }
  protected open fun onArgumentChanged(prevArg: Any, newArg: Any) { }
  open fun onResume() {}
  open fun onCreate() {}
  open fun onPause() {}
  @CallSuper
  open fun onDestroy() {
    job.cancel()
  }
}
