include(":app")
include(":mvvm")
include(":common:util")
include(":common:design")

plugins {
    id("com.gradle.enterprise") version ("3.9")
}

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
    }
}