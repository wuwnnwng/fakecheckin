package com.wwn.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wwn.databinding.ItemTextBinding

class SimpleTextAdapter(
    private val onClick: (Int) -> Unit
) : RecyclerView.Adapter<SimpleTextAdapter.Holder>() {

    private val items = mutableListOf<Pair<String, String>>()

    fun submit(data: List<Pair<String, String>>) {
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemTextBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = items[position]
        holder.binding.tvTitle.text = item.first
        holder.binding.tvSub.text = item.second
        holder.binding.root.setOnClickListener { onClick(position) }
    }

    override fun getItemCount(): Int = items.size

    class Holder(val binding: ItemTextBinding) : RecyclerView.ViewHolder(binding.root)
}
