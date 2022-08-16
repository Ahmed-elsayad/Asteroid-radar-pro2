package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.databinding.RecyclerItemBinding

class AsteroidItemsAdapter(val onClickListener: OnClickListener) :
    ListAdapter<Asteroid, AsteroidItemsAdapter.AsteroidViewHolder>(AsteroidDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
        val itemBinding = RecyclerItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false)

        return AsteroidViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        val asteroidItem = getItem(position)
        holder.itemView.setOnClickListener { //handle click events
            onClickListener.onClick(asteroidItem)
        }
        holder.onBind(asteroidItem)
    }

    class AsteroidViewHolder(val binding: RecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private lateinit var _asteroid: Asteroid

        fun onBind(asteroid: Asteroid) {
            _asteroid = asteroid
            binding.asteroid = _asteroid
            binding.executePendingBindings()

        }
    }

    class AsteroidDiffCallback : DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem == newItem
        }

    }

    class OnClickListener(val clickListener: (asteroid: Asteroid) -> Unit) {
        fun onClick(asteroid: Asteroid) = clickListener(asteroid)
    }
}