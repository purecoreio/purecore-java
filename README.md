# purecore.io java wrapper

## Gradle
```groovy
compile group: 'io.purecore', name: 'api', version: '1.1.0'

// or

compile 'io.purecore:api:1.1.0'
```

## Maven [Badge-SonatypeReleases]: https://img.shields.io/nexus/r/https/oss.sonatype.org/io.purecore/api.svg "Sonatype Releases"
```xml
<dependency>
    <groupId>io.purecore</groupId>
    <artifactId>api</artifactId>
    <version>Last Version, Look At The Nexus Badge</version>
</dependency>
```
## Are you in the right place?

If you are building a Minecraft plugin, this is **not** what you are looking for. You should get a mc-plugin purecore.io instance. In order to do that, please, take a look at the mc-plugin repo to see how to get an existing purecore.io instance on a loaded server.

If you are using purecore.io for editing the core functionality of purecore's java wrapper, this is the place.

In order to code a purecore addon for craftbukkit, forge, or others, just grab the live core instance with getCore(), do **not** create a new Core() object, or you won't be able to access the instance information.
