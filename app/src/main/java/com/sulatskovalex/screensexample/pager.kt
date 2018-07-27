package com.sulatskovalex.screensexample

import android.view.View
import android.view.ViewGroup
import com.github.sulatskovalex.screens.*

class PagScreen(presenter: PagPresenter) : PagerScreen<PagScreen, PagPresenter, Unit>(presenter) {
  override val screenTags = arrayOf(PAGER1, PAGER2, PAGER3)
  override val firstTag: String = PAGER2
}

class PagPresenter(router: Router) : Presenter<PagPresenter, PagScreen, Unit>(router)

class P1Screen(presenter: P1Presenter) : Screen<P1Screen, P1Presenter, Unit>(presenter) {
  override fun createView(parent: ViewGroup): View = inflate(parent, R.layout.screen_page_first)

}

class P1Presenter(router: Router) : Presenter<P1Presenter, P1Screen, Unit>(router)

class P2Screen(presenter: P2Presenter) : Screen<P2Screen, P2Presenter, Unit>(presenter) {
  override fun createView(parent: ViewGroup): View = inflate(parent, R.layout.screen_page_second)

}

class P2Presenter(router: Router) : Presenter<P2Presenter, P2Screen, Unit>(router)

class P3Screen(presenter: P3Presenter) : Screen<P3Screen, P3Presenter, Unit>(presenter) {
  override fun createView(parent: ViewGroup): View = inflate(parent, R.layout.screen_page_third)

}

class P3Presenter(router: Router) : Presenter<P3Presenter, P3Screen, Unit>(router)