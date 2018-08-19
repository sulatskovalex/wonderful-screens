package com.github.sulatskovalex.screens

import android.content.Context
import android.support.annotation.CallSuper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import kotlinx.coroutines.experimental.Job

abstract class Screen<Self : Screen<Self, P, A>, P : Presenter<P, Self, A>, A : Any>(protected val presenter: P) {

  init {
    presenter.view = this as Self
  }

  abstract val screenTag: String
  lateinit var view: android.view.View
    private set
  protected lateinit var activity: ScreensActivity
  protected lateinit var inflater: LayoutInflater
    private set
  internal var state = Initialized
    private set

  internal fun create(parent: ViewGroup) {
    this.activity = parent.context as ScreensActivity
    this.inflater = LayoutInflater.from(parent.context)
    this.view = createView(inflater, parent)
    this.view.isFocusable = true
    this.view.isClickable = true
  }

  abstract fun createView(inflater: LayoutInflater, parent: ViewGroup): android.view.View

  fun hideKeyBoard() {
    (activity.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.hideSoftInputFromWindow(
        (activity.currentFocus ?: view).windowToken, 0)
  }

  internal fun attachTo(container: ViewGroup, firstly: Boolean) {
    container.addView(view)
    if (firstly) {
      onViewAdded(view)
    }
  }

  protected open fun onViewAdded(view: View) {

  }

  internal open fun setArg(arg: A) {
    if (arg == null) return
    if (arg::class.java == presenter.argumentClass) {
      presenter.argument = arg
    }
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

open class Presenter<Self : Presenter<Self, S, A>, S : Screen<S, Self, A>, A : Any>(val rootRouter: Router) {
  protected val jobs = mutableListOf<Job>()
  lateinit var view: S
    internal set
  lateinit var argument: A
    internal set
  open var argumentClass: Class<A> = Any::class.java as Class<A>

  open fun onResume() {}
  open fun onCreate() {}
  open fun onPause() {}
  @CallSuper
  open fun onDestroy() {
    jobs.forEach { it.cancel() }
  }
}
