@file:Suppress("OverrideDeprecatedMigration", "OverrideDeprecatedMigration")

package com.udacity.asteroidradar.main

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.Clicked
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.asteroidsAdapter
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        val adapter = asteroidsAdapter()
        binding.asteroidRecycler.adapter=adapter
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        viewModel.asteroidsList.observe(this as LifecycleOwner, Observer {
     it?.let {
         adapter.submitList(it)
     }
        })

        setHasOptionsMenu(true)
Log.i("yehiaso",viewModel.pictureOfDay.value.toString())
        var pictureImage:ImageView = binding.activityMainImageOfTheDay
viewModel.pictureOfDay.observe(this as LifecycleOwner, Observer {
    Picasso.get().load(it.url).into(pictureImage)
})

        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.onItemClicked(
            when(item.itemId) {
                R.id.show_all_menu-> Clicked.All
                R.id.show_today_menu -> Clicked.Today
                else -> Clicked.ThisWeek
            }
        )
        return true
    }
}
