# NyaLib

## Features
* Block networking system
* Universal Item and Energy Handlers

## How to Use  

1. Add NyaRepo under `repositories`
```groovy
maven {
	name = "NyaRepo"
	url = "https://maven.fildand.cz/releases"
}
```
NyaLib is also published on Glass Maven
```groovy
maven {
    name = 'Glass Releases'
    url = 'https://maven.glass-launcher.net/releases'
}
```

&nbsp;
2. Add NyaLib as a dependency in `dependencies`
```groovy
modImplementation "net.danygames2014:NyaLib:${project.nyalib_version}"
```

&nbsp;
3. Specify the version in `gradle.properties`
```properties
nyalib_version=0.9.0
```

