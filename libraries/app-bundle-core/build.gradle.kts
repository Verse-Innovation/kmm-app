plugins {
    id("io.verse.kmm.library")
}

apply("${project.rootProject.file("gradle/github_repo_access.gradle")}")

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(libs.tagd.arch)
                api(libs.verse.storage)
                api(libs.verse.system.core)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.tagd.arch.test)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.androidx.core.ktx)
                implementation(libs.play.services.ads.identifier)
            }
        }
        val androidInstrumentedTest by getting {
            dependencies {
                implementation(libs.mockito.android)
            }
        }
    }
}

android {
    namespace = "io.verse.app.core"
}

pomBuilder {
    description.set("App Core library")
}