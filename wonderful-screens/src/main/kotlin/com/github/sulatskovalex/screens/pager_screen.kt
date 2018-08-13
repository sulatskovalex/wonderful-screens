package com.github.sulatskovalex.screens

import android.support.annotation.CallSuper
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.sulatskovalex.screens.Screen.Companion.Created
import com.github.sulatskovalex.screens.Screen.Companion.Initialized
import com.github.sulatskovalex.screens.Screen.Companion.Paused
import com.github.sulatskovalex.screens.Screen.Companion.Resumed
import org.koin.KoinContext
import org.koin.standalone.StandAloneContext

abstract class PagerScreen<S: PagerScreen<S, P, A>, P : PagerPresenter<P, S, A>, A : Any>(presenter: P)
  : Screen<S, P, A>(presenter), BackPressedHandler, PagerRouter {

  abstract val screenTags: Array<String>
  abstract val firstScreenTag: String
  abstract val canScrollHorizontally: Boolean
  private lateinit var adapter: ScreensAdapter
  private lateinit var layoutManager: RecyclerView.LayoutManager
  private lateinit var recyclerView: RecyclerView

  final override fun createView(inflater: LayoutInflater, parent: ViewGroup): View {
    val view = createViewWithRecycler(inflater, parent)
    recyclerView = recycler(view)
    recyclerView.setItemViewCacheSize(screenTags.size)
    layoutManager = createLayoutManager(parent)
    recyclerView.layoutManager = layoutManager
    PagerSnapHelper().attachToRecyclerView(recyclerView)
    adapter = ScreensAdapter(presenter, screenTags)
    recyclerView.adapter = adapter
    recyclerView.scrollToPosition(screenTags.indexOf(firstScreenTag))
    presenter.pagerRouter = this
    return view
  }

  override fun setArg(arg: A) {
    if (arg::class.java == presenter.argumentClass) {
      super.setArg(arg)
    }
    adapter.setArg(arg)
  }

  protected open fun createViewWithRecycler(inflater: LayoutInflater, parent: ViewGroup): View =
      RecyclerView(activity)

  protected open fun recycler(createdView: View): RecyclerView {
    return createdView as RecyclerView
  }

  protected open fun createLayoutManager(parent: ViewGroup): RecyclerView.LayoutManager {
    return object : LinearLayoutManager(
        parent.context, LinearLayoutManager.HORIZONTAL, false) {
      override fun canScrollHorizontally(): Boolean {
        return super.canScrollHorizontally() && canScrollHorizontally
      }
    }
  }

  @CallSuper
  override fun <A : Any> onBackPressed(arg: A): Boolean {
    return adapter.handleBack(arg)
  }

  @CallSuper
  override fun pause() {
    super.pause()
    adapter.pause()
  }

  @CallSuper
  override fun resume() {
    super.resume()
    adapter.resume()
  }

  @CallSuper
  override fun destroy() {
    super.destroy()
    adapter.destroy()
  }

  @CallSuper
  override fun openTab(tag: String) {
    openTab(tag, Unit)
  }

  override fun <A : Any> setArgTo(tag: String, arg: A) {
    adapter.setArgTo(tag, arg)
  }

  @CallSuper
  override fun <A : Any> openTab(tag: String, arg: A) {
    val indexOf = adapter.getIndexOf(tag)
    adapter.scrollTo(indexOf, arg)
    layoutManager.scrollToPosition(indexOf)
  }
}

internal interface PagerRouter {
  fun <A : Any> openTab(tag: String, arg: A)
  fun openTab(tag: String)
  fun <A : Any> setArgTo(tag: String, arg: A)
}

open class PagerPresenter<P: PagerPresenter<P, S, A>, S : PagerScreen<S, P, A>, A : Any>(router: Router)
  : Presenter<P, S, A>(router), PagerRouter {

  internal lateinit var pagerRouter: PagerRouter

  override fun openTab(tag: String) {
    pagerRouter.openTab(tag)
  }

  override fun <A : Any> openTab(tag: String, arg: A) {
    pagerRouter.openTab(tag, arg)
  }

  override fun <A : Any> setArgTo(tag: String, arg: A) {
    pagerRouter.setArgTo(tag, arg)
  }

  open fun onScreenResumed(position: Int, screen: Screen<*, *, *>) {

  }

  open fun onScreenPaused(position: Int, screen: Screen<*, *, *>) {

  }

}

internal class ScreenHolder(val screen: Screen<*, *, *>) : RecyclerView.ViewHolder(screen.view)

internal class ScreensAdapter(val pagerPresenter: PagerPresenter<*,*,*>, tags: Array<String>) : RecyclerView.Adapter<ScreenHolder>() {
  private val screens: List<Screen<*, *, *>> =
      List(tags.size) { index ->
        val screen: Screen<*, *, *> = (StandAloneContext.koinContext as KoinContext).get(tags[index])
        screen
      }

  private var current: Screen<*, *, *>? = null

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScreenHolder {
    val screen = screens[viewType]
    if (screen.state == Initialized) {
      screen.create(parent)
      screen.create()
    }
    return ScreenHolder(screen)
  }

  override fun onViewAttachedToWindow(holder: ScreenHolder) {
    val screen = holder.screen
    current = screen
    if (screen.state == Created || screen.state == Paused) {
      pagerPresenter.onScreenResumed(getIndexOf(screen.screenTag), screen)
      screen.resume()
    }
  }

  override fun onViewDetachedFromWindow(holder: ScreenHolder) {
    val screen = holder.screen
    if (screen.state == Resumed) {
      pagerPresenter.onScreenPaused(getIndexOf(screen.screenTag), screen)
      screen.pause()
    }
  }

  fun <A : Any> handleBack(arg: A): Boolean {
    val current = this.current
    return current != null && current is BackPressedHandler && current.onBackPressed(arg)
  }

  override fun onBindViewHolder(holder: ScreenHolder, position: Int) {}

  override fun getItemViewType(position: Int): Int = position

  override fun getItemCount(): Int = screens.size

  fun pause() {
    val screen = current
    if (screen != null && screen.state == Resumed) {
      screen.pause()
    }
  }

  fun resume() {
    val screen = current
    if (screen != null && (screen.state == Created || screen.state == Paused)) {
      screen.resume()
    }
  }

  fun destroy() {
    val screen = current
    screens.asReversed().filter { it != screen && (it.state == Created || it.state == Paused) }.forEach { it.destroy() }
    screen?.destroy()
  }

  fun <A : Any> scrollTo(screenIndex: Int, arg: A) {
    (screens[screenIndex] as? Screen<*, *, A>)?.setArg(arg)
  }

  fun getIndexOf(tag: String): Int {
    screens.forEachIndexed { index, screen ->
      if (screen.screenTag == tag) {
        return index
      }
    }
    throw Throwable("screen with tag $tag is not exist in pager")
  }

  fun <A : Any> setArg(arg: A) {
    (current as? Screen<*, *, A>?)?.setArg(arg)
  }

  fun <A : Any> setArgTo(tag: String, arg: A) {
    (screens[getIndexOf(tag)] as? Screen<*, *, A>)?.setArg(arg)
  }
}
