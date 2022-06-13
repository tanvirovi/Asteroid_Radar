package com.tanvir.asteroidradar.main

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tanvir.asteroidradar.R
import com.tanvir.asteroidradar.databinding.FragmentMainBinding

@RequiresApi(Build.VERSION_CODES.N)
class MainFragment : Fragment() {
    private var viewModelAdapter: AsteroidAdapter? = null
    private val viewModel: MainViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onViewCreated()"
        }
        ViewModelProvider(
            this,
            MainViewModel.Factory(activity.application)
        )[MainViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.asteroidWeekList.observe(viewLifecycleOwner) {
            it?.let {
                viewModelAdapter?.submitList(it)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentMainBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_main,
            container, false
        )
        binding.lifecycleOwner = viewLifecycleOwner

        binding.viewModel = viewModel

        setHasOptionsMenu(true)
        viewModelAdapter = AsteroidAdapter(AsteroidListener { it ->
            viewModel.onAsteroidClicked(it)
        })

        viewModel.pod.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                binding.podModel = it.first()
            }
        }
        viewModel.navigateToFragmentDetails.observe(viewLifecycleOwner) {
            it?.let {
                this.findNavController().navigate(
                    MainFragmentDirections.actionShowDetail(it)
                )
                viewModel.onFragmentDetailsNavigated()
            }
        }

        binding.root.findViewById<RecyclerView>(R.id.asteroid_recycler).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewModelAdapter
        }
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.show_all_menu -> {
                viewModel.asteroidWeekList.observe(viewLifecycleOwner) {
                    it?.let {
                        viewModelAdapter?.submitList(it)
                    }
                }
                true
            }
            R.id.show_today_menu ->{
                viewModel.asteroidTodayList.observe(viewLifecycleOwner) {
                    it?.let {
                        viewModelAdapter?.submitList(it)
                    }
                }
                true
            }
            R.id.show_saved_menu ->{
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
