package com.creartpro.smarthome.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.creartpro.smarthome.R
import com.creartpro.smarthome.databinding.ActivityOnboardingBinding
import com.creartpro.smarthome.ui.home.HomeActivity

class OnBoardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var onboardingAdapter: OnBoardingAdapter
    private lateinit var indicatorContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.white)

        setOnBoardingItems()
        setIndicators()
        setCurrentIndicator(0)
    }

    private fun setOnBoardingItems() {
        onboardingAdapter = OnBoardingAdapter(
            listOf(
                OnBoardingItem(
                    onBoardingImage = R.drawable.ic_onboarding_1,
                    title = getString(R.string.onboarding_1),
                    description = getString(R.string.onBoardingDesc_1)
                ),
                OnBoardingItem(
                    onBoardingImage = R.drawable.ic_onboarding_2,
                    title = getString(R.string.onboarding_2),
                    description = getString(R.string.onBoardingDesc_2)
                ),
                OnBoardingItem(
                    onBoardingImage = R.drawable.ic_onboarding_3,
                    title = getString(R.string.onboarding_3),
                    description = getString(R.string.onBoardingDesc_3)
                )
            )
        )

        binding.apply {
            val onBoardingViewPager = vpOnboarding
            onBoardingViewPager.adapter = onboardingAdapter
            onBoardingViewPager.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    setCurrentIndicator(position)
                }
            })
            (onBoardingViewPager.getChildAt(0) as RecyclerView).overScrollMode =
                RecyclerView.OVER_SCROLL_NEVER

            btnNext.setOnClickListener {
                vpOnboarding.setCurrentItem(vpOnboarding.currentItem + 1, true)
            }

            btnStart.setOnClickListener {
                startActivity(Intent(this@OnBoardingActivity, HomeActivity::class.java))
                finish()
            }
        }
    }

    private fun loadLastScreen() {
        binding.btnNext.visibility = View.INVISIBLE
        binding.btnStart.visibility = View.VISIBLE
    }

    private fun setIndicators() {
        indicatorContainer = binding.indicatorsContainer
        val indicators = arrayOfNulls<ImageView>(onboardingAdapter.itemCount)
        val layoutParams: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        layoutParams.setMargins(8, 0, 8, 0)
        for (i in indicators.indices) {
            indicators[i] = ImageView(applicationContext)
            indicators[i]?.let {
                it.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.onboarding_indicator_inactive
                    )
                )
                it.layoutParams = layoutParams
                indicatorContainer.addView(it)
            }
        }
    }

    private fun setCurrentIndicator(position: Int) {
        val childCount = indicatorContainer.childCount
        for (i in 0 until childCount) {
            val imageView = indicatorContainer.getChildAt(i) as ImageView
            if (i == position) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.onboarding_indicator_active
                    )
                )
                if (i == onboardingAdapter.itemCount - 1) {
                    loadLastScreen()
                }
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.onboarding_indicator_inactive
                    )
                )
            }
        }
    }
}