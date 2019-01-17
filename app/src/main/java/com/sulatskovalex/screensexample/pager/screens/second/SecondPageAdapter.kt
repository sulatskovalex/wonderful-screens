package com.sulatskovalex.screensexample.pager.screens.second

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sulatskovalex.screensexample.R
import kotlinx.android.synthetic.main.item_second_page.view.*

class SecondPageAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder  =
      Holder(
          LayoutInflater
              .from(parent.context)
              .inflate(R.layout.item_second_page, parent, false))

  override fun getItemCount(): Int = 5

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

  }

  class Holder(view: View) : RecyclerView.ViewHolder(view) {
    init {
      view.second_page_item_list.isNestedScrollingEnabled = false
      view.second_page_item_list.layoutManager =
          LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL,false)
      view.second_page_item_list.adapter = ItemAdapter()
    }
  }


}
class ItemAdapter :RecyclerView.Adapter<RecyclerView.ViewHolder>() {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder  =
      Holder(
          LayoutInflater
              .from(parent.context)
              .inflate(R.layout.item_second_page_view, parent, false))

  override fun getItemCount(): Int = 20

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

  }

  class Holder(view: View) : RecyclerView.ViewHolder(view)
}