package com.example.weatherapp.tabs

import android.graphics.drawable.ClipDrawable.HORIZONTAL
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.dagger.Injectable
import com.example.weatherapp.dagger.ViewModelFactory
import com.example.weatherapp.network.Resource
import com.example.weatherapp.network.Status
import com.example.weatherapp.network.WebService
import com.example.weatherapp.viewmodel.WeatherViewModel
import kotlinx.android.synthetic.main.weekly_weather_fragment.*
import kotlinx.android.synthetic.main.weekly_weather_item.view.*
import javax.inject.Inject

class WeeklyWeatherFragment: Fragment(), Injectable {

    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    fun injectViewModelFactory(viewModelFactory: ViewModelFactory) {
        if (this::viewModelFactory.isInitialized) return
        else this.viewModelFactory = viewModelFactory
    }


    private fun onDailyWeather(resource: Resource<WebService.WeeklyWeatherResponse>){
        when (resource.status) {
            Status.LOADING -> {
            }

            Status.ERROR -> {
                Toast.makeText(activity, resource.message, Toast.LENGTH_LONG).show()
            }

            Status.SUCCESS -> {
                city_weekly.text = resource.data?.timezone!!.split("/")[1]
                current_date_weekly.text = getDate(resource.data.current.timeStamp, "EEEE MMMM yy")
                current_weather_weekly.text = resource.data.current.weather[0].main
                current_temp_weekly.text = requireActivity().getString(R.string.current_temp, resource.data.current.temperature.toInt())

                val weeklyItems = mutableListOf<WeeklyItem>()

                for(i in resource.data.daily.indices) {
                    val day = resource.data.daily[i]
                    val timeStamp = day.timeStamp
                    val minTemp = day.temp.min
                    val maxTemp = day.temp.max
                    val weather = day.weather[0].main
                    val weatherDesc = day.weather[0].description

                    val tempRange = requireActivity().getString(R.string.week_day_temp_range_format, minTemp.toInt(), maxTemp.toInt())
                    val weekDay = getDate(timeStamp, "EE")
                    val desc = requireActivity().getString(R.string.week_day_weather_desc_format, weather, weatherDesc)

                    val weekItem =  WeeklyItem(R.mipmap.stormy, weekDay!!, tempRange, desc)
                    weeklyItems.add(i, weekItem)
                }

                val itemAdapter = WeeklyWeatherItemAdapter().also {
                    it.items = weeklyItems.toList()
                }

                with(weekly_recycler) {
                    adapter = itemAdapter
                    val divider = DividerItemDecoration(this@WeeklyWeatherFragment.context, HORIZONTAL)
                    addItemDecoration(divider)
                    adapter?.notifyDataSetChanged()
                }
            }

        }

    }

    private val viewModel: WeatherViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(WeatherViewModel::class.java)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.weekly_weather_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getDailyWeather()
        viewModel.dailyWeatherResult.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            onDailyWeather(it)
        })
    }

    inner class WeeklyWeatherItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(weeklyItem: WeeklyItem) {
            with(itemView) {
                week_day.text = weeklyItem.weekDay
                week_day_temp_range.text = weeklyItem.tempRange
                weekly_item_avatar.setImageResource(weeklyItem.avatar)
                week_day_weather_desc.text = weeklyItem.weatherDesc
            }
        }
    }

    inner class WeeklyWeatherItemAdapter : RecyclerView.Adapter<WeeklyWeatherItemViewHolder>() {
        var items = emptyList<WeeklyItem>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeeklyWeatherItemViewHolder {
            return WeeklyWeatherItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.weekly_weather_item, parent, false))
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onBindViewHolder(holder: WeeklyWeatherItemViewHolder, position: Int) {
            holder.bind(items[position])
        }
    }

}

data class WeeklyItem(val avatar: Int, val weekDay: String, val tempRange: String, val weatherDesc: String)