package com.licenta.calculator.ui.sbd

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.licenta.calculator.R
import com.licenta.calculator.databinding.FragmentSbdMainBinding

class SbdMainFragment : Fragment() {

    private var _binding: FragmentSbdMainBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSbdMainBinding.inflate(inflater, container, false) // Inițializează _binding aici
        return binding.root // Returnează rădăcina binding-ului
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSquat.setOnClickListener {
            findNavController().navigate(R.id.action_sbdMain_to_squat)
        }

        binding.btnBenchPress.setOnClickListener {
            findNavController().navigate(R.id.action_sbdMain_to_bench_press)
        }

        binding.btnDeadlift.setOnClickListener {
            findNavController().navigate(R.id.action_sbdMain_to_deadlift)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Curăță referința la binding
    }
}

