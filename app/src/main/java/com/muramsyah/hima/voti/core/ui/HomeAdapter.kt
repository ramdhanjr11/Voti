package com.muramsyah.hima.voti.core.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.muramsyah.hima.voti.R
import com.muramsyah.hima.voti.core.domain.model.CalonKahim
import com.muramsyah.hima.voti.databinding.ItemCalonKahimBinding

class HomeAdapter : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    private val listCalonKahim = ArrayList<CalonKahim>()

    var onClick: ((CalonKahim) -> Unit)? = null

    fun setData(data: List<CalonKahim>?) {
        if (data == null) return
        listCalonKahim.clear()
        listCalonKahim.addAll(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemCalonKahimBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listCalonKahim[position])
    }

    override fun getItemCount(): Int = listCalonKahim.size

    inner class ViewHolder(private val binding: ItemCalonKahimBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data: CalonKahim) {

            val circularProgressBar = CircularProgressDrawable(itemView.context).apply {
                strokeWidth = 5f
                centerRadius = 30f
                start()
            }

            Glide.with(binding.root.context)
                .load(data.imageUrl)
                .apply(RequestOptions.placeholderOf(circularProgressBar))
                .into(binding.imgCalon)

            binding.tvCalon.text = "Calon ${(adapterPosition+1)}"
            binding.tvNamaCalon.text = data.nama

            binding.cardView.setOnClickListener {
                onClick?.invoke(data)
            }
        }
    }
}