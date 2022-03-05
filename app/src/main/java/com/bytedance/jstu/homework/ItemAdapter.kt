package com.bytedance.jstu.homework

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ItemAdapter : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    private val contentList = mutableListOf<String>()
    private val filteredList = mutableListOf<String>()

    class ItemViewHolder(view: View): RecyclerView.ViewHolder(view) {

        private val tv= view.findViewById<Button>(R.id.button1)
        private val tr = view.findViewById<TextView>(R.id.brief)

        fun bind(content: String) {
            tv.text = content
            tr.text = "这里是${content}的概述信息"
            tv.setOnClickListener{
                Log.d("press", "点击${content}")
                val intent = Intent()
                intent.setClass(tv.context, IntroductionActivity::class.java)
                val bundle = Bundle()
                bundle.putString("intro", "这里是${content}的简介")
                intent.putExtras(bundle)
                tv.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val v = View.inflate(parent.context, R.layout.item_layout, null)
        return ItemViewHolder(v)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(filteredList[position])
    }

    override fun getItemCount(): Int = filteredList.size

    fun setContentList(list: List<String>) {
        contentList.clear()
        contentList.addAll(list)
        filteredList.clear()
        filteredList.addAll(list)
        notifyDataSetChanged()
    }

    fun setFilter(keyword: String?) {
        filteredList.clear()
        if (keyword?.isNotEmpty() == true) {
            filteredList.addAll(contentList.filter { it.contains(keyword) })
        } else {
            filteredList.addAll(contentList)
        }
        notifyDataSetChanged()
    }
}