package com.licenta.calculator.ui.deadlift

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
import com.licenta.calculator.databinding.FragmentDeadliftBinding
import java.lang.Math.pow
import kotlin.math.E

class DeadliftFragment : Fragment() {

    private var _binding: FragmentDeadliftBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            val weightText = binding.editDeadliftWeight.text.toString()
            val repsText = binding.editDeadliftReps.text.toString()

            if (weightText.isNotEmpty() && repsText.isNotEmpty()) {
                calculateRM()
            } else {
                binding.textViewDeadliftRM.text = getString(R.string.rm)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentDeadliftBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.editDeadliftWeight.addTextChangedListener(textWatcher)
        binding.editDeadliftReps.addTextChangedListener(textWatcher)

        binding.textViewDeadliftRM.text = getString(R.string.rm)

        binding.btnDeadlift.setOnClickListener { calculateRM() }
    }

    private fun calculateRM() {
        // Am observat că în contextul anterior foloseai ID-uri de bench press aici.
        // Le-am corectat pentru a se potrivi cu cele probabile pentru deadlift din textWatcher.
        val weightText = binding.editDeadliftWeight.text.toString()
        val repsText = binding.editDeadliftReps.text.toString()

        if (weightText.isEmpty() || repsText.isEmpty()) {
            binding.textViewDeadliftRM.text = getString(R.string.rm)
            return
        }

        val weight = weightText.toDoubleOrNull()
        val repsDouble = repsText.toDoubleOrNull() // Reps pot fi introduse ca double

        if (weight == null || repsDouble == null) {
            binding.textViewDeadliftRM.text = "Numere invalide"
            return
        }

        if (repsDouble <= 0) {
            binding.textViewDeadliftRM.text = "Rep: Trebuie să fie > 0"
            return
        }


        val estimated1RM = (weight * repsDouble * 0.0333) + weight

        if (estimated1RM.isNaN() || estimated1RM.isInfinite()) {
            binding.textViewDeadliftRM.text = "Rezultat invalid"
            Log.e("CalculateRM_Deadlift", "Calculated RM is NaN or Infinite. Weight: $weight, Reps: $repsDouble, Result: $estimated1RM")
            return
        }

        binding.textViewDeadliftRM.text = "%.2f kg".format(estimated1RM)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}