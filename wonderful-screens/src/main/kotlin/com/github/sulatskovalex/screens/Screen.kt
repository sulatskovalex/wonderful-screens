package com.github.sulatskovalex.screens

import android.content.Context
import android.support.annotation.CallSuper
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import kotlinx.coroutines.experimental.Job

abstract class Screen<Self : Screen<Self, P, A>, P : Presenter<P, Self, A>, A : Any>(protected val presenter: P) {

  init {
    presenter.view = this as Self
  }

  abstract val screenTag: String
  lateinit var view: android.view.View
  lateinit var activity: ScreensActivity
  lateinit var inflater: LayoutInflater
  var state = Initialized
    private set

  fun create(parent: ViewGroup) {
    this.activity = parent.context as ScreensActivity
    this.inflater = LayoutInflater.from(parent.context)
    this.view = createView(inflater, parent)
  }

  abstract fun createView(inflater: LayoutInflater, parent: ViewGroup): android.view.View

  fun hideKeyBoard() {
    (activity.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.hideSoftInputFromWindow(
        (activity.currentFocus ?: view).windowToken, 0)
  }

  open fun attachTo(container: ViewGroup) {
    container.addView(view)
  }

  open fun setArg(arg: A) {
    presenter.argument = arg
  }

  @CallSuper
  open fun create() {
    state = Created
    presenter.onCreate()
  }

  @CallSuper
  open fun resume() {
    state = Resumed
    presenter.onResume()
  }

  @CallSuper
  open fun pause() {
    state = Paused
    presenter.onPause()
  }

  @CallSuper
  open fun destroy() {
    state = Destroyed
    presenter.onDestroy()
  }

  companion object {
    const val Initialized = 0
    const val Created = 1
    const val Resumed = 2
    const val Paused = 3
    const val Destroyed = 4
  }
}

open class Presenter<Self : Presenter<Self, S, A>, S : Screen<S, Self, A>, A : Any>(val router: Router) {
  protected val jobs = mutableListOf<Job>()
  lateinit var view: S
  lateinit var argument: A
  open fun onResume() {}
  open fun onCreate() {}
  open fun onPause() {}
  @CallSuper
  open fun onDestroy() {
    jobs.forEach { it.cancel() }
  }
}
