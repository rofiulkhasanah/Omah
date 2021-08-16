package com.creartpro.smarthome.ui.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.creartpro.smarthome.databinding.ItemDeviceBinding
import com.creartpro.smarthome.entity.PerangkatEntity
import com.creartpro.smarthome.ui.perangkat.PerangkatActivity

class PerangkatAdapter(private val listPerangkat: List<PerangkatEntity>) :
    RecyclerView.Adapter<PerangkatAdapter.PerangkatViewHolder>() {
    inner class PerangkatViewHolder(binding: ItemDeviceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val image = binding.imgHomeDevice
        private val nama_perangkat = binding.tvLabelDevice

        fun bind(perangkatEntity: PerangkatEntity) {
            image.setImageResource(perangkatEntity.imgPerangkatOn)
            nama_perangkat.text = perangkatEntity.nama_perangkat

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, PerangkatActivity::class.java)
                intent.putExtra(PerangkatActivity.DEVICE, perangkatEntity)
                it.context.startActivity(intent)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PerangkatViewHolder {
        val binding = ItemDeviceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PerangkatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PerangkatViewHolder, position: Int) {
        holder.bind(listPerangkat[position])
    }

    override fun getItemCount(): Int = listPerangkat.size
}