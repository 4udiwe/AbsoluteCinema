plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
    id("org.jetbrains.dokka") version "1.9.10"
}

tasks.dokkaHtmlPartial.configure {
    outputDirectory.set(file("$buildDir/dokka/domain")) // Указываем директорию для вывода
    dokkaSourceSets {
        named("main") {
            sourceRoots.from("src/main/java") // Указываем путь к исходникам
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}

dependencies{
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")
}