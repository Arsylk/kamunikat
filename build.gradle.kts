import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("multiplatform") version "1.6.10"
    kotlin("plugin.serialization") version "1.6.10"


    id("org.gretty") version "3.0.6"
    application
    war
}

group = "org.kamunikat"
version = "1.0"

repositories {
    mavenCentral()
}


kotlin {
    jvm {
        withJava()
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
    }
    js(IR) {
        browser {
            commonWebpackConfig {
                cssSupport.enabled = true
                sourceMaps = true
            }
        }
        binaries.executable()
    }
    sourceSets {
        fun kWrapper(name: String, version: String) =
            "org.jetbrains.kotlin-wrappers:kotlin-$name:$version-pre.301-kotlin-1.6.10"

        val coroutinesVersion = "1.6.0"
        val ktorVersion = "1.6.7"

        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.3.2")

                implementation("io.ktor:ktor-client-core:$ktorVersion")

                implementation("io.insert-koin:koin-core:3.1.5")
            }
        }
        val jsMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:$coroutinesVersion")

                implementation("io.ktor:ktor-client-js:$ktorVersion")
                implementation("io.ktor:ktor-client-json:$ktorVersion")
                implementation("io.ktor:ktor-client-serialization:$ktorVersion")

                val reactVersion = "17.0.2"
                implementation(npm("react", reactVersion))
                implementation(npm("react-dom", reactVersion))
                implementation(kWrapper("react", reactVersion))
                implementation(kWrapper("react-dom", reactVersion))
                implementation(kWrapper("react-css", reactVersion))

                implementation(npm("@babel/core", "7.0.0"))
                implementation(npm("@mui/material", "5.4.2"))
                implementation(npm("@mui/icons-material", "5.4.2"))
                implementation(kWrapper("mui", "5.4.2"))
                implementation(kWrapper("mui-icons", "5.4.2"))

                implementation(npm("@emotion/react", "11.7.1"))
                implementation(npm("@emotion/styled", "11.6.0"))

                implementation(npm("react-router-dom", "6.2.1"))
                implementation(kWrapper("react-router-dom", "6.2.1"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:$coroutinesVersion")

                implementation("ch.qos.logback:logback-classic:1.2.10")

                implementation("io.ktor:ktor-server-core:$ktorVersion")
                implementation("io.ktor:ktor-server-tomcat:$ktorVersion")
                implementation("io.ktor:ktor-server-servlet:$ktorVersion")
                implementation("io.ktor:ktor-server-sessions:$ktorVersion")
                implementation("io.ktor:ktor-serialization:$ktorVersion")
                implementation("io.ktor:ktor-html-builder:$ktorVersion")
                implementation("io.ktor:ktor-locations:$ktorVersion")
                implementation("io.ktor:ktor-auth:$ktorVersion")
                implementation("io.ktor:ktor-auth-jwt:$ktorVersion")

                implementation("io.insert-koin:koin-ktor:3.1.5")

                implementation("org.jetbrains.exposed:exposed-core:0.37.3")
                implementation("org.jetbrains.exposed:exposed-dao:0.37.3")
                implementation("org.jetbrains.exposed:exposed-jdbc:0.37.3")
                implementation("org.jetbrains.exposed:exposed-kotlin-datetime:0.37.3")
                implementation("mysql:mysql-connector-java:8.0.28")

                implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.2")
            }
        }
    }
}

application {
    mainClass.set("io.ktor.server.tomcat.EngineMain")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=true")
}

gretty {
    servletContainer = "tomcat9"
    contextPath = "/"
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
}

tasks.getByName<Jar>("jvmJar") {
    val taskName = if (project.properties["isProduction"] == true) "jsBrowserProductionWebpack"
    else "jsBrowserDevelopmentWebpack"
    val webpackTask = tasks.getByName<KotlinWebpack>(taskName)
    dependsOn(webpackTask)
    from(File(webpackTask.destinationDirectory, webpackTask.outputFileName))
}

tasks.named<JavaExec>("run") {
    dependsOn(tasks.named<Jar>("jvmJar"))
    classpath(tasks.named<Jar>("jvmJar"))
}


tasks.war {
    from(tasks.getByName("jsBrowserDistribution")) {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
        into("WEB-INF/classes")
    }

    webAppDirectory.set(file("src/jvmMain/webapp"))
    archiveFileName.set("${project.name}.war")
}

tasks.register<Copy>("dockerDeploy") {
    val deployDir = "${project.rootDir.parent}/docker/tomcat/webapps"
    dependsOn(tasks.war)
    from("build/libs/${project.name}.war") {
        include("${project.name}.war")
        rename { "ROOT.war" }
    }
    into(deployDir)
    doFirst {
        delete("$deployDir/ROOT")
        delete("$deployDir/ROOT.war")
    }
}
