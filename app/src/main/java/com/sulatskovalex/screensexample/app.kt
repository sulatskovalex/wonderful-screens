package com.sulatskovalex.screensexample

import android.app.Application
import android.view.ViewGroup
import com.github.sulatskovalex.screens.Router
import com.github.sulatskovalex.screens.ScreensActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import org.koin.android.ext.android.startKoin

// main
const val MAIN_SCREEN = "MAIN_SCREEN"

// container
const val CONTAINER_SCREEN_MAIN = "CONTAINER_SCREEN_MAIN"
const val CONTAINER_SCREEN_FIRST = "CONTAINER_SCREEN_FIRST"
const val CONTAINER_SCREEN_SECOND = "CONTAINER_SCREEN_SECOND"

// viewpager
const val PAGER_SCREEN_MAIN = "PAGER_SCREEN_MAIN"
const val PAGE_SCREEN_FIRST = "PAGE_SCREEN_FIRST"
const val PAGE_SCREEN_SECOND = "PAGE_SCREEN_SECOND"
const val PAGE_SCREEN_THIRD = "PAGE_SCREEN_THIRD"

class MainActivity : ScreensActivity() {

  override val router: Router by inject()
  override val contentId: Int = R.layout.activity_main
  override val container: ViewGroup
    get() = mainActivityContainer
  override val firstScreenTag: String = MAIN_SCREEN
}

class App : Application() {

  override fun onCreate() {
    super.onCreate()
    // start Koin!
    startKoin(listOf(mainModule))
  }
}
