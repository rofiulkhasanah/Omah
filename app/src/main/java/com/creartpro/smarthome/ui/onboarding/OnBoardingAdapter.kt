package com.creartpro.smarthome.ui.onboarding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.creartpro.smarthome.databinding.ItemOnboardingBinding

class OnBoardingAdapter(private val listOnBoardingItems: List<OnBoardingItem>) :
    RecyclerView.Adapter<OnBoardingAdapter.OnBoardingViewHolder>() {

    inner class OnBoardingViewHolder(binding: ItemOnboardingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val image = binding.imgOnBoarding
        private val title = binding.tvOnBoarding
        private val desc = binding.tvOnBoardingDesc

        fun bind(onBoardingItem: OnBoardingItem) {
            image.setImageResource(onBoardingItem.onBoardingImage)
            title.text = onBoardingItem.title
            desc.text = onBoardingItem.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoardingViewHolder {
        val binding =
            ItemOnboardingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OnBoardingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OnBoardingViewHolder, position: Int) {
        holder.bind(listOnBoardingItems[position])
    }

    override fun getItemCount(): Int = listOnBoardingItems.size
}