import io.pebbletemplates.pebble.PebbleEngine
import io.pebbletemplates.pebble.loader.FileLoader
import org.jetbrains.changelog.*
import org.jetbrains.kotlin.gradle.dsl.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
  repositories {
    mavenCentral()
  }

  dependencies {
    classpath(libs.pebble)
  }
}

plugins {
  id("java")
  alias(libs.plugins.kotlin)
  alias(libs.plugins.intelliJPlatform)
  alias(libs.plugins.changelog)
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
    intellijIdea(providers.gradleProperty("platformVersion"))
    bundledPlugins("org.jetbrains.kotlin", "com.intellij.java")
    plugins(
      "com.cursiveclojure.cursive:2025.2-252",
      "com.jetbrains.rust:252.27397.133",
      "JavaScript:252.27397.106",
      "org.intellij.scala:2025.2.30",
      "org.jetbrains.plugins.go:252.26830.24",
      "PsiViewer:252.23892.248",
      "PythonCore:252.27397.103"
    )
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

tasks.register("generate") {
  notCompatibleWithConfigurationCache("Pebble classes are not serializable")

  doLast {
    val engine = PebbleEngine.Builder().loader(FileLoader().apply {
      prefix = "src/main/templates/"
      suffix = ".pebble"
    }).build()
    val themeTemplate = engine.getTemplate("theme")
    val schemeTemplate = engine.getTemplate("alabaster")

    val themesDir = file("src/main/resources/themes")
    if (!themesDir.exists()) {
      themesDir.mkdirs()
    }

    listOf("light", "dark").forEach { variant ->
      listOf("", "bg").forEach { flavour ->
        if (variant == "dark" && flavour == "bg") return@forEach

        val fileSuffix = listOf(variant, flavour).joinToString("") {
          when (it) {
            "dark" -> "-dark"
            "bg" -> "-bg"
            else -> ""
          }
        }

        val context = mapOf(
          "variant" to variant,
          "flavour" to flavour
        )
        schemeTemplate.evaluate(file("src/main/resources/themes/alabaster$fileSuffix.xml").writer(), context)
        themeTemplate.evaluate(file("src/main/resources/themes/alabaster$fileSuffix.theme.json").writer(), context)
      }
    }
  }
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

  buildPlugin {
    dependsOn("generate")
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
