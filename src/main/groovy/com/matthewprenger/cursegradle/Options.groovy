package com.matthewprenger.cursegradle

import org.gradle.api.Project

/**
 * Various options for CurseGradle. These affect the entire plugin and not just a single curse project.
 */
class Options {

    /**
     * Debug mode will stop just short of actually uploading the file to Curse, and instead spit out the JSON
     * to the console. Useful for testing your buildscript.
     */
    boolean debug = false

    /**
     * If this is left enabled, CurseGradle will automatically detect the compatible versions of Java for the project
     * and add them to the CurseForge metadata.
     */
    boolean javaVersionAutoDetect = true

    /**
     * Enable integration with the Gradle Java plugin. This includes setting the default artifact to the jar task.
     */
    boolean javaIntegration

    /**
     * Enable integration with the Bukkit plugin. This includes setting the default apiBaseUrl.
     */
    boolean bukkitIntegration = false

    /**
     * Enable integration with the ForgeGradle plugin. This includes setting dependencies on the reobfuscation tasks.
     */
    boolean forgeGradleIntegration

    /**
     * Enable integration with the Fabric loom plugin. This includes setting dependencies on the reobfuscation tasks.
     */
    boolean fabricLoomIntegration

    /**
     * Enable integration with generic endpoints. This requires setting the default apiBaseUrl.
     */
    boolean genericIntegration = false

    /**
     * The api base url of the curse forge project
     */
    String apiBaseUrl = 'https://minecraft.curseforge.com'

    Options(Project project) {
        def fg1 = project.plugins.hasPlugin("forge")
        def fg2 = project.plugins.hasPlugin("net.minecraftforge.gradle.forge")
        def fg3 = project.plugins.hasPlugin("net.minecraftforge.gradle")
        def loom = project.plugins.hasPlugin("fabric-loom")
        def java = project.plugins.hasPlugin("java")

        javaIntegration = java
        forgeGradleIntegration = fg1 || fg2 || fg3
        fabricLoomIntegration = loom
    }
}
