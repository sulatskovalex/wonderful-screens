package com.github.sulatskovalex.screens

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
import kotlinx.android.synthetic.main.screen_pager.view.*
import org.koin.KoinContext
import org.koin.standalone.StandAloneContext

abstract class PagerScreen<Self : PagerScreen<Self, P, A>, P : PagerPresenter<P, Self, A>, A : Any>(
    presenter: P)
  : Screen<Self, P, A>(presenter), BackPressedHandler, PagerRouter {

  abstract val screenTags: Array<String>
  abstract val firstScreenTag: String
  abstract val canScrollHorizontally: Boolean
  protected lateinit var adapter: PagerAdapter

  private lateinit var manager: RecyclerView.LayoutManager

  private lateinit var recyclerView: RecyclerView

  override fun createView(inflater: LayoutInflater, parent: ViewGroup): View {
    val view = createViewWithRecycler(inflater, parent)
    recyclerView = recycler(view)
    recyclerView.setItemViewCacheSize(screenTags.size)
    manager = createLayoutManager(parent)
    recyclerView.layoutManager = manager
    PagerSnapHelper().attachToRecyclerView(recyclerView)
    adapter = PagerAdapter(screenTags)
    recyclerView.adapter = adapter
    recyclerView.scrollToPosition(screenTags.indexOf(firstScreenTag))
    presenter.pagerRouter = this
    return view
  }

  private fun createLayoutManager(parent: ViewGroup): RecyclerView.LayoutManager {
    return object : LinearLayoutManager(
        parent.context, LinearLayoutManager.HORIZONTAL, false) {
      override fun canScrollHorizontally(): Boolean {
        return super.canScrollHorizontally() && canScrollHorizontally
      }
    }
  }

  override fun onBackPressed(): Boolean {
    return adapter.handleBack()
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

  open fun recycler(createdView: View): RecyclerView {
    return createdView.pager_list
  }

  protected open fun createViewWithRecycler(inflater: LayoutInflater, parent: ViewGroup): View =
      inflater.inflate(R.layout.screen_pager, parent, false)

  override fun open(tag: String) {
    open(tag, Unit)
  }

  override fun <A : Any> open(tag: String, arg: A) {
    val indexOf = adapter.getIndexOf(tag)
    adapter.scrollTo(indexOf, arg)
    manager.scrollToPosition(indexOf)
  }
}

interface PagerRouter {
  fun <A : Any> open(tag: String, arg: A)
  fun open(tag: String)
}

open class PagerPresenter<Self : PagerPresenter<Self, S, A>, S : PagerScreen<S, Self, A>, A : Any>(
    router: Router)
  : Presenter<Self, S, A>(router) {
  lateinit var pagerRouter: PagerRouter

}

class ScreenHolder(val screen: Screen<*, *, *>) : RecyclerView.ViewHolder(screen.view)

class PagerAdapter(tags: Array<String>) : RecyclerView.Adapter<ScreenHolder>() {
  private val screens =
      List(
          tags.size,
          { index ->
            val screen: Screen<*, *, *> = (StandAloneContext.koinContext as KoinContext).get(tags[index])
            screen
          })

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
      screen.resume()
    }
  }

  override fun onViewDetachedFromWindow(holder: ScreenHolder) {
    val screen = holder.screen
    if (screen.state == Resumed) {
      screen.pause()
    }
  }

  fun handleBack(): Boolean {
    val current = this.current
    return current != null && current is BackPressedHandler && current.onBackPressed()
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
    (screens[screenIndex] as Screen<*, *, A>).setArg(arg)
  }

  fun getIndexOf(tag: String): Int {
    var screenIndex = -1
    screens.forEachIndexed { index, screen ->
      if (screen.screenTag == tag) {
        screenIndex = index
        return@forEachIndexed
      }
    }
    if (screenIndex == -1) {
      throw Throwable("screen with tag $tag is not exist in pager")
    }
    return screenIndex
  }
}
