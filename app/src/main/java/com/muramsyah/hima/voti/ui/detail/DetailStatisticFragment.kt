package com.muramsyah.hima.voti.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.muramsyah.hima.voti.core.domain.model.CalonKahim
import com.muramsyah.hima.voti.databinding.ItemBotomsheetStatisticBinding
import org.koin.android.viewmodel.ext.android.viewModel


class DetailStatisticFragment : BottomSheetDialogFragment() {

    companion object {
        val TAG = DetailStatisticFragment::class.java.simpleName
        const val EXTRA_DATA = "extra_data"
    }

    private val viewModel: DetailCalonViewModel by viewModel()

    private var _binding: ItemBotomsheetStatisticBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = ItemBotomsheetStatisticBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null) {
            val calonKahim = arguments?.getParcelable<CalonKahim>(EXTRA_DATA)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}