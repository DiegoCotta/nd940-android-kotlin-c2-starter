package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onViewCreated()"
        }
        ViewModelProvider(
            this,
            MainViewModel.Factory(activity.application)
        ).get(MainViewModel::class.java)

    }

    private var asteroidAdapter: AsteroidAdapter? = null
    private lateinit var binding: FragmentMainBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        asteroidAdapter = AsteroidAdapter {
            viewModel.displayAsteroidDetails(it)

        }

        binding.asteroidRecycler.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = asteroidAdapter
        }

        setHasOptionsMenu(true)

        setObservables()

        return binding.root
    }

    private fun setObservables() {
        viewModel.navigateToAsteroidDetails.observe(viewLifecycleOwner, Observer { asteroid ->
            if (asteroid != null) {
                findNavController().navigate(MainFragmentDirections.actionShowDetail(asteroid))
                viewModel.displayAsteroidDetailsComplete()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.show_all_menu -> viewModel.setFilterAll()
            R.id.show_today_menu -> viewModel.setFilterToday()
            R.id.show_week_menu -> viewModel.setFilterWeek()
        }
        return true
    }
}
