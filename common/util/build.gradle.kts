plugins {
    //Остальные настройки подтягиваются из commonAndroid.gradle.kts
    id("commonAndroid")
}

android {

}

dependencies {
    implementation("androidx.fragment:fragment-ktx:1.5.0")
    implementation(Deps.coroutines)
    implementation(Deps.timber)
}