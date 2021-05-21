/*
 * Copyright (c) 2021. Herman Cheung
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



rootProject.name = "ccc"

/**
 * The pluginManagement block is need to load plugin from maven local repository.
 * it has to be the first block in settings.gradle.kts
 * When all plugins needed are published to Gradle Plugin Portal, this block can be
 * commented.
 */
pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        jcenter()
        gradlePluginPortal()
    }
}

plugins {
    id("de.fayard.refreshVersions") version "0.10.0"
}
