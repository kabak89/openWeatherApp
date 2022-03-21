package com.test.kabak.openweather.ui.list

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.format.DateUtils
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProviders
import coil.compose.AsyncImage
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.test.kabak.openweather.R
import com.test.kabak.openweather.core.network.Constants
import com.test.kabak.openweather.core.storage.City
import com.test.kabak.openweather.core.storage.CurrentWeather
import com.test.kabak.openweather.ui.addCity.AddCityActivity
import com.test.kabak.openweather.ui.common.MyTheme
import com.test.kabak.openweather.ui.common.dividerColor
import com.test.kabak.openweather.ui.common.textColor
import com.test.kabak.openweather.ui.forecast.ForecastActivity

class ListActivity : AppCompatActivity() {
    private lateinit var viewModel: CitiesListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(CitiesListViewModel::class.java)

        setContent {
            MyTheme {
                MyScreen()
            }
        }

        viewModel.errorsLiveData.observe(this) { event ->
            event?.getContentIfNotHandled()?.let {
//                ErrorInteractor.handleError(it, this@ListActivity, binding.root)
            }
        }
    }

    @Composable
    @Preview
    private fun MyScreen(
    ) {
        val state by viewModel.stateLiveData.observeAsState()

        val stateToWork = state
        stateToWork ?: return

        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        val intent = Intent(this@ListActivity, AddCityActivity::class.java)
                        ActivityCompat.startActivity(this@ListActivity, intent, null)
                    },
                    backgroundColor = MaterialTheme.colors.primary,
                    content = {
                        Icon(
                            painter = painterResource(id = android.R.drawable.ic_menu_add),
                            contentDescription = null,
                            tint = androidx.compose.ui.graphics.Color.White
                        )
                    }
                )
            },
            content = {
                CitiesList(stateToWork)
            }
        )
    }

    @Composable
    private fun CitiesList(stateToWork: CitiesListViewModel.State) {
        SwipeRefresh(
            state = rememberSwipeRefreshState(stateToWork.isLoading),
            onRefresh = { viewModel.loadData() },
        ) {
            LazyColumn(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                items(items = stateToWork.cities, itemContent = {
                    CityItem(it)
                    Divider(color = MaterialTheme.colors.dividerColor)
                })
            }
        }
    }

    @Composable
    @Preview
    private fun CityItem(@PreviewParameter(CitiItemProvider::class) item: ListWeatherObject) {
        Box(modifier = Modifier
            .clickable { goDetails(item) }) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Column {
                    val currentWeather = item.currentWeather

                    Row {
                        Column(modifier = Modifier.weight(weight = 1f)) {
                            Text(
                                text = item.city.name,
                                style = MaterialTheme.typography.body1,
                                color = MaterialTheme.colors.textColor,
                            )

                            if (currentWeather == null) {
                                Text(
                                    text = getString(R.string.error_forecast_not_available),
                                    style = MaterialTheme.typography.body2,
                                    color = MaterialTheme.colors.textColor,
                                )
                            } else {
                                val text = formatShortDate(currentWeather)
                                Text(
                                    text = text,
                                    style = MaterialTheme.typography.body2,
                                    color = MaterialTheme.colors.textColor,
                                )
                            }
                        }
                        if (currentWeather != null) {
                            Column(modifier = Modifier.width(110.dp)) {
                                Row(modifier = Modifier.width(intrinsicSize = IntrinsicSize.Min)) {
                                    AsyncImage(
                                        modifier = Modifier
                                            .width(40.dp)
                                            .height(40.dp),
                                        model = Uri.parse("${Constants.IMAGES_BASE_URL}${currentWeather.icon}.png"),
                                        contentDescription = null,
                                    )

                                    Text(
                                        modifier = Modifier.width(40.dp),
                                        text = String.format("%.0f", currentWeather.minT)
                                    )
                                }
                                if (currentWeather.description != null) {
                                    Text(text = currentWeather.description)
                                }
                                Text(
                                    text = getString(
                                        R.string.wind_speed_template,
                                        currentWeather.windSpeed.toString()
                                    )
                                )
                            }
                        }
                    }

                    if (currentWeather == null || currentWeather.isOutdated()) {
                        Text(getString(R.string.error_forecast_is_outdated))
                    }
                }
            }
        }
    }

    private fun goDetails(item: ListWeatherObject) {
        val intent = Intent(this@ListActivity, ForecastActivity::class.java)
        val bundle = Bundle()
        bundle.putString(CITY_ID_KEY, item.city.cityId)
        intent.putExtras(bundle)
        ActivityCompat.startActivity(this@ListActivity, intent, null)
    }

    private fun formatShortDate(currentWeather: CurrentWeather): String {
        val time = DateUtils.formatDateTime(
            this@ListActivity,
            currentWeather.timestamp,
            DateUtils.FORMAT_SHOW_TIME
        )
        val date = DateUtils.formatDateTime(
            this@ListActivity,
            currentWeather.timestamp,
            DateUtils.FORMAT_NUMERIC_DATE
        )
        return "$date $time"
    }

    class CitiItemProvider : PreviewParameterProvider<ListWeatherObject> {
        override val values = listOf(
            ListWeatherObject(
                city = City(cityId = "1", name = "Novosibirsk"),
                currentWeather = CurrentWeather(
                    cityId = "",
                    minT = -10f,
                    maxT = 10f,
                    description = "snow",
                    icon = null,
                    timestamp = 123L,
                    windSpeed = 10f
                ),
            ),
            ListWeatherObject(
                city = City(cityId = "1", name = "Moscow"),
                currentWeather = null,
            )
        ).asSequence()
    }

    override fun onStart() {
        super.onStart()

        viewModel.loadData()
    }

    companion object {
        const val CITY_ID_KEY = "CITY_ID_KEY"
    }
}
