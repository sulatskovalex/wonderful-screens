package com.sulatskovalex.screensexample

import android.app.Application
import android.view.ViewGroup
import com.github.sulatskovalex.screens.Router
import com.github.sulatskovalex.screens.ScreensActivity
import com.sulatskovalex.screensexample.start_main.MainScreen
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import org.koin.android.ext.android.startKoin


const val PAGE_SCREEN_THIRD = "PAGE_SCREEN_THIRD"

class MainActivity : ScreensActivity() {

  override val router: Router by inject()
  override val contentId: Int = R.layout.activity_main
  override val container: ViewGroup
    get() = mainActivityContainer
  override val firstScreenTag: String = MainScreen.Tag
}

class App : Application() {

  override fun onCreate() {
    super.onCreate()
    // start Koin!
    startKoin(this, listOf(mainModule))
  }
}
