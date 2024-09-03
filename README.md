# NyaLib

## Features
* Block networking system
* Universal Item and Energy Handlers

## How to Use  

1. Add NyaRepo under `repositories`
```groovy
maven {
	name = "nyarepo"
	url = "https://maven.fildand.cz/releases"
}
```

2. Add NyaLib as a dependency in `dependencies`
```groovy
modImplementation "net.danygames2014:NyaLib:${project.nyalib_version}"
```

3. Specify the version in `gradle.properties`
```properties
nyalib_version=0.4.0
```

