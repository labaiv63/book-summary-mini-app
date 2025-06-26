import com.test.bookSummaryMiniApp.libs
import com.test.bookSummaryMiniApp.apply
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

@Suppress("unused")
internal class DaggerHiltCoreConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply(libs.findPlugin("ksp").get())

            dependencies {
                add("implementation", libs.findLibrary("dagger-hilt-core").get())
                add("ksp", libs.findLibrary("dagger-hilt-compiler").get())
            }
        }
    }
}
