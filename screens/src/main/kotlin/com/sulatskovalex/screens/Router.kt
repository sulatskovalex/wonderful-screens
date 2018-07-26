package com.sulatskovalex.screens

import android.view.View
import android.view.ViewGroup
import org.koin.KoinContext
import org.koin.standalone.StandAloneContext

class Router {
  private val stack: MutableList<Screen<*, *, *>> = mutableListOf()
  private lateinit var activity: ScreensActivity
  private lateinit var container: ViewGroup
  val current: Screen<*, *, *>?
    get() = if (stack.isNotEmpty()) {
      stack.last()
    } else null

  fun forward(tag: String) {
    forward(tag, Unit)
  }

  fun <A : Any> forward(tag: String, arg: A) {
    replace(tag, arg, false)
  }

  fun replace(tag: String) {
    replace(tag, Unit)
  }

  fun <A : Any> replace(tag: String, arg: A) {
    replace(tag, arg, true)
  }

  private fun <A : Any> replace(tag: String, argument: A, destroy: Boolean) {
    if (!stack.isEmpty()) {
      pause(current, destroy)
    }
    val fragment: Screen<*, *, A> = (StandAloneContext.koinContext as KoinContext).get(tag)
    stack.add(fragment)
    fragment.create(container)
    (fragment as? InnerScreen)?.setInnerRouter(this)
    activity.permissionsListener = fragment as? PermissionsListener
    fragment.setArg(argument)
    fragment.create()
    if (fragment.state == Screen.Created) {
      resume(fragment)
    }
  }

  fun <A : Any> setRoot(tag: String, arg: A) {
    stack.forEach {
      pause(it, true)
    }
    forward(tag, arg)
  }

  fun setRoot(tag: String) {
    setRoot(tag, Unit)
  }

  fun handleBack(): Boolean {
    if (!stack.isEmpty()) {
      val current = current
      if (current is BackPressedHandler) {
        val handled = current.onBackPressed()
        if (handled) {
          return true
        }
      }
      pause(current, true)
      resume(this.current)
      return !stack.isEmpty()
    }
    return false
  }

  private fun resume(fragment: Screen<*, *, *>?) {
    if (fragment == null) {
      return
    }
    if (fragment.state == Screen.Created || fragment.state == Screen.Paused) {
      fragment.view.removeFromParent()
      fragment.addTo(container)
      activity.permissionsListener = fragment.view as? PermissionsListener
      fragment.resume()
    }
  }

  private fun pause(screen: Screen<*, *, *>?, destroy: Boolean) {
    if (screen == null) {
      return
    }
    if (screen.state == Screen.Resumed) {
      screen.pause()
    }
    if (destroy && (screen.state == Screen.Paused || screen.state == Screen.Created)) {
      screen.destroy()
      screen.view.removeFromParent()
      stack.remove(screen)
    }
  }

  fun onResume() {
    resume(current)
  }

  fun onPause() {
    pause(current, false)
  }

  fun back() {
    activity.onBackPressed()
  }

  fun attachToContainer(container: ViewGroup) {
    this.container = container
    this.activity = container.context as ScreensActivity
  }

  private fun View.removeFromParent() {
    if (parent != null) {
      (parent as? ViewGroup)?.removeView(this)
    }
  }
}
