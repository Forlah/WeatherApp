package com.example.weatherapp.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.dagger.ViewModelFactory
import com.example.weatherapp.R
import com.example.weatherapp.dagger.Injectable
import com.example.weatherapp.network.Resource
import com.example.weatherapp.network.Status
import com.example.weatherapp.network.WebService
import com.example.weatherapp.utils.ItemSpacingDecorator
import com.example.weatherapp.viewmodel.WeatherViewModel
import kotlinx.android.synthetic.main.today_weather_fragment.*
import kotlinx.android.synthetic.main.today_weather_item.view.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class TodayWeatherFragment : Fragment(), Injectable {

    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: WeatherViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(WeatherViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.today_weather_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getTodayWeather()
        viewModel.todayWeatherResult.observe(viewLifecycleOwner, Observer {
            onTodayWeatherFound(it)
        })
    }

    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(todayItem: TodayItem, position: Int) {
            if (position == 0) {
                itemView.item_today.visibility = View.VISIBLE
            }
            itemView.item_title.text = todayItem.title
            itemView.item_subtitle.text = todayItem.subTitle
            itemView.item_avatar.setImageResource(todayItem.avatar)
        }
    }

    inner class ItemAdapter : RecyclerView.Adapter<ItemViewHolder>() {
        var items = emptyList<TodayItem>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            return ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.today_weather_item, parent, false))
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            holder.bind(items[position], position)
        }
    }

    @Inject
    fun injectViewModelFactory(viewModelFactory: ViewModelFactory) {
        if (this::viewModelFactory.isInitialized) return
        else this.viewModelFactory = viewModelFactory
    }

    private fun onTodayWeatherFound(resource: Resource<WebService.TodayWeatherResponse>) {
        when (resource.status) {
            Status.LOADING -> {
            }

            Status.ERROR -> {
                Toast.makeText(activity, resource.message, Toast.LENGTH_LONG).show()
            }

            Status.SUCCESS -> {
                current_temp.text = requireActivity().getString(R.string.current_temp, resource.data?.main?.temp?.toInt())
                current_weather.text = resource.data?.weather!![0].main
                city.text = resource.data.name
                current_date.text = getDate(resource.data.timeStamp, "EEEE MMMM yy")
                val items = listOf(TodayItem(R.mipmap.thermometer, "Feels Like", requireActivity().getString(R.string.item_temp_format, resource.data.main.feelsLike.toInt().toString())),
                        TodayItem(R.mipmap.humidity, "Humidity", requireActivity().getString(R.string.humidity_format, resource.data.main.humidity)),
                        TodayItem(R.mipmap.wind, "Wind", requireActivity().getString(R.string.wind_speed_format, resource.data.wind.speed.toInt())),
                        TodayItem(R.mipmap.sun, "UV Index", "7"))

                val itemAdapter = ItemAdapter().also {
                    it.items = items
                }

                with(recycler_) {
                    adapter = itemAdapter
                    addItemDecoration(ItemSpacingDecorator(resources.getDimensionPixelSize(R.dimen.spacing), 2))
                    adapter?.notifyDataSetChanged()
                }
            }

        }
    }

}

fun getDate(s: Long, pattern: String): String? {
    return try {
        val sdf = SimpleDateFormat(pattern)
        val netDate = Date(s * 1000)
        sdf.format(netDate)
    } catch (e: Exception) {
        e.toString()
    }
}

data class TodayItem(val avatar: Int, val title: String, val subTitle: String)