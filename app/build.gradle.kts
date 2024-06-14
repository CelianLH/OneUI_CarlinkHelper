plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.baidu.BaiduMap"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.baidu.BaiduMap"
        minSdk = 23
        targetSdk = 34
        versionCode = 10000
        versionName = "1.0"
        multiDexEnabled = true
        //vectorDrawables.useSupportLibrary = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        buildConfig = true
        viewBinding = true
    }
    lint {
        abortOnError = false
        checkReleaseBuilds = false
        //baseline = file("lint-baseline.xml")
    }
    packaging {
        resources {
            excludes += "/META-INF/*"
            excludes += "/META-INF/*"
            excludes += "/META-INF/com/**"
            excludes += "/META-INF/versions/**"
            excludes += "/org/bouncycastle/**"
            excludes += "/kotlin/**"
            excludes += "/kotlinx/**"
            excludes += "/okhttp3/**"
            excludes += "/*.txt"
            excludes += "/*.bin"
            excludes += "/*.json"
        }
    }
}

configurations.all {

}

configurations.configureEach {
    exclude("androidx.appcompat", "appcompat")
    exclude("androidx.fragment", "fragment")
    exclude("androidx.core",  "core")
    exclude("androidx.core",  "core-ktx")
    exclude("androidx.customview",  "customview")
    exclude("androidx.viewpager",  "viewpager")
    exclude("androidx.drawerlayout",  "drawerlayout")
    exclude("androidx.viewpager",  "viewpager")
    exclude("androidx.viewpager2",  "viewpager2")
    exclude("androidx.coordinatorlayout",  "coordinatorlayout")
    exclude("androidx.recyclerview",  "recyclerview")
    exclude("androidx.swiperefreshlayout",  "swiperefreshlayout")
}

dependencies {
    implementation("io.github.oneuiproject:design:1.2.6")
    implementation("io.github.oneuiproject.sesl:appcompat:1.4.0")
    implementation("io.github.oneuiproject.sesl:material:1.5.0")
    implementation("io.github.oneuiproject.sesl:preference:1.1.0")
    implementation("io.github.oneuiproject.sesl:recyclerview:1.4.1")
    implementation("io.github.oneuiproject.sesl:swiperefreshlayout:1.0.0")
    implementation("io.github.oneuiproject.sesl:viewpager:1.1.0")
    implementation("io.github.oneuiproject.sesl:viewpager2:1.1.0")



    implementation("io.github.oneuiproject.sesl:apppickerview:1.0.0")
    implementation("io.github.oneuiproject.sesl:indexscroll:1.0.3")
    implementation("io.github.oneuiproject.sesl:picker-basic:1.2.0")
    implementation("io.github.oneuiproject.sesl:picker-color:1.1.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")


    testImplementation("junit:junit:4.13.2")
    implementation("org.greenrobot:eventbus:3.3.1")
    implementation("org.jsoup:jsoup:1.17.2")
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor("com.github.bumptech.glide:compiler:4.15.1")
    implementation("com.google.code.findbugs:jsr305:3.0.2")
    implementation("com.github.getActivity:XXPermissions:18.63")
    implementation("io.github.oneuiproject:icons:1.1.0")

    implementation("com.airbnb.android:lottie:6.3.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("androidx.datastore:datastore-preferences:1.1.1")
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.1")
    implementation("com.google.dagger:hilt-android:2.42")
    implementation("com.github.tiann:FreeReflection:3.1.0")
}