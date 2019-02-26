package com.github.sulatskovalex.screens

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import org.koin.dsl.context.ModuleDefinition

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

  internal fun  onBackPressed(arg: Any) {
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
  override fun onConfigurationChanged(newConfig: Configuration) {
    configurationChangedHandler?.onConfigurationChanged(newConfig)
    super.onConfigurationChanged(newConfig)
  }
}

interface RequestPermissionsResultHandler {
  fun onRequestPermissionsResult(reqCode: Int, permissions: Array<String>, result: IntArray)
}

interface BackPressedHandler {
  fun  onBackPressed(arg: Any): Boolean
}

interface ActivityResultHandler {
  fun onActivityResult(reqCode: Int, resCode: Int, data: Intent?)
}

interface ConfigurationChangedHandler {
  fun onConfigurationChanged(newConfig: Configuration?)
}

inline fun ModuleDefinition.screen(tag: String, crossinline sc: () -> Screen<*, *>) = factory(tag) { sc.invoke() }

inline fun <reified T : Presenter<*, *>> ModuleDefinition.presenter(crossinline presenter: () -> T) = factory { presenter.invoke() }

fun Screen<*,*>.hideKeyboard() {
  (activity.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.hideSoftInputFromWindow(
      (activity.currentFocus ?: view).windowToken, 0)
}
