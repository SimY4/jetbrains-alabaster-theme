import org.jetbrains.changelog.Changelog
import org.jetbrains.changelog.date
import org.jetbrains.kotlin.gradle.dsl.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("java") // Java support
  alias(libs.plugins.kotlin) // Kotlin support
  alias(libs.plugins.intelliJPlatform) // IntelliJ Platform Gradle Plugin
  alias(libs.plugins.changelog) // Gradle Changelog Plugin
}

group = providers.gradleProperty("pluginGroup").get()
version = providers.gradleProperty("pluginVersion").get()

repositories {
  mavenCentral()
  intellijPlatform {
    defaultRepositories()
  }
}

dependencies {
  intellijPlatform {
    create(providers.gradleProperty("platformType"), providers.gradleProperty("platformVersion"))
  }
}

intellijPlatform {
  buildSearchableOptions = false

  pluginConfiguration {
    id = providers.gradleProperty("pluginGroup")
    name = providers.gradleProperty("pluginName")
    version = providers.gradleProperty("pluginVersion")

    ideaVersion {
      sinceBuild = providers.gradleProperty("pluginSinceBuild")
    }
  }

  signing {
    certificateChain = providers.environmentVariable("CERTIFICATE_CHAIN")
    privateKey = providers.environmentVariable("PRIVATE_KEY")
    password = providers.environmentVariable("PRIVATE_KEY_PASSWORD")
  }

  publishing {
    token = providers.environmentVariable("PUBLISH_TOKEN")
    // The pluginVersion is based on the SemVer (https://semver.org) and supports pre-release labels, like 2.1.7-alpha.3
    // Specify pre-release label to publish the plugin in a custom Release Channel automatically. Read more:
    // https://plugins.jetbrains.com/docs/intellij/deployment.html#specifying-a-release-channel
    channels = providers.gradleProperty("pluginVersion").map { listOf(it.substringAfter('-', "").substringBefore('.').ifEmpty { "default" }) }
  }

  pluginVerification {
    ides {
      recommended()
    }
  }
}

changelog {
  version.set(providers.gradleProperty("pluginVersion"))
  path.set(file("CHANGELOG.md").canonicalPath)
  header.set(provider { "${version.get()} - ${date()}" })
  headerParserRegex.set("""(\d\.\d+\.\d+)""".toRegex())
  itemPrefix.set("-")
  keepUnreleasedSection.set(true)
  unreleasedTerm.set("[Unreleased]")
  groups.set(listOf("Added", "Changed", "Deprecated", "Removed", "Fixed", "Security"))
}

tasks {
  // Set the JVM compatibility versions
  providers.gradleProperty("javaVersion").get().let {
    withType<JavaCompile> {
      sourceCompatibility = it
      targetCompatibility = it
    }
    withType<KotlinCompile> {
      compilerOptions {
        apiVersion = KotlinVersion.KOTLIN_1_8
        jvmTarget = JvmTarget.fromTarget(it)
      }
    }
  }

  wrapper {
    gradleVersion = providers.gradleProperty("gradleVersion").get()
  }

  patchPluginXml {
    pluginVersion.set(providers.gradleProperty("pluginVersion"))
    sinceBuild.set(providers.gradleProperty("pluginSinceBuild"))
    untilBuild.set(providers.gradleProperty("pluginUntilBuild"))

    // Get the latest available change notes from the changelog file
    changeNotes.set(
      provider { changelog.renderItem(changelog.getLatest(), Changelog.OutputType.HTML) }
    )
  }

  publishPlugin {
    dependsOn(patchChangelog)
  }
}
