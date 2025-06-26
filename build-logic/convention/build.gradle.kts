plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.android.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("androidApplicationCompose") {
            id = "book_summary.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = "book_summary.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("androidLibrary") {
            id = "book_summary.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidFeature") {
            id = "book_summary.android.feature"
            implementationClass = "AndroidFeatureConventionPlugin"
        }
        register("daggerHiltCore") {
            id = "book_summary.dagger.hilt.core"
            implementationClass = "DaggerHiltCoreConventionPlugin"
        }
        register("daggerHiltAndroid") {
            id = "book_summary.dagger.hilt.android"
            implementationClass = "DaggerHiltAndroidConventionPlugin"
        }
        register("kotlinLibrary") {
            id = "book_summary.kotlin.library"
            implementationClass = "KotlinLibraryConventionPlugin"
        }
    }
}
