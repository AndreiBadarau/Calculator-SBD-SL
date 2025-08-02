package com.licenta.calculator.ui.muscleup

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.licenta.calculator.R
import com.licenta.calculator.databinding.FragmentMuscleUpBinding
import java.lang.Math.pow

class MuscleUpFragment : Fragment() {

    private var _binding: FragmentMuscleUpBinding? = null
    private val binding get() = _binding!!

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            val bodyWeightText = binding.editMuscleUpBodyWeight.text.toString()
            val addedWeightText = binding.editMuscleUpAddedWeight.text.toString()
            val repsText = binding.editMuscleUpReps.text.toString()

            if (bodyWeightText.isNotEmpty() && addedWeightText.isNotEmpty() && repsText.isNotEmpty()) {
                calculateRM()
            } else {
                binding.textViewMuscleUpRM.text = getString(R.string.rm)
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
        _binding = FragmentMuscleUpBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.editMuscleUpBodyWeight.addTextChangedListener(textWatcher)
        binding.editMuscleUpAddedWeight.addTextChangedListener(textWatcher)
        binding.editMuscleUpReps.addTextChangedListener(textWatcher)

        binding.textViewMuscleUpRM.text = getString(R.string.rm)

    }

    private fun calculateRM() {
        val bodyWeightText = binding.editMuscleUpBodyWeight.text.toString()
        val addedWeightText = binding.editMuscleUpAddedWeight.text.toString()
        val repsText = binding.editMuscleUpReps.text.toString()

        if (bodyWeightText.isEmpty() || addedWeightText.isEmpty() || repsText.isEmpty()) {
            binding.textViewMuscleUpRM.text = getString(R.string.rm)
            return
        }

        val bodyWeight = bodyWeightText.toDoubleOrNull()
        val addedWeight = addedWeightText.toDoubleOrNull()
        val repsDouble = repsText.toDoubleOrNull()

        if (bodyWeight == null || addedWeight == null || repsDouble == null) {
            binding.textViewMuscleUpRM.text = "Numere invalide"
            return
        }

        val repsInt = repsDouble.toInt()

        if (repsDouble != repsInt.toDouble() || repsInt <= 0) {
            binding.textViewMuscleUpRM.text = "Rep: Nr. întreg > 0"
            return
        }

        val estimated1RM: Double

        estimated1RM = 0.5 * ((-pow(((pow(addedWeight, 2.0)) - (294 * addedWeight) - (24 * bodyWeight * (pow(((pow(repsDouble, 1.5)) - 1), 0.5))) + 21609), 0.5)) + addedWeight + 147)

        binding.textViewMuscleUpRM.text = "%.2f kg".format(estimated1RM)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
