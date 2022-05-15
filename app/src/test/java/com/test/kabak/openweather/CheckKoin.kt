/**
 * Created by Eugeny Kabak on 15.05.2022
 */
package com.test.kabak.openweather

import android.content.Context
import com.test.kabak.openweather.di.appModule
import io.mockk.mockkClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.experimental.categories.Category
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import org.koin.test.category.CheckModuleTest
import org.koin.test.check.checkModules
import org.koin.test.mock.MockProvider

@Category(CheckModuleTest::class)
@OptIn(ExperimentalCoroutinesApi::class)
class CheckKoin {

    @get:Rule
    val coroutineDispatchersRule = object : TestWatcher() {

        val dispatcher = StandardTestDispatcher()

        override fun starting(description: Description?) {
            Dispatchers.setMain(dispatcher)
        }

        override fun finished(description: Description?) {
            Dispatchers.resetMain()
        }
    }

    private val testModule = module {}

    @Before
    fun setUp() {
        MockProvider.register { clazz ->
            mockkClass(clazz)
        }
    }

    @Test
    fun checkAppModule() {
        koinApplication {
            modules(appModule + testModule)
            checkModules {
                withInstance<Context>()
            }
        }
    }
}