package com.sulatskovalex.screens

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.sulatskovalex.screens.Screen.Companion.Created
import com.sulatskovalex.screens.Screen.Companion.Initialized
import com.sulatskovalex.screens.Screen.Companion.Paused
import com.sulatskovalex.screens.Screen.Companion.Resumed
import kotlinx.android.synthetic.main.screen_pager.view.*
import org.koin.KoinContext
import org.koin.standalone.StandAloneContext

abstract class PagerScreen<Self : PagerScreen<Self, P, A>, P : Presenter<P, Self, A>, A : Any>(
    presenter: P)
  : Screen<Self, P, A>(presenter) {

  abstract val screenTags: Array<String>
  abstract val firstTag: String
  private lateinit var adapter: PagerAdapter

  override fun createView(parent: ViewGroup): View {
    val view = createViewWithRecycler(parent)
    val recyclerView = recycler(view)
    recyclerView.setItemViewCacheSize(screenTags.size)
    val manager = LinearLayoutManager(parent.context, LinearLayoutManager.HORIZONTAL, false)
    recyclerView.layoutManager = manager
    PagerSnapHelper().attachToRecyclerView(recyclerView)
    adapter = PagerAdapter(screenTags)
    recyclerView.adapter = adapter
    recyclerView.scrollToPosition(screenTags.indexOf(firstTag))
    return view
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

  open fun recycler(view: View): RecyclerView {
    return view.pager_list
  }

  protected open fun createViewWithRecycler(parent: ViewGroup): View = inflate(parent,
                                                                               R.layout.screen_pager)
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
}
