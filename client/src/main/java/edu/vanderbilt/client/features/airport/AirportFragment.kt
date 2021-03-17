package edu.vanderbilt.client.features.airport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import edu.vanderbilt.client.R
import edu.vanderbilt.client.databinding.AirportFragmentBinding

class AirportFragment : Fragment() {
    private val viewModel: AirportViewModel by viewModels()
    private lateinit var binding: AirportFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.airport_fragment,
            container,
            false
        )

        // Allow binding to observe LiveData updates.
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.airports.observe(viewLifecycleOwner) { airports ->
            binding.textView.text = airports.map { airport ->
                "${airport.airportCode} -> ${airport.airportName}"
            }.reduce { acc, next ->
                "$acc\n$next"
            }
        }
    }
}