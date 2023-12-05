plugins {
    id("java")
    id("org.jetbrains.intellij") version "1.13.3"
}

group = "com.codeshell.intellij"
version = "0.0.3"

repositories {
    mavenCentral()
}

dependencies {
    implementation("cn.hutool:hutool-all:5.8.22")
    implementation("com.alibaba.fastjson2:fastjson2:2.0.41")
}

intellij {
    version.set("2022.2.5")
    type.set("IC")
}

tasks {
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
        options.encoding = "UTF-8"
    }

    patchPluginXml {
        sinceBuild.set("222")
        untilBuild.set("232.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}
