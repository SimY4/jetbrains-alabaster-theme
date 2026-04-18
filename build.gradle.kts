import io.pebbletemplates.pebble.PebbleEngine
import io.pebbletemplates.pebble.loader.FileLoader
import org.jetbrains.changelog.*
import org.jetbrains.intellij.platform.gradle.TestFrameworkType

buildscript {
  repositories {
    mavenCentral()
  }

  dependencies {
    classpath(libs.pebble)
  }
}

plugins {
  id("org.jetbrains.kotlin.jvm")
  id("org.jetbrains.intellij.platform")
  id("org.jetbrains.changelog")
}

dependencies {
  intellijPlatform {
    intellijIdea("2025.2.6.1")
    bundledPlugins("com.intellij.java", "org.jetbrains.kotlin", "org.jetbrains.plugins.yaml")
    plugins(
      "com.cursiveclojure.cursive:2025.2-252",
      "com.jetbrains.rust:252.27397.133",
      "JavaScript:252.27397.106",
      "org.intellij.scala:2025.2.30",
      "org.jetbrains.plugins.go:252.26830.24",
      "PsiViewer:252.23892.248",
      "PythonCore:252.27397.103"
    )
    testFramework(TestFrameworkType.Platform)
  }
}

intellijPlatform {
  buildSearchableOptions = false

  pluginConfiguration {
    description = providers.fileContents(layout.projectDirectory.file("README.md")).asText.map {
      val start = "<!-- Plugin description -->"
      val end = "<!-- Plugin description end -->"

      with(it.lines()) {
        if (!containsAll(listOf(start, end))) {
          throw GradleException("Plugin description section not found in README.md:\n$start ... $end")
        }
        subList(indexOf(start) + 1, indexOf(end)).joinToString("\n").let(::markdownToHTML)
      }
    }

    ideaVersion {
      sinceBuild = "231"
    }

    val changelog = project.changelog // local variable for configuration cache compatibility
    // Get the latest available change notes from the changelog file
    changeNotes = version.map { pluginVersion ->
      with(changelog) {
        renderItem(
          (getOrNull(pluginVersion) ?: getUnreleased())
            .withHeader(false)
            .withEmptySections(false),
          Changelog.OutputType.HTML,
        )
      }
    }
  }
}

changelog {
  groups.empty()
  repositoryUrl = providers.gradleProperty("pluginRepositoryUrl")
  versionPrefix = ""
}

tasks.register("generate") {
  notCompatibleWithConfigurationCache("Pebble classes are not serializable")

  inputs.dir("src/main/templates")
  outputs.dir("src/main/resources/themes")

  doLast {
    val engine = PebbleEngine.Builder()
      .strictVariables(true)
      .loader(FileLoader(file("src/main/templates/").absolutePath).apply { suffix = ".pebble" })
      .build()
    val themeTemplate = engine.getTemplate("theme")
    val schemeTemplate = engine.getTemplate("alabaster")

    val themesDir = file("src/main/resources/themes")
    if (!themesDir.exists()) {
      themesDir.mkdirs()
    }

    listOf("bg", "dark", "mono", "mono-dark", "").forEach { variant ->
      val nameSuffix = when (variant) {
        "" -> ""
        "bg" -> " BG"
        else -> variant.split('-').joinToString(" ", prefix = " ") { word -> word.replaceFirstChar { it.titlecaseChar() } }
      }
      val fileSuffix = if (variant.isEmpty()) variant else "-$variant"
      val background = when (variant) {
        "" -> "F7F7F7"
        "bg", "mono" -> "FFFFFF"
        "mono-dark" -> "111111"
        "dark" -> "0E1415"
        else -> throw IllegalArgumentException("Unknown variant: $variant")
      }

      val context = mapOf("variant" to variant, "name_suffix" to nameSuffix, "file_suffix" to fileSuffix, "primary_background" to background)
      file("src/main/resources/themes/alabaster$fileSuffix.xml").writer().use { schemeTemplate.evaluate(it, context) }
      file("src/main/resources/themes/alabaster$fileSuffix.theme.json").writer().use { themeTemplate.evaluate(it, context) }
    }
  }
}

tasks {
  clean {
    delete("src/main/resources/themes")
  }

  processResources {
    dependsOn("generate")
  }

  wrapper {
    gradleVersion = "9.4.1"
  }

  publishPlugin {
    dependsOn(patchChangelog)
  }
}
