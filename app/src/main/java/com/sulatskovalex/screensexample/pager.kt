package com.sulatskovalex.screensexample

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.sulatskovalex.screens.*
import kotlinx.android.synthetic.main.screen_pager.view.*

class PagScreen(presenter: PagPresenter, override val screenTag: String)
  : PagerScreen<PagScreen, PagPresenter, Unit>(presenter) {
  override val canScrollHorizontally: Boolean = true
  override val screenTags = arrayOf(PAGER1, PAGER2, PAGER3)
  override val firstScreenTag: String = PAGER2

  override fun createViewWithRecycler(inflater: LayoutInflater, parent: ViewGroup): View {
    val view = inflater.inflate(R.layout.screen_pager, parent, false)
    view.first_page.setOnClickListener { presenter.onFirstPageClick() }
    view.second_page.setOnClickListener { presenter.onSecondPageClick() }
    view.third_page.setOnClickListener { presenter.onThirdPageClick() }
    return view
  }
}

class PagPresenter(router: Router) : PagerPresenter<PagPresenter, PagScreen, Unit>(router) {

  fun onFirstPageClick() {
    pagerRouter.openTab(PAGER1)
  }

  fun onSecondPageClick() {
    pagerRouter.openTab(PAGER2)
  }

  fun onThirdPageClick() {
    pagerRouter.openTab(PAGER3)
  }
}

class P1Screen(presenter: P1Presenter, override val screenTag: String)
  : Screen<P1Screen, P1Presenter, Unit>(presenter) {

  override fun createView(inflater: LayoutInflater, parent: ViewGroup): View =
      inflater.inflate(R.layout.screen_page_first, parent, false)
}

class P1Presenter(router: Router) : Presenter<P1Presenter, P1Screen, Unit>(router)

class P2Screen(presenter: P2Presenter, override val screenTag: String)
  : Screen<P2Screen, P2Presenter, Unit>(presenter) {

  override fun createView(inflater: LayoutInflater, parent: ViewGroup): View =
      inflater.inflate(R.layout.screen_page_second, parent, false)
}

class P2Presenter(router: Router) : Presenter<P2Presenter, P2Screen, Unit>(router)

class P3Screen(presenter: P3Presenter, override val screenTag: String)
  : Screen<P3Screen, P3Presenter, Unit>(presenter) {
  override fun createView(inflater: LayoutInflater, parent: ViewGroup): View =
      inflater.inflate(R.layout.screen_page_third, parent, false)
}

class P3Presenter(router: Router) : Presenter<P3Presenter, P3Screen, Unit>(router)