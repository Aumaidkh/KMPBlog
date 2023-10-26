import com.varabyte.kobweb.gradle.application.util.configAsKobwebApplication
import kotlinx.html.link
import kotlinx.html.script

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.kobweb.application)
    alias(libs.plugins.serialization.plugin)
    // alias(libs.plugins.kobwebx.markdown)
}

group = "com.hopcape.blog"
version = "1.0-SNAPSHOT"

kobweb {
    app {
        index {
            description.set("Powered by Kobweb")
            head.add {
                script(
                    type = null,
                    src = "/highlight.min.js",
                    block = {}
                )
                link(
                    rel = "stylesheet",
                    href = "/github-dark.css"
                )
                script(
                    type = null,
                    src = "https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js",
                    block = {}
                )
                link(
                    rel = "stylesheet",
                    href = "https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css"
                )
                link(
                    rel = "preconnect",
                    href = "https://fonts.googleapis.com"
                )
                link(
                    rel = "preconnect",
                    href = "https://fonts.gstatic.com"
                )
                link(
                    rel = "stylesheet",
                    href = "https://fonts.googleapis.com/css2?family=Poppins:wght@100;300;400;500;600;700;800;900&display=swap"
                )

            }
        }
    }
}

kotlin {
    configAsKobwebApplication("blog", includeServer = true)

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(libs.kotlinx.serialization)
            }
        }

        val jsMain by getting {
            dependencies {
                implementation(compose.html.core)
                implementation(libs.kobweb.core)
                implementation(libs.kobweb.silk)
                implementation(libs.silk.icons.fa)
                implementation(libs.kotlinx.serialization)
                // implementation(libs.kobwebx.markdown)
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(libs.kobweb.api)
                implementation(libs.kmongo.database)
                implementation(libs.mongodb)
                implementation(libs.kotlinx.serialization)
            }
        }
    }
}
