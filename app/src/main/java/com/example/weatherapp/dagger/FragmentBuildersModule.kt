package com.example.weatherapp.dagger

import com.example.weatherapp.tabs.TodayWeatherFragment
import com.example.weatherapp.tabs.WeeklyWeatherFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
Any fragment that requires injection should be listed here
 */
@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    internal abstract fun contributeTodayWeatherFragment(): TodayWeatherFragment

    @ContributesAndroidInjector
    internal abstract fun contributeWeeklyWeatherFragment(): WeeklyWeatherFragment
}