plugins {
    alias(libs.plugins.book.summary.android.library)
    alias(libs.plugins.book.summary.dagger.hilt.android)
}

dependencies {
    implementation(project(":domain"))
}