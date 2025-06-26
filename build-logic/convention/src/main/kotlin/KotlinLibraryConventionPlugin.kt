import com.test.bookSummaryMiniApp.libs
import com.test.bookSummaryMiniApp.apply
import com.test.bookSummaryMiniApp.configureKotlinJvm
import org.gradle.api.Plugin
import org.gradle.api.Project

@Suppress("unused")
internal class KotlinLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.findPlugin("kotlin-jvm").get())
            }

            configureKotlinJvm()
        }
    }
}
