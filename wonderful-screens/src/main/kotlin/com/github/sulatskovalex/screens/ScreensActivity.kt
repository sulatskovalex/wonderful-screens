package com.github.sulatskovalex.screens

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup

abstract class ScreensActivity : AppCompatActivity() {

  protected abstract val router: Router
  abstract val contentId: Int
  abstract val container: ViewGroup
  abstract val firstScreenTag: String
  protected open val firstScreenArg: Any = Unit
  internal var requestPermissionsResultHandler: RequestPermissionsResultHandler? = null
  internal var activityResultHandler: ActivityResultHandler? = null
  internal var configurationChangedHandler: ConfigurationChangedHandler? = null

  @CallSuper
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(contentId)
    router.attachToContainer(container)
    router.setRoot(firstScreenTag, firstScreenArg)
  }

  @CallSuper
  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    activityResultHandler?.onActivityResult(requestCode, resultCode, data)
  }

  @CallSuper
  override fun onResume() {
    router.onResume()
    super.onResume()
  }

  @CallSuper
  override fun onPause() {
    router.onPause()
    super.onPause()
  }

  fun <A : Any> onBackPressed(arg: A) {
    if (!router.handleBack(arg)) {
      super.onBackPressed()
    }
  }

  @CallSuper
  override fun onBackPressed() {
    onBackPressed(Unit)
  }

  @CallSuper
  override fun onRequestPermissionsResult(
      requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
    requestPermissionsResultHandler?.onRequestPermissionsResult(requestCode, permissions, grantResults)
  }

  @CallSuper
  override fun onConfigurationChanged(newConfig: Configuration?) {
    configurationChangedHandler?.onConfigurationChanged(newConfig)
    super.onConfigurationChanged(newConfig)
  }
}

interface RequestPermissionsResultHandler {
  fun onRequestPermissionsResult(reqCode: Int, permissions: Array<String>, result: IntArray)
}

interface BackPressedHandler {
  fun <A : Any> onBackPressed(arg: A): Boolean
}

interface ActivityResultHandler {
  fun onActivityResult(reqCode: Int, resCode: Int, data: Intent?)
}

interface ConfigurationChangedHandler {
  fun onConfigurationChanged(newConfig: Configuration?)
}

inline fun org.koin.dsl.context.Context.screen(tag: String, crossinline sc: () -> Screen<*, *, *>) = factory(tag) { sc.invoke() }

inline fun <reified T : Presenter<*, *, *>> org.koin.dsl.context.Context.presenter(crossinline presenter: () -> T) = factory { presenter.invoke() }
