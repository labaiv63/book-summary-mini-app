import com.android.build.api.dsl.LibraryExtension

import com.test.bookSummaryMiniApp.configureCompileOptions
import com.test.bookSummaryMiniApp.configureKotlinAndroid
import com.test.bookSummaryMiniApp.libs
import com.test.bookSummaryMiniApp.apply
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

@Suppress("unused")
internal class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.findPlugin("android-library").get())
                apply(libs.findPlugin("kotlin-android").get())
            }

            extensions.getByType<LibraryExtension>().apply {
                configureCompileOptions()
                configureKotlinAndroid(this)
            }
        }
    }
}
