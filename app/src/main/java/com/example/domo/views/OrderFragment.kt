package com.example.domo.views

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.domo.R
import com.example.domo.databinding.FragmentOrderBinding
import com.example.domo.viewModels.SharedViewModelStates
import com.example.domo.viewModels.ViewModelFactory
import com.example.domo.viewModels.WaiterActivityOrderFragmentSharedViewModel
import com.google.android.material.transition.MaterialContainerTransform
import extentions.appComponent

class OrderFragment : Fragment() {
    private lateinit var binding: FragmentOrderBinding
    private lateinit var sharedViewModel: WaiterActivityOrderFragmentSharedViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        sharedViewModel = ViewModelProvider(requireActivity(), ViewModelFactory(context.appComponent)).get(
            WaiterActivityOrderFragmentSharedViewModel::class.java)
    }

    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentOrderBinding.inflate(layoutInflater)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment
            duration = 300
        }
        observeViewModelStates()
        postponeEnterTransition()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.doOnPreDraw {
            startPostponedEnterTransition()
        }
    }

    private fun observeViewModelStates() {
        sharedViewModel.states.observe(viewLifecycleOwner) {
            when(it) {
                is SharedViewModelStates.ShowingMenuDialog -> {

                }
                else -> {} //DefaultState
            }
        }
    }
}