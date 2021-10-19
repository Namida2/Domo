package com.example.domo.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

import com.example.domo.databinding.FragmentRegistrationBinding
import com.example.domo.viewModels.RegistrationViewModel
import android.content.DialogInterface


import android.app.AlertDialog
import com.example.domo.viewModels.RegistrationViewModelStates
import com.google.common.eventbus.Subscribe


class RegistrationFragment: Fragment() {

    private val viewModel: RegistrationViewModel by viewModels()
    lateinit var binding: FragmentRegistrationBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegistrationBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        observeViewModelState()

        return binding.root
    }

    private fun observeViewModelState() {
        viewModel.state.observe(viewLifecycleOwner) {
            when(it) {
                is RegistrationViewModelStates.WrongPasswordConfirmation -> {

                }
                else -> {}//defaultState
            }

        }
    }

}