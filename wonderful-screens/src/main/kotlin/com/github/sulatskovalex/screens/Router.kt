package com.github.sulatskovalex.screens

import android.view.View
import android.view.ViewGroup
import org.koin.core.KoinContext
import org.koin.standalone.StandAloneContext

open class Router {
  internal val stack: MutableList<Screen<*, *, *>> = mutableListOf()
  private lateinit var activity: ScreensActivity
  private lateinit var container: ViewGroup

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
    val current = stack.lastOrNull()
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

  fun <A : Any> back(arg: A) {
    activity.onBackPressed(arg)
  }

  fun back() {
    back(Unit)
  }

  private fun <A : Any> replace(tag: String, argument: A, destroy: Boolean) {
    if (!stack.isEmpty()) {
      pause(stack.lastOrNull(), destroy)
    }
    val screen: Screen<*, *, A> = (StandAloneContext.koinContext as KoinContext).get(tag)
    stack.add(screen)
    screen.createView(container)
    (screen as? ChildScreen)?.setChildRouter(this)
    screen.setArg(argument)
    screen.create()
    resume(screen)
  }

  internal fun <A : Any> handleBack(arg: A): Boolean {
    if (!stack.isEmpty()) {
      val current = stack.lastOrNull()
      if (current is BackPressedHandler) {
        val handled = current.onBackPressed(arg)
        if (handled) {
          return true
        }
      }
      pause(current, true)
      val screen = stack.lastOrNull()
      (screen as? Screen<*, *, A>?)?.setArg(arg)
      resume(screen)
      return stack.isNotEmpty()
    }
    return false
  }

  private fun resume(screen: Screen<*, *, *>?) {
    screen?.apply {
      if (state == Screen.Created || state == Screen.Paused) {
        container.addView(view)
        if (state == Screen.Created) {
          onViewAdded(view)
        }
        activity.requestPermissionsResultHandler = this as? RequestPermissionsResultHandler
        activity.activityResultHandler = this as? ActivityResultHandler
        activity.configurationChangedHandler = this as? ConfigurationChangedHandler
        resume()
      }
    }
  }

  private fun pause(screen: Screen<*, *, *>?, destroy: Boolean) {
    screen?.apply {
      if (state == Screen.Resumed) {
        pause()
        view.removeFromParent()
      }
      if (destroy && (state == Screen.Paused || state == Screen.Created)) {
        destroy()
        stack.remove(this)
      }
    }
  }

  internal fun onResume() {
    resume(stack.lastOrNull())
  }

  internal fun onPause() {
    pause(stack.lastOrNull(), false)
  }

  fun attachToContainer(container: ViewGroup) {
    this.container = container
    this.activity = container.context as ScreensActivity
  }

  private fun View.removeFromParent() {
    (parent as? ViewGroup?)?.removeView(this)
  }

  internal fun onDestroy() {
    var index = stack.size - 1
    while (index >= 0) {
      pause(stack[index], true)
      index--
    }
  }
}
