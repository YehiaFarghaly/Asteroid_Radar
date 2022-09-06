package com.udacity.asteroidradar

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView

import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.databinding.AsteroidItemViewBinding
import com.udacity.asteroidradar.main.MainFragmentDirections

class asteroidsAdapter:ListAdapter<Asteroid,asteroidsAdapter.AsteroidViewHolder>(AsteroidsDiff()) {

    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
       val item =getItem(position)
       holder.bind(item)
        holder.itemView.setOnClickListener{
            val direction = MainFragmentDirections.actionShowDetail(item)
            it.findNavController().navigate(direction)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
       return AsteroidViewHolder.from(parent)
    }

    class AsteroidViewHolder private constructor(val binding: AsteroidItemViewBinding):RecyclerView.ViewHolder(binding.root) {
        val name:TextView
        val date:TextView
        val hazardous:ImageView
        init {
            name = binding.asteroidName
            date = binding.asteroidDate
            hazardous=binding.asteroidHazerd
        }
        fun bind(item:Asteroid) {
            binding.asteroidItem=item
            name.text= item.codename
            date.text = item.closeApproachDate
           binding.executePendingBindings()
        }
        companion object{
            @SuppressLint("SuspiciousIndentation")
            fun from(parent:ViewGroup):AsteroidViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
              val binding=AsteroidItemViewBinding.inflate(layoutInflater,parent,false)
                return AsteroidViewHolder(binding)
            }
        }
    }
    class AsteroidsDiff:DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id==newItem.id
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem==newItem
        }
    }
    interface onClick{
        fun onRecycleItemClicked(position:Int)
    }


}