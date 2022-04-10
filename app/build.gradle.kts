plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {
    compileSdk = 32

    defaultConfig {
        applicationId = "com.test.kabak.openweather"
        minSdk = 21
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
        }
    }

    dataBinding {
        isEnabled = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = freeCompilerArgs + "-Xbackend-threads -2"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.2.0-alpha07"
    }
}

dependencies {
    implementation(project(":mvvm"))

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${InfrastructureVersions.kotlin}")

    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.3")
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    implementation("com.google.android.material:material:1.5.0")

    implementation("com.squareup.retrofit2:retrofit:2.6.2")
    implementation("com.squareup.retrofit2:converter-gson:2.6.2")
    implementation("com.squareup.retrofit2:adapter-rxjava2:2.6.2")
    implementation("com.squareup.retrofit2:converter-scalars:2.6.2")

    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${LibVersions.coroutines}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${LibVersions.coroutines}")

    implementation("com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2")

    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.1")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")

    implementation("androidx.room:room-runtime:${LibVersions.room}")
    implementation("androidx.room:room-ktx:${LibVersions.room}")
    kapt("androidx.room:room-compiler:${LibVersions.room}")

    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")
    implementation("io.reactivex.rxjava2:rxjava:2.2.13")

    implementation("com.squareup.picasso:picasso:2.71828")
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${LibVersions.viewModel}")

    implementation("com.jakewharton.timber:timber:4.7.1")

    implementation("androidx.work:work-runtime-ktx:2.7.1")

    implementation("androidx.core:core-splashscreen:1.0.0-beta02")

    //Compose
    // Integration with activities
    implementation("androidx.activity:activity-compose:1.4.0")
    // Compose Material Design
    implementation("androidx.compose.material:material:1.1.1")
    // Animations
    implementation("androidx.compose.animation:animation:1.1.1")
    // Tooling support (Previews, etc.)
    implementation("androidx.compose.ui:ui-tooling:1.1.1")
    // Integration with ViewModels
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.4.1")
    // When using a MDC theme
    implementation("com.google.android.material:compose-theme-adapter:1.1.6")

    implementation("androidx.compose.runtime:runtime-livedata:1.2.0-alpha07")
    //SwipeToRefresh
    implementation("com.google.accompanist:accompanist-swiperefresh:0.24.4-alpha")
    //Image loading
    implementation("io.coil-kt:coil:2.0.0-rc01")
    implementation("io.coil-kt:coil-compose:2.0.0-rc01")
    //Compose
}