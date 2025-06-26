import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import com.test.bookSummaryMiniApp.configureAndroidCompose
import com.test.bookSummaryMiniApp.configureCompileOptions
import com.test.bookSummaryMiniApp.configureKotlinAndroid
import com.test.bookSummaryMiniApp.libs
import com.test.bookSummaryMiniApp.apply

@Suppress("unused")
internal class AndroidApplicationComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.findPlugin("android-application").get())
                apply(libs.findPlugin("kotlin-android").get())
                apply(DaggerHiltAndroidConventionPlugin::class.java)
            }

            extensions.getByType<ApplicationExtension>().apply {
                configureCompileOptions()
                configureAndroidCompose(this)
                configureKotlinAndroid(this)
            }
        }
    }
}
