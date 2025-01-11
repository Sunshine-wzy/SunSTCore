plugins {
    java
    id("org.jetbrains.kotlin.jvm") version "1.8.22"

    id("com.github.johnrengelman.shadow") version "5.2.0"
}

group = "io.github.sunshinewzy"
version = "1.2.7"

repositories {
    maven("https://maven.aliyun.com/repository/public/")
    maven("http://sacredcraft.cn:8081/repository/releases") { isAllowInsecureProtocol = true }
    
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.22")
    implementation("org.bstats", "bstats-bukkit", "2.2.1")

    compileOnly("ink.ptms.core:v12001:12001:universal")
    compileOnly("ink.ptms.core:v12001:12001:mapped")

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
        
        relocate("org.bstats.", "io.github.sunshinewzy.sunstcore.libs.bstats.")
        relocate("kotlin.", "kotlin1822s.")
        
//        dependencies { 
//            include(dependency("org.bstats:bstats-base:2.2.1"))
//            include(dependency("org.bstats:bstats-bukkit:2.2.1"))
//        }
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