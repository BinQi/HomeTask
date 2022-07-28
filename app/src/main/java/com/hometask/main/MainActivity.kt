package com.hometask.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.google.android.material.tabs.TabLayout
import com.hometask.app.BaseActivity
import com.hometask.app.R
import com.hometask.app.databinding.ActivityMainBinding
import com.hometask.ext.toTabType
import com.hometask.main.home.HomeFragment
import com.hometask.main.home.HomeViewModel
import com.hometask.main.profile.ProfileFragment
import com.hometask.main.profile.ProfileViewModel

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private val profileViewModel: ProfileViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()
    private val fragments by lazy { listOf(HomeFragment(), ProfileFragment()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        profileViewModel.onCreate()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        binding.tab.apply {
            TabType.values().forEachIndexed { index, tab ->
                addTab(newTab())
                getTabAt(index)?.also {
                    it.setIcon(tab.icon)
                    it.setText(tab.title)
                }
            }
            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    handleTabSelected(tab.toTabType())
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    if (tab.toTabType() == TabType.HOME) {
                        homeViewModel.refreshHomePage()
                    }
                }
            })
            handleTabSelected(TabType.HOME)
        }
    }

    private fun handleTabSelected(tabType: TabType?) {
        when (tabType) {
            TabType.HOME -> {
                fragments[0]
            }
            TabType.PROFILE -> {
                fragments[1]
            }
            else -> null
        }?.let {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, it)
                .commit()
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
}