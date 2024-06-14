import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply(false)
    alias(libs.plugins.android.library) apply(false)
    alias(libs.plugins.jetbrains.kotlin.android) apply(false)
    alias(libs.plugins.jetbrains.kotlin.jvm) apply(false)
    alias(libs.plugins.hilt.android) apply(false)
    alias(libs.plugins.kotlin.kapt) apply(false)
    alias(libs.plugins.androidx.navigation.safeargs) apply(false)
    alias(libs.plugins.kotlin.parcelize) apply(false)
    alias(libs.plugins.ktlint) apply(false)
}

subprojects {
    apply(plugin = rootProject.libs.plugins.ktlint.get().pluginId)

    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        version.set("1.3.0")
        debug.set(true)
        verbose.set(true)
        android.set(true)
        outputToConsole.set(true)
        outputColorName.set("RED")
        ignoreFailures.set(true)
        enableExperimentalRules.set(true)
        reporters {
            reporter(ReporterType.PLAIN)
            reporter(ReporterType.CHECKSTYLE)
        }
        filter {
            exclude("**/generated/**")
            include("**/kotlin/**")
        }
    }
}
