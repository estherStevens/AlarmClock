// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
//    kotlin("plugin.serialization") version "2.0.21"
    alias(libs.plugins.kotlin.serialize) apply true
    alias(libs.plugins.ksp) apply true
//    alias(libs.plugins.ksp) apply true
}

