package com.github.sulatskovalex.screens

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.github.sulatskovalex.screens.Screen.Companion.Created
import com.github.sulatskovalex.screens.Screen.Companion.Paused
import com.github.sulatskovalex.screens.Screen.Companion.Resumed
import org.koin.standalone.StandAloneContext

abstract class PagerScreen<
    S : PagerScreen<S, P>,
    P : PagerPresenter<P, S>>(presenter: P)
  : Screen<S, P>(presenter), BackPressedHandler, PagerRouter {

  abstract val screenTags: Array<String>
  abstract val firstScreenTag: String
  abstract val firstScreenArg: Any
  private lateinit var adapter: ScreensAdapter
  private lateinit var viewPager: ViewPager

  final override fun createView(inflater: LayoutInflater, parent: ViewGroup): View {
    val view = createViewWithPager(inflater, parent)
    viewPager = pager(view)
    adapter = ScreensAdapter(activity, screenTags, presenter) {
      viewPager.currentItem
    }
    viewPager.adapter = adapter
    viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
      override fun onPageScrollStateChanged(state: Int) {}

      override fun onPageScrolled(position: Int, positionOffset: Float, offsetPixels: Int) {}

      override fun onPageSelected(position: Int) {
        adapter.onPageSelected(position)
      }

    })
    adapter.setArgTo(firstScreenTag, firstScreenArg)
    viewPager.currentItem = screenTags.indexOf(firstScreenTag)
    presenter.pagerRouter = this
    return view
  }

  override fun setArg(arg: Any) {
    super.setArg(arg)
    adapter.setArg(arg)
  }

  protected open fun createViewWithPager(
      inflater: LayoutInflater, parent: ViewGroup): View = ViewPager(activity)

  protected open fun pager(createdView: View): ViewPager = createdView as ViewPager

  @CallSuper
  override fun onBackPressed(arg: Any): Boolean {
    return adapter.handleBack(arg)
  }

  override fun pause() {
    super.pause()
    adapter.pause()
  }

  override fun resume() {
    super.resume()
    adapter.resume()
  }

  override fun destroy() {
    super.destroy()
    adapter.destroy()
  }

  override fun openTab(tag: String) {
    openTab(tag, Unit)
  }

  final override fun setArgTo(tag: String, arg: Any) {
    adapter.setArgTo(tag, arg)
  }

  @CallSuper
  final override fun openTab(tag: String, arg: Any) {
    val position = adapter.getIndexOf(tag)
    adapter.setArgTo(position, arg)
    viewPager.currentItem = position
  }
}

interface PagerRouter {
  fun openTab(tag: String, arg: Any)
  fun openTab(tag: String)
  fun setArgTo(tag: String, arg: Any)
}

open class PagerPresenter<P : PagerPresenter<P, S>, S : PagerScreen<S, P>>(router: Router)
  : Presenter<P, S>(router) {

  lateinit var pagerRouter: PagerRouter
    internal set

  open fun onScreenResumed(position: Int, screen: Screen<*, *>?) {

  }

  open fun onScreenPaused(position: Int, screen: Screen<*, *>?) {

  }
}

internal class ScreensAdapter(
    val activity: ScreensActivity,
    tags: Array<String>,
    private val presenter: PagerPresenter<*, *>,
    private val currentPositionProvider: () -> Int)
  : PagerAdapter() {

  override fun isViewFromObject(view: View, obj: Any): Boolean = view == obj

  private val screens: List<Screen<*, *>> =
      List(tags.size) { index ->
        StandAloneContext.getKoin().koinContext.get<Screen<*, *>>(tags[index])
      }

  var currentPosition: Int = 0
  var current: Screen<*, *>? = null

  fun handleBack(arg: Any): Boolean {
    val current = this.current
    return current != null && current is BackPressedHandler && current.onBackPressed(arg)
  }

  override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
    container.removeView(screens[position].view)
  }

  override fun instantiateItem(container: ViewGroup, position: Int): Any {
    val screen = screens[position]
    if (screen.state == Screen.Initialized) {
      screen.createView(container)
      screen.create()
      currentPosition = currentPositionProvider.invoke()
      current = screens[currentPosition]
      if (currentPosition == position && (screen.state == Screen.Created || screen.state == Screen.Paused)) {
        attachListeners(current)
        current?.resume()
      }
    }
    if (screen.view.parent == null) {
      container.addView(screen.view)
    }
    return screen.view
  }

  private fun attachListeners(current: Screen<*, *>?) {
    current?.apply {
      if (this is ActivityResultHandler) {
        activity.activityResultHandler = this
      }
      if (this is RequestPermissionsResultHandler) {
        activity.requestPermissionsResultHandler = this
      }
      if (this is ConfigurationChangedHandler) {
        activity.configurationChangedHandler = this
      }
    }
  }

  override fun getCount(): Int = screens.size

  fun pause() {
    val screen = current
    if (screen != null && screen.state == Resumed) {
      screen.pause()
    }
  }

  fun resume() {
    val screen = current
    if (screen != null && (screen.state == Created || screen.state == Paused)) {
      attachListeners(current)
      screen.resume()
    }
  }

  fun destroy() {
    val screen = current
    screens.asReversed().filter { it != screen && (it.state == Created || it.state == Paused) }.forEach { it.destroy() }
    screen?.destroy()
  }

  fun  setArgTo(screenIndex: Int, arg: Any) {
    screens[screenIndex].setArg(arg)
  }

  fun getIndexOf(tag: String): Int {
    val index = screens.indexOfFirst { it.screenTag == tag }
    return if (index >= 0) index else throw Throwable("screen with tag $tag is not exist in pager")
  }

  fun  setArg(arg: Any) {
    current?.setArg(arg)
  }

  fun  setArgTo(tag: String, arg: Any) {
    screens[getIndexOf(tag)].setArg(arg)
  }

  fun onPageSelected(position: Int) {
    current?.pause()
    presenter.onScreenPaused(currentPosition, current)
    current = screens[position]
    attachListeners(current)
    current?.resume()
    presenter.onScreenResumed(position, current)
    currentPosition = position
  }
}
