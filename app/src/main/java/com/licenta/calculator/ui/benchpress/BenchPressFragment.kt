package com.licenta.calculator.ui.benchpress

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.licenta.calculator.R
import com.licenta.calculator.databinding.FragmentBenchPressBinding
import java.lang.Math.pow
import kotlin.math.E

class BenchPressFragment : Fragment() {

    private var _binding: FragmentBenchPressBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            val weightText = binding.editBenchPressWeight.text.toString()
            val repsText = binding.editBenchPressReps.text.toString()

            if (weightText.isNotEmpty() && repsText.isNotEmpty()) {
                calculateRM()
            } else {
                binding.textViewBenchPressRM.text = getString(R.string.rm)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentBenchPressBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.editBenchPressWeight.addTextChangedListener(textWatcher)
        binding.editBenchPressReps.addTextChangedListener(textWatcher)

        binding.textViewBenchPressRM.text = getString(R.string.rm)

        binding.btnBenchPress.setOnClickListener { calculateRM() }
    }

    private fun calculateRM() {
        val weightText = binding.editBenchPressWeight.text.toString()
        val repsText = binding.editBenchPressReps.text.toString()

        if (weightText.isEmpty() || repsText.isEmpty()) {
            binding.textViewBenchPressRM.text = getString(R.string.rm)
            return
        }

        val weight = weightText.toDoubleOrNull()
        val repsDouble = repsText.toDoubleOrNull()

        if (weight == null || repsDouble == null) {
            binding.textViewBenchPressRM.text = "Numere invalide"
            return
        }

        val repsInt = repsDouble.toInt()

        if (repsDouble != repsInt.toDouble() || repsInt <= 0) {
            binding.textViewBenchPressRM.text = "Rep: Nr. întreg > 0"
            return
        }

        val estimated1RM: Double

        if (repsInt <= 10) {
            // FORMULA PENTRU REPETĂRI <= 10 (BENCH PRESS)
            val denominator = (0.1025 * pow(repsInt.toDouble(), 2.0)) - (3.8208 * repsInt) + 103.59
            if (denominator == 0.0) {
                binding.textViewBenchPressRM.text = "Eroare calcul (den=0)"
                Log.e("CalculateRM_Bench", "Denominator is zero for reps <= 10. Reps: $repsInt")
                return
            }
            estimated1RM = (100 * weight) / denominator
        } else {
            // FORMULA PENTRU REPETĂRI > 10 (BENCH PRESS)
            // Constanta 2.71828 este E
            val denominator = 102.22 * pow(E, (-0.031 * repsInt))
            if (denominator == 0.0) {
                binding.textViewBenchPressRM.text = "Eroare calcul (den=0)"
                Log.e("CalculateRM_Bench", "Denominator is zero for reps > 10. Reps: $repsInt")
                return
            }
            estimated1RM = (100 * weight) / denominator
        }

        if (estimated1RM.isNaN() || estimated1RM.isInfinite()) {
            binding.textViewBenchPressRM.text = "Rezultat invalid"
            Log.e("CalculateRM_Bench", "Calculated RM is NaN or Infinite. Weight: $weight, Reps: $repsInt, Result: $estimated1RM")
            return
        }

        binding.textViewBenchPressRM.text = "%.2f kg".format(estimated1RM)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}