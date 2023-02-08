package com.example.interviewtask.ui

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater

import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.interviewtask.R
import com.example.interviewtask.databinding.ListViewBinding
import com.example.interviewtask.model.Product

class ProductAdapter(val context: Context, val listener: AdapterCallback) :
    RecyclerView.Adapter<ProductAdapter.Holder>() {

    private var list = ArrayList<Product>()


    class Holder(binding: ListViewBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: ListViewBinding

        init {
            this.binding = binding
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = DataBindingUtil.inflate<ListViewBinding>(
            LayoutInflater.from(parent.context), R.layout.list_view, parent, false
        )
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.binding.bean = list[position]
        Glide.with(context).load(list[position].product_photo).into(holder.binding.iv)
        holder.binding.iv
        holder.binding.pos = position
        holder.binding.callback = listener
    }

    fun getList(): ArrayList<Product> {
        return this.list
    }

    fun removeAt(position: Int) {
        this.list.removeAt(position)
        notifyItemRemoved(position)
    }


    @SuppressLint("NotifyDataSetChanged")
    fun setList(data: ArrayList<Product>) {
        this.list = data
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }
}