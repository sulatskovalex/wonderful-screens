package com.sulatskovalex.screensexample

import com.github.sulatskovalex.screens.Router
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
import org.koin.dsl.module.applicationContext

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
