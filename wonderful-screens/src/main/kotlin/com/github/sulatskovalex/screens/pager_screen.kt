package com.github.sulatskovalex.screens

import android.support.annotation.CallSuper
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.sulatskovalex.screens.Screen.Companion.Created
import com.github.sulatskovalex.screens.Screen.Companion.Paused
import com.github.sulatskovalex.screens.Screen.Companion.Resumed
import org.koin.core.KoinContext
import org.koin.standalone.StandAloneContext

abstract class PagerScreen<
    S : PagerScreen<S, P, A>,
    P : PagerPresenter<P, S, A>,
    A : Any>(presenter: P)
  : Screen<S, P, A>(presenter), BackPressedHandler, PagerRouter {

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

  override fun setArg(arg: A) {
    if (arg::class.java == presenter.argumentClass) {
      super.setArg(arg)
    }
    adapter.setArg(arg)
  }

  protected open fun createViewWithPager(
          inflater: LayoutInflater, parent: ViewGroup): View = ViewPager(activity)

  protected open fun pager(createdView: View): ViewPager = createdView as ViewPager

  @CallSuper
  override fun <A : Any> onBackPressed(arg: A): Boolean {
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

  override fun <A : Any> setArgTo(tag: String, arg: A) {
    adapter.setArgTo(tag, arg)
  }

  @CallSuper
  override fun <A : Any> openTab(tag: String, arg: A) {
    val position = adapter.getIndexOf(tag)
    adapter.setArgTo(position, arg)
    viewPager.currentItem = position
  }
}

interface PagerRouter {
  fun <A : Any> openTab(tag: String, arg: A)
  fun openTab(tag: String)
  fun <A : Any> setArgTo(tag: String, arg: A)
}

open class PagerPresenter<P : PagerPresenter<P, S, A>, S : PagerScreen<S, P, A>, A : Any>(router: Router)
  : Presenter<P, S, A>(router) {

  lateinit var pagerRouter: PagerRouter
    internal set

  open fun onScreenResumed(position: Int, screen: Screen<*, *, *>?) {

  }

  open fun onScreenPaused(position: Int, screen: Screen<*, *, *>?) {

  }

}

internal class ScreensAdapter(
        val activity: ScreensActivity,
    tags: Array<String>,
    private val presenter: PagerPresenter<*, *, *>,
    private val currentPositionProvider: () -> Int)
  : PagerAdapter() {

  override fun isViewFromObject(view: View, obj: Any): Boolean = view == obj

  private val screens: List<Screen<*, *, *>> =
      List(tags.size) { index ->
        val screen: Screen<*, *, *> = (StandAloneContext.koinContext as KoinContext).get(tags[index])
        screen
      }

  var currentPosition: Int = 0
  var current: Screen<*, *, *>? = null

  fun <A : Any> handleBack(arg: A): Boolean {
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
    container.addView(screen.view, position)
    return screen.view
  }

  private fun attachListeners(current: Screen<*, *, *>?) {
    activity.activityResultHandler = current as? ActivityResultHandler
    activity.requestPermissionsResultHandler = current as? RequestPermissionsResultHandler
    activity.configurationChangedHandler = current as? ConfigurationChangedHandler
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

  fun <A : Any> setArgTo(screenIndex: Int, arg: A) {
    (screens[screenIndex] as? Screen<*, *, A>)?.setArg(arg)
  }

  fun getIndexOf(tag: String): Int {
    val index = screens.indexOfFirst { it.screenTag == tag }
    return if (index >= 0) index else throw Throwable("screen with tag $tag is not exist in pager")
  }

  fun <A : Any> setArg(arg: A) {
    (current as? Screen<*, *, A>?)?.setArg(arg)
  }

  fun <A : Any> setArgTo(tag: String, arg: A) {
    (screens[getIndexOf(tag)] as? Screen<*, *, A>)?.setArg(arg)
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
