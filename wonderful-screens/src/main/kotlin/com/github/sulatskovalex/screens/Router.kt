package com.github.sulatskovalex.screens

import android.view.View
import android.view.ViewGroup
import org.koin.KoinContext
import org.koin.standalone.StandAloneContext

open class Router {
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
    val screen: Screen<*, *, A> = (StandAloneContext.koinContext as KoinContext).get(tag)
    stack.add(screen)
    screen.create(container)
    (screen as? InnerScreen)?.setInnerRouter(this)
    activity.setPermissionsListener(screen as? PermissionsListener)
    activity.setActivityResultListener(screen as? OnActivityResultListener)
    screen.setArg(argument)
    screen.create()
    if (screen.state == Screen.Created) {
      resume(screen)
    }
  }

  fun <A : Any> setRoot(tag: String, arg: A) {
    var index = stack.size - 1
    while (index >= 0) {
      pause(stack[index], true)
      index--
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

  private fun resume(screen: Screen<*, *, *>?) {
    if (screen == null) {
      return
    }
    if (screen.state == Screen.Created || screen.state == Screen.Paused) {
      screen.view.removeFromParent()
      screen.addTo(container)
      screen.resume()
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

  fun back(tag: String) {
    var index = stack.size - 1
    val current = this.current
    while (index >= 0) {
      val screen = stack[index]
      if (screen.screenTag == tag) {
        if (screen != current) {
          resume(screen)
          break
        }
      } else {
        pause(screen, true)
      }
      index --
    }
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

  fun onDestroy() {
    var index = stack.size - 1
    while (index >= 0) {
      pause(stack[index], true)
      index--
    }
  }
}
