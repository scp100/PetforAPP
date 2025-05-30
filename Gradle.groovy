// app/build.gradle.kts
plugins {
  id("com.android.application")
  kotlin("android")
  kotlin("kapt")
}

android {
  compileSdk = 33
  defaultConfig {
    applicationId = "com.example.pethabittracker"
    minSdk = 21
    targetSdk = 33
  }
  buildFeatures { compose = true }
  composeOptions { kotlinCompilerExtensionVersion = "1.4.0" }
}

dependencies {
  implementation(project(":core"))
  implementation("androidx.core:core-ktx:1.9.0")
  implementation("androidx.compose.ui:ui:1.3.3")
  implementation("androidx.compose.material:material:1.3.1")
  implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.5.1")
  implementation("androidx.activity:activity-compose:1.6.1")
  implementation("androidx.room:room-runtime:2.4.3")
  kapt("androidx.room:room-compiler:2.4.3")
  // …其他依賴…
}
