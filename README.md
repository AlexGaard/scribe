# scribe
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
