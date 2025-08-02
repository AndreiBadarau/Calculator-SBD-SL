package com.licenta.calculator.ui.squat

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
import com.licenta.calculator.databinding.FragmentSquatBinding
import java.lang.Math.pow
import kotlin.math.E

class SquatFragment : Fragment() {

    private var _binding: FragmentSquatBinding? = null

    private val binding get() = _binding!!

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            val weightText = binding.editSquatWeight.text.toString()
            val repsText = binding.editSquatReps.text.toString()

            if (weightText.isNotEmpty() && repsText.isNotEmpty()) {
                calculateRM()
            } else {
                binding.textViewSquatRM.text = getString(R.string.rm)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val squatViewModel =
            ViewModelProvider(this).get(SquatViewModel::class.java)

        _binding = FragmentSquatBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.editSquatWeight.addTextChangedListener(textWatcher)
        binding.editSquatReps.addTextChangedListener(textWatcher)

        binding.textViewSquatRM.text = getString(R.string.rm)

        binding.btnSquat.setOnClickListener { calculateRM() }
    }

    private fun calculateRM() {
        val weightText = binding.editSquatWeight.text.toString()
        val repsText = binding.editSquatReps.text.toString()

        if (weightText.isEmpty() || repsText.isEmpty()) {
            binding.textViewSquatRM.text = getString(R.string.rm)
            return
        }

        val weight = weightText.toDoubleOrNull()
        val repsDouble = repsText.toDoubleOrNull()

        if (weight == null || repsDouble == null) {
            binding.textViewSquatRM.text = "Numere invalide"
            return
        }

        val repsInt = repsDouble.toInt()

        if (repsDouble != repsInt.toDouble() || repsInt <= 0) {
            // Dacă nu e număr întreg sau este <= 0
            binding.textViewSquatRM.text = "Rep: Nr. întreg > 0"
            return
        }

        val estimated1RM: Double

        if (repsInt <= 10) {
            val denominator = (0.1914 * pow(repsInt.toDouble(), 2.0)) - (5.2087 * repsInt) + 104.43
            if (denominator == 0.0) { // Protecție împărțire la zero
                binding.textViewSquatRM.text = "Eroare calcul (den=0)"
                Log.e("CalculateRM", "Denominator is zero for reps <= 10. Reps: $repsInt")
                return
            }
            estimated1RM = (100 * weight) / denominator
        } else {
            val denominator = 101.33 * pow(E, (-0.037 * repsInt))
            if (denominator == 0.0) { // Protecție împărțire la zero
                binding.textViewSquatRM.text = "Eroare calcul (den=0)"
                Log.e("CalculateRM", "Denominator is zero for reps > 10. Reps: $repsInt")
                return
            }
            estimated1RM = (100 * weight) / denominator
        }

        if (estimated1RM.isNaN() || estimated1RM.isInfinite()) {
            binding.textViewSquatRM.text = "Rezultat invalid"
            Log.e("CalculateRM", "Calculated RM is NaN or Infinite. Weight: $weight, Reps: $repsInt, Result: $estimated1RM")
            return
        }

        binding.textViewSquatRM.text = "%.2f kg".format(estimated1RM)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}