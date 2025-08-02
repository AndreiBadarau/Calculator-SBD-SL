package com.licenta.calculator.ui.pullup

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.licenta.calculator.R
import com.licenta.calculator.databinding.FragmentPullUpBinding

class PullUpFragment : Fragment() {

    private var _binding: FragmentPullUpBinding? = null

    private val binding get() = _binding!!

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            val bodyWeightText = binding.editPullUpBodyWeight.text.toString()
            val addedWeightText = binding.editPullUpAddedWeight.text.toString()
            val repsText = binding.editPullUpReps.text.toString()

            if (bodyWeightText.isNotEmpty() && addedWeightText.isNotEmpty() && repsText.isNotEmpty()) {
                calculateRM()
            } else {
                binding.textViewPullUpRM.text = getString(R.string.rm)
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

        _binding = FragmentPullUpBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.editPullUpBodyWeight.addTextChangedListener(textWatcher)
        binding.editPullUpAddedWeight.addTextChangedListener(textWatcher)
        binding.editPullUpReps.addTextChangedListener(textWatcher)

        binding.textViewPullUpRM.text = getString(R.string.rm)
    }

    private fun calculateRM() {
        val bodyWeightText = binding.editPullUpBodyWeight.text.toString()
        val addedWeightText = binding.editPullUpAddedWeight.text.toString()
        val repsText = binding.editPullUpReps.text.toString()

        if (bodyWeightText.isEmpty() || addedWeightText.isEmpty() || repsText.isEmpty()) {
            binding.textViewPullUpRM.text = getString(R.string.rm)
            return
        }

        val bodyWeight = bodyWeightText.toDoubleOrNull()
        val addedWeight = addedWeightText.toDoubleOrNull()
        val repsDouble = repsText.toDoubleOrNull()

        if (bodyWeight == null || addedWeight == null || repsDouble == null) {
            binding.textViewPullUpRM.text = "Numere invalide"
            return
        }

        val repsInt = repsDouble.toInt()

        if (repsDouble != repsInt.toDouble() || repsInt <= 0) {
            binding.textViewPullUpRM.text = "Rep: Nr. întreg > 0"
            return
        }

        val estimated1RM: Double

        estimated1RM = (((bodyWeight + addedWeight) * ( 1 + ( 0.0333 * (repsInt - 1)))) - bodyWeight)

        binding.textViewPullUpRM.text = "%.2f kg".format(estimated1RM)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}