plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")
    id("kotlin-kapt")
}

android {

    namespace = "com.learnitevekri"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.learnitevekri"
        minSdk = 24
        targetSdk = 34
        versionCode = 9
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        viewBinding = true
        dataBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //Navigation
    val nav_version = "2.7.5"
    // Kotlin
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")
    // Feature module Support
    implementation("androidx.navigation:navigation-dynamic-features-fragment:$nav_version")
    // Testing Navigation
    androidTestImplementation("androidx.navigation:navigation-testing:$nav_version")
    // Jetpack Compose Integration
    implementation("androidx.navigation:navigation-compose:$nav_version")
    // Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")
    //Retrofit setup
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.github.bumptech.glide:glide:4.15.1")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")
    //ViewModel
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    //MaterialDesign
    implementation("com.google.android.material:material:1.11.0")
    //RecyclerView                                       Nav
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    //Photo requesting
    implementation("androidx.camera:camera-camera2:1.4.0-alpha04")
    implementation("androidx.camera:camera-lifecycle:1.4.0-alpha04")
    //Cookie Jar
    implementation("com.github.franmontiel:PersistentCookieJar:v1.0.1")
    //ViewPager2
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    //Gson
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("org.danilopianini:gson-extras:0.2.1")
    //CardView
    implementation("androidx.cardview:cardview:1.0.0")
    //Progressbar
    implementation("com.github.MackHartley:RoundedProgressBar:3.0.0")
    //Firebase
    implementation(platform("com.google.firebase:firebase-bom:32.7.2"))
    implementation("com.google.firebase:firebase-messaging")
    implementation("com.google.firebase:firebase-analytics")

    //Room
    implementation("androidx.room:room-runtime:2.6.0")
    kapt("androidx.room:room-compiler:2.6.0")
    implementation("androidx.room:room-ktx:2.6.0")
    annotationProcessor ("androidx.room:room-compiler:2.6.0")
}