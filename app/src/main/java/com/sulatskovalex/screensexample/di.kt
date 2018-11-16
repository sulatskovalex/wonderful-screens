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
import org.koin.dsl.module.module

val mainModule = module {
  single { Router() }

  screen(MainScreen.Tag) { MainScreen(get()) }
  presenter { MainPresenter(get()) }

  /**
   * Container
   */
  screen(MainContainerScreen.Tag) { MainContainerScreen(get()) }
  presenter { MainContainerPresenter(get()) }

  screen(ContainerFirstScreen.Tag) { ContainerFirstScreen(get()) }
  presenter { ContainerFirstPresenter(get()) }

  screen(ContainerSecondScreen.Tag) { ContainerSecondScreen(get()) }
  presenter { ContainerSecondPresenter(get()) }

  /**
   * ViewPager
   */
  screen(MainPagerScreen.Tag) { MainPagerScreen(get()) }
  presenter { MainPagerPresenter(get()) }

  screen(PageFirstScreen.Tag) { PageFirstScreen(get()) }
  presenter { PageFirstPresenter(get()) }

  screen(PageSecondScreen.Tag) { PageSecondScreen(get()) }
  presenter { PageSecondPresenter(get()) }

  screen(PageThirdScreen.Tag) { PageThirdScreen(get()) }
  presenter { PageThirdPresenter(get()) }

}
