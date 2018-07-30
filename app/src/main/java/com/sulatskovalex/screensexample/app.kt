package com.sulatskovalex.screensexample

import android.app.Application
import android.view.ViewGroup
import com.github.sulatskovalex.screens.Router
import com.github.sulatskovalex.screens.Screen
import com.github.sulatskovalex.screens.ScreensActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import org.koin.android.ext.android.startKoin
import org.koin.dsl.module.applicationContext

const val MAIN = "MAIN"
const val CONTAINER = "CONTAINER"
const val CONTAINER1 = "CONTAINER1"
const val CONTAINER2 = "CONTAINER2"
const val PAGER = "PAGER"
const val PAGER1 = "Pager1"
const val PAGER2 = "Pager2"
const val PAGER3 = "PAGER3"

class MainActivity : ScreensActivity() {

  override val router: Router by inject()
  override val contentId: Int = R.layout.activity_main
  override val container: ViewGroup
    get() = mainActivityContainer
  override val firstScreenTag: String = MAIN
}

class App : Application() {

  override fun onCreate() {
    super.onCreate()
    startKoin(listOf(mainModule))
  }
}

val mainModule = applicationContext {
  bean { Router() }

  factory(MAIN) { MainScreen(get(), MAIN) as Screen<*, *, *> }
  factory { MainPresenter(get()) }

  factory(CONTAINER) { CScreen(get(), CONTAINER) as Screen<*, *, *> }
  factory { CPresenter(get()) }

  factory(CONTAINER1) { C1Screen(get(), CONTAINER1) as Screen<*, *, *> }
  factory { C1Presenter(get()) }

  factory(CONTAINER2) { C2Screen(get(), CONTAINER2) as Screen<*, *, *> }
  factory { C2Presenter(get()) }

  factory(PAGER) { PagScreen(get(), PAGER) as Screen<*, *, *> }
  factory { PagPresenter(get()) }

  factory(PAGER1) { P1Screen(get(), PAGER1) as Screen<*, *, *> }
  factory { P1Presenter(get()) }

  factory(PAGER2) { P2Screen(get(), PAGER2) as Screen<*, *, *> }
  factory { P2Presenter(get()) }

  factory(PAGER3) { P3Screen(get(), PAGER3) as Screen<*, *, *> }
  factory { P3Presenter(get()) }

}
