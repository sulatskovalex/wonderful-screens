package com.sulatskovalex.screensexample

import android.app.Application
import android.view.ViewGroup
import com.github.sulatskovalex.screens.Router
import com.github.sulatskovalex.screens.ScreensActivity
import com.github.sulatskovalex.screens.presenter
import com.github.sulatskovalex.screens.screen
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

  screen(MAIN) { MainScreen(get(), MAIN) }
  presenter { MainPresenter(get()) }

  screen(CONTAINER) { CScreen(get(), CONTAINER) }
  presenter { CPresenter(get()) }

  screen(CONTAINER1) { C1Screen(get(), CONTAINER1) }
  presenter { C1Presenter(get()) }

  screen(CONTAINER2) { C2Screen(get(), CONTAINER2) }
  presenter { C2Presenter(get()) }

  screen(PAGER) { PagScreen(get(), PAGER) }
  presenter { PagPresenter(get()) }

  screen(PAGER1) { P1Screen(get(), PAGER1) }
  presenter { P1Presenter(get()) }

  screen(PAGER2) { P2Screen(get(), PAGER2) }
  presenter { P2Presenter(get()) }

  screen(PAGER3) { P3Screen(get(), PAGER3) }
  presenter { P3Presenter(get()) }

}
