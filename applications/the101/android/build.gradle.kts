plugins {
    id("com.android.application")
    kotlin("android")
}

apply("${project.rootProject.file("gradle/secrets.gradle")}")

repositories {
    google()
    mavenCentral()
    mavenLocal()

    maven {
        url = uri("https://maven.pkg.github.com/pavan2you/kmm-clean-architecture")

        credentials {
            username = extra["githubUser"] as? String
            password = extra["githubToken"] as? String
        }
    }
}

android {
    namespace = "io.verse.app.android"
    compileSdk = 34
    defaultConfig {
        applicationId = "io.verse.app.android"
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        // Deeplink configuration
        val appScheme = "sample"
        val appHost = "sampleApp"
        val firebaseDynamicLinkDomain = "apps.sampleApp.com"
        val deepLinkDomain = "www.sampleApp.com"
        buildConfigField("APP_SCHEME", "$appScheme://")
        buildConfigField("APP_HOST", appHost)
        buildConfigField("FIREBASE_DYNAMIC_LINK_DOMAIN", firebaseDynamicLinkDomain)
        buildConfigField("DEEP_LINK_DOMAIN", deepLinkDomain)
    }
    buildFeatures {
        compose = true
        buildConfig
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.5"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        buildConfig = true
    }
    flavorDimensions += listOf("free", "paid")
    productFlavors {
        create("demo") {
            dimension = "free"
            buildConfigField("String", "FLAVOUR_DIMENSION", "\"free\"")
        }
        create("pro") {
            dimension = "paid"
            buildConfigField("String", "FLAVOUR_DIMENSION", "\"paid\"")
        }
    }
}

dependencies {
    implementation(project(":libraries:app-bundle-core"))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.activity.compose)
    implementation(libs.tagd.android)
    implementation(libs.androidx.appcompat)
}

inline fun <reified ValueT> com.android.build.api.dsl.VariantDimension.buildConfigField(
    name: String,
    value: ValueT
) {
    val resolvedValue = when (value) {
        is String -> "\"$value\""
        else -> value
    }.toString()
    buildConfigField(ValueT::class.java.simpleName, name, resolvedValue)
}