plugins {
    java
    id("org.jetbrains.kotlin.jvm") version "1.5.10"

    id("com.github.johnrengelman.shadow") version "5.2.0"
}

group = "io.github.sunshinewzy"
version = "1.2.2"

repositories {
    maven {
        url = uri("https://maven.aliyun.com/repository/public/")
    }
    mavenLocal()
    mavenCentral()
}

dependencies {
    compileOnly(kotlin("stdlib"))
    implementation("org.bstats", "bstats-bukkit", "2.2.1")

    compileOnly(fileTree(mapOf("dir" to "cores", "include" to listOf("*.jar"))))
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks {
    jar {
        destinationDirectory.set(file("build/core"))
        
    }
    
    shadowJar {
        archiveClassifier.set("")
        destinationDirectory.set(file("build/core"))
        
        relocate("org.bstats", "io.github.sunshinewzy.sunstcore.libs.bstats")
        
        dependencies { 
            include(dependency("org.bstats:bstats-base:2.2.1"))
            include(dependency("org.bstats:bstats-bukkit:2.2.1"))
        }
    }

    compileKotlin {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }

    compileTestKotlin {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }

    processResources {
        filesMatching("plugin.yml") {
            expand(
                "name" to rootProject.name,
                "main" to "${project.group}.sunstcore.SunSTCore",
                "version" to project.version
            )
        }
    }
}