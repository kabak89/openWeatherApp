plugins {
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation(kotlin("script-runtime"))
    //should maintain kotlin version
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.0")
    //should maintain gradle version
    implementation("com.android.tools.build:gradle:7.2.2")
}