package com.sulatskovalex.screensexample

import android.app.Application
import android.view.ViewGroup
import com.github.sulatskovalex.screens.Router
import com.github.sulatskovalex.screens.ScreensActivity
import com.github.sulatskovalex.screens.presenter
import com.github.sulatskovalex.screens.screen
import com.sulatskovalex.screensexample.container.MainContainerPresenter
import com.sulatskovalex.screensexample.container.MainContainerScreen
import com.sulatskovalex.screensexample.container.screens.first.ContainerFirstPresenter
import com.sulatskovalex.screensexample.container.screens.first.ContainerFirstScreen
import com.sulatskovalex.screensexample.container.screens.second.ContainerSecondPresenter
import com.sulatskovalex.screensexample.container.screens.second.ContainerSecondScreen
import com.sulatskovalex.screensexample.pager.MainPagerPresenter
import com.sulatskovalex.screensexample.pager.MainPagerScreen
import com.sulatskovalex.screensexample.pager.screens.first.PageFirstPresenter
import com.sulatskovalex.screensexample.pager.screens.first.PageFirstScreen
import com.sulatskovalex.screensexample.pager.screens.second.PageSecondPresenter
import com.sulatskovalex.screensexample.pager.screens.second.PageSecondScreen
import com.sulatskovalex.screensexample.pager.screens.third.PageThirdPresenter
import com.sulatskovalex.screensexample.pager.screens.third.PageThirdScreen
import com.sulatskovalex.screensexample.start_main.MainPresenter
import com.sulatskovalex.screensexample.start_main.MainScreen
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import org.koin.android.ext.android.startKoin
import org.koin.dsl.module.applicationContext

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

// just declare it
val mainModule = applicationContext {
  bean { Router() }

  screen(MAIN_SCREEN) { MainScreen(get(), MAIN_SCREEN) }
  presenter { MainPresenter(get()) }

  /**
   * Container
   */
  screen(CONTAINER_SCREEN_MAIN) { MainContainerScreen(get(), CONTAINER_SCREEN_MAIN) }
  presenter { MainContainerPresenter(get()) }

  screen(CONTAINER_SCREEN_FIRST) { ContainerFirstScreen(get(), CONTAINER_SCREEN_FIRST) }
  presenter { ContainerFirstPresenter(get()) }

  screen(CONTAINER_SCREEN_SECOND) { ContainerSecondScreen(get(), CONTAINER_SCREEN_SECOND) }
  presenter { ContainerSecondPresenter(get()) }

  /**
   * ViewPager
   */
  screen(PAGER_SCREEN_MAIN) { MainPagerScreen(get(), PAGER_SCREEN_MAIN) }
  presenter { MainPagerPresenter(get()) }

  screen(PAGE_SCREEN_FIRST) { PageFirstScreen(get(), PAGE_SCREEN_FIRST) }
  presenter { PageFirstPresenter(get()) }

  screen(PAGE_SCREEN_SECOND) { PageSecondScreen(get(), PAGE_SCREEN_SECOND) }
  presenter { PageSecondPresenter(get()) }

  screen(PAGE_SCREEN_THIRD) { PageThirdScreen(get(), PAGE_SCREEN_THIRD) }
  presenter { PageThirdPresenter(get()) }

}
