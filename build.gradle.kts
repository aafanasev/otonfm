// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    configurations.classpath {
        // The sekret plugin publishes a strict constraint pinning org.jetbrains:annotations to 13.0
        // (an artifact of being built with Gradle's kotlin-dsl plugin). AGP 9 needs 23.0.0, so the
        // two are irreconcilable without forcing. Remove once sekret is rebuilt on a newer Gradle.
        resolutionStrategy { force("org.jetbrains:annotations:23.0.0") }
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.firebase) apply false
    alias(libs.plugins.crashlytics) apply false
    alias(libs.plugins.sekret) apply false
}