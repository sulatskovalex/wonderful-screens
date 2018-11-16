package com.github.sulatskovalex.screens

import android.view.ViewGroup
import org.koin.standalone.StandAloneContext

open class Router {
  private val stack: MutableList<Screen<*, *>> = mutableListOf()
  private lateinit var activity: ScreensActivity
  private lateinit var container: ViewGroup

  fun forward(tag: String) {
    forward(tag, Unit)
  }

  fun forward(tag: String, arg: Any) {
    replace(tag, arg, false)
  }

  fun replace(tag: String) {
    replace(tag, Unit)
  }

  fun replace(tag: String, arg: Any) {
    replace(tag, arg, true)
  }

  fun setRoot(tag: String, arg: Any) {
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

  fun backTo(tag: String, arg: Any) {
    var index = stack.size - 1
    val current = stack.lastOrNull()
    while (index >= 0) {
      val screen = stack[index]
      if (screen.screenTag == tag) {
        if (screen != current) {
          screen.setArg(arg)
          resume(screen)
          break
        }
      } else {
        pause(screen, true)
      }
      index--
    }
  }

  fun back(arg: Any) {
    activity.onBackPressed(arg)
  }

  fun back() {
    back(Unit)
  }

  private fun replace(tag: String, argument: Any, destroy: Boolean) {
    if (!stack.isEmpty()) {
      pause(stack.lastOrNull(), destroy)
    }
    val screen: Screen<*, *> = StandAloneContext.getKoin().koinContext.get(tag)
    stack.add(screen)
    screen.createView(container)
    (screen as? ChildScreen)?.setChildRouter(this)
    screen.setArg(argument)
    screen.create()
    resume(screen)
  }

  internal fun handleBack(arg: Any): Boolean {
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
      screen?.setArg(arg)
      resume(screen)
      return stack.isNotEmpty()
    }
    return false
  }

  private fun resume(screen: Screen<*, *>?) {
    screen?.apply {
      if (state == Screen.Created || state == Screen.Paused) {
        activity.requestPermissionsResultHandler = this as? RequestPermissionsResultHandler
        activity.activityResultHandler = this as? ActivityResultHandler
        activity.configurationChangedHandler = this as? ConfigurationChangedHandler
        resume()
      }
    }
  }

  private fun pause(screen: Screen<*, *>?, destroy: Boolean) {
    screen?.apply {
      if (state == Screen.Resumed) {
        pause()
      }
      if (destroy && (state == Screen.Paused || state == Screen.Created)) {
        destroy()
        stack.remove(this)
        if (stack.isNotEmpty()) {
          (view.parent as? ViewGroup?)?.removeView(view)
        }
      }
    }
  }

  fun setArgsTo(screenTag: String, arg: Any) {
    stack.firstOrNull { it.screenTag == screenTag }?.setArg(arg)
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

  internal fun onDestroy() {
    var index = stack.size - 1
    while (index >= 0) {
      pause(stack[index], true)
      index--
    }
  }
}
