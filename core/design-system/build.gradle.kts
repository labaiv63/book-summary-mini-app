plugins {
    alias(libs.plugins.book.summary.android.library.compose)
}

dependencies {
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.foundation)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}