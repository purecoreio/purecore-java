# purecore.io java wrapper 
[![nexus version](https://img.shields.io/nexus/r/https/oss.sonatype.org/io.purecore/api.svg)](https://repo1.maven.org/maven2/io/purecore/api/)
[![discord guild](https://img.shields.io/discord/622819380958461984.svg)](https://discord.com/invite/CVnQAh?utm_source=Discord%20Widget&utm_medium=Connect)
[![twitter](https://img.shields.io/twitter/follow/purecoreio?style=social&logo=twitter)](https://twitter.com/purecoreio)


## Gradle
```groovy
compile group: 'io.purecore', name: 'api', version: '+'

// or

compile 'io.purecore:api:+'
```

## Maven
```xml
<dependency>
    <groupId>io.purecore</groupId>
    <artifactId>api</artifactId>
    <version>RELEASE</version>
</dependency>
```

## Are you in the right place?

If you are building a Minecraft plugin, this is **not** what you are looking for. You should get a mc-plugin purecore.io instance. In order to do that, please, take a look at the mc-plugin repo to see how to get an existing purecore.io instance on a loaded server.

If you are using purecore.io for editing the core functionality of purecore's java wrapper, this is the place.

In order to code a purecore addon for craftbukkit, forge, or others, just grab the live core instance with getCore(), do **not** create a new Core() object, or you won't be able to access the instance information.
