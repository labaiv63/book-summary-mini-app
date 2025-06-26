import com.test.bookSummaryMiniApp.libs
import com.test.bookSummaryMiniApp.apply
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

@Suppress("unused")
internal class DaggerHiltAndroidConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.findPlugin("ksp").get())
                apply(libs.findPlugin("dagger-hilt-android").get())
            }

            dependencies {
                add("implementation", libs.findLibrary("dagger-hilt-android").get())
                add("ksp", libs.findLibrary("dagger-hilt-android-compiler").get())
            }
        }
    }
}
