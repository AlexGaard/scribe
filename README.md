# scribe

[![](https://jitpack.io/v/AlexGaard/scribe.svg)](https://jitpack.io/#AlexGaard/scribe) [![Tests](https://github.com/AlexGaard/scribe/actions/workflows/test.yaml/badge.svg?branch=main)](https://github.com/AlexGaard/scribe/actions/workflows/test.yaml)

[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=scribe&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=scribe) [![Bugs](https://sonarcloud.io/api/project_badges/measure?project=scribe&metric=bugs)](https://sonarcloud.io/summary/new_code?id=scribe) [![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=scribe&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=scribe) [![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=scribe&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=scribe)

Lightweight and simple library for configuring applications.

Supports loading configuration parameters from different sources such as properties, environment variables, .env files, HashMaps etc...

## Usage

```java

Config config = new ConfigBuilder()
        .loadSystemProperties()
        .loadEnvironmentVariables()
        .loadPropertiesFile("./my.properties")
        .loadEnvFile("./local.env")
        .loadEnvFileIfExists("./might-not-exist.env")
        .build(); // merges the loaded config values

Optional<String> foo = config.getString("FOO");
boolean isEnabled = config.requireBoolean("IS_ENABLED");
int dbPort = config.requirePortNumber("DB_PORT");
URL url = config.requireUrl("SOME_URL");
List<String> values = config.requireStringList("VALUES");
Optional<Double> doubleValue = config.getDouble("DOUBLE_VALUE");

// ...


```

## Installation

### Add Jitpack repository

Gradle:
```kotlin
repositories {
	maven { setUrl("https://jitpack.io") }
}
```

Maven:
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

### Add dependency
Gradle:
```kotlin
dependencies {
	implementation("com.github.alexgaard:scribe:LATEST_RELEASE")
}
```

Maven:
```xml
<dependency>
    <groupId>com.github.alexgaard</groupId>
    <artifactId>scribe</artifactId>
    <version>LATEST_RELEASE</version>
</dependency>
```

The latest release can be found at https://github.com/AlexGaard/scribe/releases.

## Requirements

* JDK 11 or above
