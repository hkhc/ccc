import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
//import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    java
    kotlin("jvm")
    id("io.hkhc.jarbird")
}

repositories {
    mavenCentral()
    jcenter()
    google()
}

/*
 It is needed to make sure every version of java compiler to generate same kind of bytecode.
 Without it and build this with java 8+ compiler, then the project build with java 8
 will get error like this:
   > Unable to find a matching variant of <your-artifact>:
      - Variant 'apiElements' capability <your-artifact>:
          - Incompatible attributes:
              - Required org.gradle.jvm.version '8' and found incompatible value '13'.
              - Required org.gradle.usage 'java-runtime' and found incompatible value 'java-api'.
              ...
 */
java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks {

    /*
    Without this Kotlin generates java 6 bytecode, which is hardly fatal.
     */
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

//    dokka {
//        outputFormat = "html"
//        outputDirectory = "$buildDir/dokka"
//    }
}

jarbird {
    mavenCentral()

}

dependencies {
    implementation(Kotlin.stdlib.jdk8)
    implementation("io.hkhc.log:ihlog:_")
    testImplementation(Testing.junit4)
    testImplementation("org.assertj:assertj-core:_")
}
