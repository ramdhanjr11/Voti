package com.muramsyah.hima.voti.ui.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.muramsyah.hima.voti.R
import com.muramsyah.hima.voti.core.data.Resource
import com.muramsyah.hima.voti.core.domain.model.CalonKahim
import com.muramsyah.hima.voti.databinding.ItemBotomsheetCalonKahimBinding
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*


class DetailCalonFragment : BottomSheetDialogFragment() {

    companion object {
        val TAG = DetailCalonFragment::class.java.simpleName
        const val EXTRA_DATA = "extra_data"
    }

    private val viewModel: DetailCalonViewModel by viewModel()

    private var _binding: ItemBotomsheetCalonKahimBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = ItemBotomsheetCalonKahimBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null) {
            val calonKahim = arguments?.getParcelable<CalonKahim>(EXTRA_DATA)

            binding.tvNama.text = calonKahim?.nama
            binding.tvVisi.text = calonKahim?.visi
            binding.tvMisi.text = calonKahim?.misi

            binding.btnVoting.setOnClickListener {
                viewModel.getMahasiswa().observe(this, { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            val sf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
                            val dateVoteNow = sf.format(Date())
                            viewModel.voteCandidate(resource.data!!, calonKahim!!, dateVoteNow.toString())
                            Toast.makeText(requireContext(), "Kamu telah memilih ${calonKahim.nama}!", Toast.LENGTH_SHORT).show()
                        }
                        is Resource.Error -> {
                            Toast.makeText(requireContext(), resources.getString(R.string.title_something_wrong), Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}