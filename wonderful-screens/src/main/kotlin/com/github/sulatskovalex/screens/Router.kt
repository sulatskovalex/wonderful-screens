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

  fun backTo(tag: String) {
    backTo(tag, Unit)
  }

  fun <A : Any> backTo(tag: String, arg: A) {
    var index = stack.size - 1
    val current = this.current
    while (index >= 0) {
      val screen = stack[index]
      if (screen.screenTag == tag) {
        if (screen != current) {
          (screen as? Screen<*, *, A>)?.setArg(arg)
          resume(screen)
          break
        }
      } else {
        pause(screen, true)
      }
      index--
    }
  }

  fun <A: Any> back(arg: A) {
    activity.onBackPressed(arg)
  }

  fun back() {
    back(Unit)
  }

  private fun <A : Any> replace(tag: String, argument: A, destroy: Boolean) {
    if (!stack.isEmpty()) {
      pause(current, destroy)
    }
    val screen: Screen<*, *, A> = (StandAloneContext.koinContext as KoinContext).get(tag)
    stack.add(screen)
    screen.create(container)
    (screen as? InnerScreen)?.setInnerRouter(this)
    activity.permissionsListener = screen as? PermissionsListener
    activity.activityResultListener = screen as? OnActivityResultListener
    screen.setArg(argument)
    screen.create()
    resume(screen)
  }

  fun handleBack(): Boolean {
    return handleBack(Unit)
  }

  fun<A: Any> handleBack(arg: A): Boolean {
    if (!stack.isEmpty()) {
      val current = current
      if (current is BackPressedHandler) {
        val handled = current.onBackPressed(arg)
        if (handled) {
          return true
        }
      }
      pause(current, true)
      (this.current as? Screen<*, *, A>?)?.setArg(arg)
      resume(this.current)
      return stack.isNotEmpty()
    }
    return false
  }

  private fun resume(screen: Screen<*, *, *>?) {
    if (screen == null) {
      return
    }
    if (screen.state == Screen.Created || screen.state == Screen.Paused) {
      screen.view.removeFromParent()
      screen.attachTo(container)
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

  internal fun onResume() {
    resume(current)
  }

  internal fun onPause() {
    pause(current, false)
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

  internal fun onDestroy() {
    var index = stack.size - 1
    while (index >= 0) {
      pause(stack[index], true)
      index--
    }
  }
}
