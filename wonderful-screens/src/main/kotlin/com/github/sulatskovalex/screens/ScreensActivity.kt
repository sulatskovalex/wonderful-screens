package com.github.sulatskovalex.screens

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup

abstract class ScreensActivity : AppCompatActivity() {
  protected abstract val router: Router
  var permissionsListener: PermissionsListener? = null
  var activityResultListener: OnActivityResultListener? = null

  abstract val contentId: Int
  abstract val container: ViewGroup
  abstract val firstScreenTag: String

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(contentId)
    router.attachToContainer(container)
    router.setRoot(firstScreenTag)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    activityResultListener?.onActivityResult(requestCode, resultCode, data)
  }

  override fun onResume() {
    router.onResume()
    super.onResume()
  }

  override fun onPause() {
    router.onPause()
    super.onPause()
  }

  override fun onBackPressed() {
    if (!router.handleBack()) {
      super.onBackPressed()
    }
  }

  override fun onRequestPermissionsResult(
      requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
    permissionsListener?.onRequestPermissionsResult(requestCode, permissions, grantResults)
  }
}

interface PermissionsListener {
  fun onRequestPermissionsResult(reqCode: Int, permissions: Array<String>, result: IntArray)
}

interface BackPressedHandler {
  fun onBackPressed(): Boolean
}

interface OnActivityResultListener {
  fun onActivityResult(reqCode: Int, resCode: Int, data: Intent?)
}
