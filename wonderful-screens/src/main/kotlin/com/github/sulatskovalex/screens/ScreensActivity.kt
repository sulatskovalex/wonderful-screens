package com.github.sulatskovalex.screens

import android.content.Intent
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup

abstract class ScreensActivity : AppCompatActivity() {

  protected abstract val router: Router
  abstract val contentId: Int
  abstract val container: ViewGroup
  abstract val firstScreenTag: String
  var permissionsListener: PermissionsListener? = null
  var activityResultListener: OnActivityResultListener? = null

  @CallSuper
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(contentId)
    router.attachToContainer(container)
    router.setRoot(firstScreenTag)
  }

  @CallSuper
  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    activityResultListener?.onActivityResult(requestCode, resultCode, data)
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
    permissionsListener?.onRequestPermissionsResult(requestCode, permissions, grantResults)
  }
}

interface PermissionsListener {
  fun onRequestPermissionsResult(reqCode: Int, permissions: Array<String>, result: IntArray)
}

interface BackPressedHandler {
  fun <A : Any> onBackPressed(arg: A): Boolean
}

interface OnActivityResultListener {
  fun onActivityResult(reqCode: Int, resCode: Int, data: Intent?)
}
