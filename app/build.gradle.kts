import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.kapt)
}

val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties()
if (keystorePropertiesFile.exists()) {
    keystoreProperties.load(FileInputStream(keystorePropertiesFile))
}

android {
    namespace = "com.zezekalo.iou"
    compileSdk = libs.versions.compile.sdk.version.get().toInt()

    defaultConfig {
        applicationId = "com.zezekalo.iou"
        minSdk = libs.versions.min.sdk.version.get().toInt()
        targetSdk = libs.versions.target.sdk.version.get().toInt()
        versionCode = libs.versions.version.code.get().toInt()
        versionName = libs.versions.version.name.get()

        testInstrumentationRunner = "com.zezekalo.iou.app.HiltTestRunner"
    }

    signingConfigs {
        if (keystorePropertiesFile.exists()) {
            create("release") {
                keyAlias = keystoreProperties["keyAlias"] as String
                keyPassword = keystoreProperties["keyPassword"] as String
                storeFile = file(keystoreProperties["storeFile"] as String)
                storePassword = keystoreProperties["storePassword"] as String
            }
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            if (keystorePropertiesFile.exists()) {
                signingConfig = signingConfigs.getByName("release")
            }
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        val javaVersion = libs.versions.jvm.target.version.get()
        sourceCompatibility = JavaVersion.toVersion(javaVersion)
        targetCompatibility = JavaVersion.toVersion(javaVersion)
    }
    kotlinOptions {
        jvmTarget = libs.versions.jvm.target.version.get()
    }
    buildFeatures {
        buildConfig = true
    }
    testOptions.unitTests {
        isReturnDefaultValues = true
        all { tests ->
            tests.testLogging {
                exceptionFormat = TestExceptionFormat.FULL
                events("skipped", "passed", "failed")
                showStandardStreams = true
            }
        }
    }
}

dependencies {
    implementation(project(":presentation"))
    implementation(project(":data"))
    api(project(":domain"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    api(libs.timber)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    testApi(project(":domain"))
    testApi(libs.timber)
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.hamcrest)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.hilt.android.testing)
    kaptAndroidTest(libs.hilt.compiler)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
