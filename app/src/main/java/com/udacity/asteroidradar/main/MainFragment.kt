package com.udacity.asteroidradar.main

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.accessibility.AccessibilityEvent
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.*
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.roomdb.AsteroidDb
import kotlinx.coroutines.*
import java.nio.file.Paths.get
import java.util.*

class MainFragment : Fragment() {


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        val application = requireNotNull(this.activity).application
        val dataSource = AsteroidDb.getInstance(application).asteroidDao
        val imageSource = AsteroidDb.getInstance(application).imageDao

        val viewModelFactory = AsteroidViewModelFactory(
            dataSource,
            imageSource,
            application
        )
        val mainViewModel =
            ViewModelProvider(
                this, viewModelFactory).get(MainViewModel::class.java)

        binding.asteroidRecycler.adapter =
            AsteroidItemsAdapter(AsteroidItemsAdapter.OnClickListener {
                this.findNavController()
                    // Navigate to detailFragment once an item clicked and call doneNavigating to return value to null

                    .navigate(MainFragmentDirections.actionShowDetail(it))
            })
        binding.lifecycleOwner = this
        binding.viewModel = mainViewModel
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

              return true
    }

}
