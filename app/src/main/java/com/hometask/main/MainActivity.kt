package com.hometask.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.hometask.app.BaseActivity
import com.hometask.app.R
import com.hometask.app.databinding.ActivityMainBinding
import com.hometask.ext.toTabType
import com.hometask.main.home.HomeFragment
import com.hometask.main.home.HomeViewModel
import com.hometask.main.profile.ProfileFragment
import com.hometask.main.profile.ProfileViewModel
import kotlin.math.max

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private val profileViewModel: ProfileViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()
    private val fragmentTabs by lazy {
        listOf(
            FragmentTab(TabType.HOME, HomeFragment()),
            FragmentTab(TabType.PROFILE, ProfileFragment()),
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        profileViewModel.onCreate()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        binding.viewPager.apply {
            adapter = MainPagerAdapter(supportFragmentManager)
            offscreenPageLimit = max((adapter as MainPagerAdapter).count - 1, 1)
        }
        binding.tab.apply {
            setupWithViewPager(binding.viewPager)
            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    if (tab.toTabType() == TabType.HOME) {
                        homeViewModel.refreshHomePage()
                    }
                }
            })
            TabType.values().forEachIndexed { index, tab ->
                getTabAt(index)?.also {
                    it.setIcon(tab.icon)
                    it.setText(tab.title)
                }
            }
        }
    }

    inner class MainPagerAdapter(fm: FragmentManager)
        : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getCount(): Int {
            return fragmentTabs.size
        }

        override fun getItem(position: Int): Fragment {
            return fragmentTabs[position].fragment
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return resources.getString(fragmentTabs[position].type.title)
        }
    }

    enum class TabType(@DrawableRes val icon: Int, @StringRes val title: Int) {
        HOME(R.drawable.ic_baseline_home_48, R.string.home),
        PROFILE(R.drawable.ic_mine_48, R.string.profile);

        companion object {
            fun fromPosition(position: Int): TabType {
                return values().first { it.ordinal == position }
            }
        }
    }

    private data class FragmentTab(val type: TabType, val fragment: Fragment)
}