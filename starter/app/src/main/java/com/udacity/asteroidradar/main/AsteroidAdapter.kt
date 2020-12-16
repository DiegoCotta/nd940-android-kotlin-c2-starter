package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.databinding.ItemAsteroidBinding

//I prefer to use Higher-Order Functions from Kotlin instead of interface for this case
class AsteroidAdapter(val onClickListener: (Asteroid) -> Unit) :
    ListAdapter<Asteroid, AsteroidAdapter.ItemAsteroidViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }
    }

    class ItemAsteroidViewHolder(private var binding: ItemAsteroidBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            asteroid: Asteroid,
            onClickListener: (Asteroid) -> Unit
        ) {
            binding.asteroid = asteroid
            binding.root.setOnClickListener { onClickListener(asteroid) }
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ItemAsteroidViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemAsteroidBinding.inflate(layoutInflater, parent, false)
                return ItemAsteroidViewHolder(binding)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemAsteroidViewHolder {
        return ItemAsteroidViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ItemAsteroidViewHolder, position: Int) {
        holder.bind(getItem(position), onClickListener)
    }
}
