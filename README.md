# CurseGradle

[![Build Status](https://github.com/CDAGaming/CurseGradle/actions/workflows/ci.yml/badge.svg?event=push)](https://github.com/CDAGaming/CurseGradle/)

A gradle plugin for publishing artifacts to [CurseForge](https://curseforge.com/).

* [Discord](https://discord.com/invite/BdKkbpP)
* [Wiki](https://github.com/CDAGaming/CurseGradle/wiki/)
* [Changelog](https://github.com/matthewprenger/CurseGradle/releases)

## Quickstart
The following script is the bare-minimum. For more details about customization, check out the [Wiki](https://github.com/CDAGaming/CurseGradle/wiki).

To find out which versions are available, check [HERE](https://plugins.gradle.org/plugin/io.github.CDAGaming.cursegradle).

```gradle
plugins {
    id 'io.github.CDAGaming.cursegradle' version '<VERSION>'
}

curseforge {
  apiKey = '123-456' // This should really be in a gradle.properties file
  project {
    id = '12345'
    changelog = 'Changes' // A file can also be set using: changelog = file('changelog.txt')
    releaseType = 'beta'
  }
}
```
