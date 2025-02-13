package com.matthewprenger.cursegradle

import com.google.common.collect.Iterables
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.gradle.api.plugins.JavaPluginExtension

class Integration {

    private static final Logger log = Logging.getLogger(Integration)

    static void checkJava(Project project, CurseProject curseProject) {
        try {
            if (project.plugins.hasPlugin('java')) {
                log.info 'Java plugin detected, adding integration...'
                if (curseProject.mainArtifact == null) {
                    Task jarTask = project.tasks.getByName('jar')
                    log.info "Setting main artifact for CurseForge Project $curseProject.id to the java jar"
                    CurseArtifact artifact = new CurseArtifact()
                    artifact.artifact = jarTask
                    curseProject.mainArtifact = artifact
                    curseProject.uploadTask.dependsOn jarTask
                }
            }
        } catch (Throwable t) {
            log.warn("Failed Java integration", t)
        }
    }

    static void checkJavaVersion(Project project, CurseProject curseProject) {

        try {
            JavaPluginExtension javaConv = project.getExtensions().getByType(JavaPluginExtension.class)
            JavaVersion javaVersion = JavaVersion.toVersion(javaConv.getTargetCompatibility())
            curseProject.addGameVersion(String.format('Java %s', javaVersion.getMajorVersion()))
        } catch (Throwable t) {
            log.warn("Failed to check Java Version", t)
        }
    }

    static void checkForgeGradle(Project project, CurseProject curseProject) {
        if (project.hasProperty('minecraft')) {
            log.info "ForgeGradle plugin detected, adding integration..."

            // FG 3+ doesn't set MC_VERSION until afterEvaluate
            project.gradle.taskGraph.whenReady {
                try {
                    if (project.minecraft.hasProperty('version')) {
                        log.info 'Found Minecraft version in FG < 3'
                        curseProject.addGameVersion(project.minecraft.version)
                        curseProject.addGameVersion('Forge')
                    } else if (project.getExtensions().getExtraProperties().has('MC_VERSION')) {
                        log.info 'Found Minecraft version in FG >= 3'
                        curseProject.addGameVersion(project.getExtensions().getExtraProperties().get('MC_VERSION'))
                        curseProject.addGameVersion('Forge')
                    } else {
                        log.warn 'Unable to extract Minecraft version from ForgeGradle'
                    }
                } catch (Throwable t) {
                    log.warn('Failed ForgeGradle integration', t)
                }
            }
        }
    }

    static void checkFabric(Project project, boolean javaEnabled, CurseProject curseProject) {
        def mcConfig = project.configurations.findByName("minecraft")
        if (mcConfig != null) {
            log.info "loom minecraft configuration detected, adding integration..."

            String mcVersion = Iterables.getOnlyElement(mcConfig.dependencies).version
            curseProject.addGameVersion(mcVersion)
            curseProject.addGameVersion('Fabric')

            def remapJar = project.tasks.findByName('remapJar')
            if ((curseProject.mainArtifact == null || (javaEnabled && isJavaDefaultJar(curseProject))) && remapJar != null) {
                log.info "Setting main artifact for CurseForge Project $curseProject.id to Fabric remap jar"
                CurseArtifact artifact = new CurseArtifact()
                artifact.artifact = remapJar
                curseProject.mainArtifact = artifact
                curseProject.uploadTask.dependsOn remapJar
            }
        }
    }

    private static boolean isJavaDefaultJar(CurseProject curseProject) {
        def artifact = curseProject.mainArtifact.artifact
        return artifact instanceof Task && ((Task) artifact).getName() == "jar"
    }
}
