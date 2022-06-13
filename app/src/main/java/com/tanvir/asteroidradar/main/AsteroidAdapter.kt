package com.tanvir.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tanvir.asteroidradar.databinding.AsteroidItemBinding
import com.tanvir.asteroidradar.domain.AsteroidModel

class AsteroidAdapter(val asteroidListener: AsteroidListener) :
    ListAdapter<AsteroidModel, AsteroidAdapter.ViewHolder>(AsteroidModelDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), asteroidListener)
    }

    class ViewHolder private constructor(val viewDataBinding: AsteroidItemBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {

        fun bind(item: AsteroidModel, clickListener: AsteroidListener) {
            viewDataBinding.asteroidModel = item
            viewDataBinding.clickListener = clickListener
            viewDataBinding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val withDataBinding = AsteroidItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(withDataBinding)
            }
        }
    }

}

class AsteroidModelDiffCallback : DiffUtil.ItemCallback<AsteroidModel>() {
    override fun areItemsTheSame(oldItem: AsteroidModel, newItem: AsteroidModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: AsteroidModel, newItem: AsteroidModel): Boolean {
        return oldItem == newItem
    }
}

class AsteroidListener(val clickListener: (AsteroidModel) -> Unit) {
    fun onClick(asteroid: AsteroidModel) = clickListener(asteroid)
}