plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.heungjun.gaincontrol"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.heungjun.gaincontrol"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.2"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.firebase.auth)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.7.7")

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")

    implementation("androidx.compose.runtime:runtime-livedata:1.3.0")
    implementation("androidx.compose.runtime:runtime:1.5.1")


    // Compose Material3 라이브러리
    implementation("androidx.compose.material3:material3:1.0.1")

    // Compose Icons 라이브러리 (Material Icons)
    implementation("androidx.compose.material:material-icons-extended:1.4.3")

    implementation("androidx.compose.material:material:1.5.1")
    implementation("androidx.compose.material3:material3:1.2.0") // 최신 안정화 버전 사용

    // 로그인, 비밀번호 필드 아이콘 placeholder
    implementation("androidx.compose.material:material-icons-extended:1.4.3")

    implementation(platform("com.google.firebase:firebase-bom:32.3.1"))

    // Firebase 모듈
    implementation("com.google.firebase:firebase-database-ktx") // Realtime Database
    implementation("com.google.firebase:firebase-firestore:25.1.1")

    //날짜 모듈
    implementation("com.jakewharton.threetenabp:threetenabp:1.4.4")

    // 알림
    implementation("com.google.firebase:firebase-messaging-ktx:24.1.0")
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")

}
