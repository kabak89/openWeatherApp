package com.test.kabak.openweather.presentation.list

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import coil.compose.AsyncImage
import com.example.mvvm.model.BaseViewEvent
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.test.kabak.openweather.R
import com.test.kabak.openweather.core.network.Constants
import com.test.kabak.openweather.ui.addCity.AddCityActivity
import com.test.kabak.openweather.ui.common.MyTheme
import com.test.kabak.openweather.ui.common.dividerColor
import com.test.kabak.openweather.ui.common.textColor
import com.test.kabak.openweather.ui.forecast.ForecastActivity
import com.test.kabak.openweather.util.getPrintableText
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.getViewModel

class CitiesListActivity : ComponentActivity() {
    private val vm by lazy(mode = LazyThreadSafetyMode.NONE) {
        getViewModel<CitiesListViewModel>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            vm.viewEvent.flowWithLifecycle(lifecycle = lifecycle, Lifecycle.State.STARTED)
                .collect {
                    when (it) {
                        is BaseViewEvent.ScreenEvent -> handleEvent(it.event)
                    }
                }
        }

        setContent {
            MyTheme {
                MyScreen()
            }
        }
    }

    @Composable
    private fun MyScreen() {
        val state by vm.viewState.collectAsState()

        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        val intent = Intent(this@CitiesListActivity, AddCityActivity::class.java)
                        ActivityCompat.startActivity(this@CitiesListActivity, intent, null)
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
                CitiesList(state)
            }
        )
    }

    @Composable
    private fun CitiesList(
        state: CitiesListState,
    ) {
        SwipeRefresh(
            state = rememberSwipeRefreshState(state.isLoading),
            onRefresh = { vm.loadData() },
        ) {
            LazyColumn(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                items(items = state.cities, itemContent = {
                    CityItem(it)
                    Divider(color = MaterialTheme.colors.dividerColor)
                })
            }
        }
    }

    @Composable
    private fun CityItem(item: WeatherItem) {
        Box(modifier = Modifier
            .clickable { goDetails(item) }
            .fillMaxWidth()
            .wrapContentHeight()
        ) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Column {
                    val weather = item.weather

                    Row {
                        Column(modifier = Modifier.weight(weight = 1f)) {
                            Text(
                                text = this@CitiesListActivity.getPrintableText(item.cityName),
                                style = MaterialTheme.typography.body1,
                                color = MaterialTheme.colors.textColor,
                            )

                            if (weather == null) {
                                Text(
                                    text = getString(R.string.error_forecast_not_available),
                                    style = MaterialTheme.typography.body2,
                                    color = MaterialTheme.colors.textColor,
                                )
                            } else {
                                Text(
                                    text = baseContext.getPrintableText(weather.updateTime),
                                    style = MaterialTheme.typography.body2,
                                    color = MaterialTheme.colors.textColor,
                                )
                            }
                        }

                        if (weather != null) {
                            Column(modifier = Modifier.width(110.dp)) {
                                Row(modifier = Modifier.width(intrinsicSize = IntrinsicSize.Min)) {
                                    AsyncImage(
                                        modifier = Modifier
                                            .width(40.dp)
                                            .height(40.dp),
                                        model = "${Constants.IMAGES_BASE_URL}${weather.iconUrl}.png",
                                        contentDescription = null,
                                    )

                                    Text(
                                        modifier = Modifier.width(40.dp),
                                        text = String.format("%.0f", weather.minT)
                                    )
                                }

                                Text(text = weather.description)

                                Text(
                                    text = getString(
                                        R.string.wind_speed_template,
                                        weather.windSpeed.toString()
                                    )
                                )
                            }
                        }
                    }

                    if (weather == null || item.isOutdated) {
                        Text(getString(R.string.error_forecast_is_outdated))
                    }
                }
            }
        }
    }

    private fun goDetails(item: WeatherItem) {
        val intent = Intent(this@CitiesListActivity, ForecastActivity::class.java)
        val bundle = Bundle()
        bundle.putString(CITY_ID_KEY, item.cityId)
        intent.putExtras(bundle)
        ActivityCompat.startActivity(this@CitiesListActivity, intent, null)
    }

    private fun handleEvent(event: CitiesListEvent) {
        when (event) {
            is CitiesListEvent.ShowToast -> Toast.makeText(
                this,
                this.getPrintableText(event.message),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    companion object {
        const val CITY_ID_KEY = "CITY_ID_KEY"
    }
}