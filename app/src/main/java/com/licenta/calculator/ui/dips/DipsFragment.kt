package com.licenta.calculator.ui.dips

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.licenta.calculator.R
import com.licenta.calculator.databinding.FragmentDipsBinding
import java.lang.Math.pow

class DipsFragment : Fragment() {

    private var _binding: FragmentDipsBinding? = null
    private val binding get() = _binding!!

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            val bodyWeightText = binding.editDipsBodyWeight.text.toString()
            val addedWeightText = binding.editDipsAddedWeight.text.toString()
            val repsText = binding.editDipsReps.text.toString()

            if (bodyWeightText.isNotEmpty() && addedWeightText.isNotEmpty() && repsText.isNotEmpty()) {
                calculateRM()
            } else {
                binding.textViewDipsRM.text = getString(R.string.rm)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDipsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.editDipsBodyWeight.addTextChangedListener(textWatcher)
        binding.editDipsAddedWeight.addTextChangedListener(textWatcher)
        binding.editDipsReps.addTextChangedListener(textWatcher)

        binding.textViewDipsRM.text = getString(R.string.rm)

    }

    private fun calculateRM() {
        val bodyWeightText = binding.editDipsBodyWeight.text.toString()
        val addedWeightText = binding.editDipsAddedWeight.text.toString()
        val repsText = binding.editDipsReps.text.toString()

        if (bodyWeightText.isEmpty() || addedWeightText.isEmpty() || repsText.isEmpty()) {
            binding.textViewDipsRM.text = getString(R.string.rm)
            return
        }

        val bodyWeight = bodyWeightText.toDoubleOrNull()
        val addedWeight = addedWeightText.toDoubleOrNull()
        val repsDouble = repsText.toDoubleOrNull()

        if (bodyWeight == null || addedWeight == null || repsDouble == null) {
            binding.textViewDipsRM.text = "Numere invalide"
            return
        }

        val repsInt = repsDouble.toInt()

        if (repsDouble != repsInt.toDouble() || repsInt <= 0) {
            binding.textViewDipsRM.text = "Rep: Nr. întreg > 0"
            return
        }

        val estimated1RM: Double

        estimated1RM = ((((bodyWeight / 2) + addedWeight) / (1.0278 - (0.0278 * repsInt))) - (bodyWeight/2))

        binding.textViewDipsRM.text = "%.2f kg".format(estimated1RM)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}