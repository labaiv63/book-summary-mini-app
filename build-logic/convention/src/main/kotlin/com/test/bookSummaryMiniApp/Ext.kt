package com.test.bookSummaryMiniApp

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.plugins.PluginManager
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.plugin.use.PluginDependency

internal val JAVA_VERSION = JavaVersion.VERSION_21

internal val Project.libs
    get(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

internal fun PluginManager.apply(plugin: Provider<PluginDependency>) {
    apply(plugin.get().pluginId)
}

internal fun Project.configureAndroidCompose(commonExtension: CommonExtension<*, *, *, *, *, *>) {
    commonExtension.apply {
        pluginManager.apply(libs.findPlugin("kotlin-compose").get())

        buildFeatures {
            compose = true
        }

        dependencies {
            add("implementation", platform(libs.findLibrary("androidx-compose-bom").get()))
            add("implementation", libs.findLibrary("androidx-ui-tooling-preview").get())
            add("debugImplementation", libs.findLibrary("androidx-ui-tooling").get())
        }
    }
}

internal fun CommonExtension<*, *, *, *, *, *>.configureCompileOptions(javaVersion: JavaVersion = JAVA_VERSION) {
    compileOptions {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }
}

internal fun Project.configureKotlinAndroid(extension: CommonExtension<*, *, *, *, *, *>) =
    extension.apply {
        val moduleName = path.split(":").drop(2).joinToString(".").replace("-", ".")
        namespace = buildString {
            append("com.test.bool_summary_mini_app")
            if (moduleName.isNotEmpty()) append(".$moduleName")
        }
        compileSdk = libs.findVersion("compile-sdk").get().requiredVersion.toInt()
        defaultConfig {
            minSdk = libs.findVersion("min-sdk").get().requiredVersion.toInt()
        }
    }

internal fun Project.configureKotlinJvm(javaVersion: JavaVersion = JAVA_VERSION) {
    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }
}
