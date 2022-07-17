@file:Suppress("UnstableApiUsage")

plugins {
    `android-library` apply false
    kotlin("android") apply false
    `kotlin-parcelize` apply false
}

android {
    compileSdk = InfrastructureVersions.compileSdk
    buildToolsVersion = InfrastructureVersions.buildToolsVersion

    defaultConfig {
        minSdk = InfrastructureVersions.minSdk
        targetSdk = InfrastructureVersions.targetSdk
        consumerProguardFile("consumer-rules.pro")

        buildFeatures.buildConfig = false

        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf("room.schemaLocation" to "$projectDir/schemas")
            }
        }
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(Deps.kotlin)
}