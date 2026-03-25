# NyaLib

## Features
* Generic block networking
* Energy API and Energy Networks
* Item API
* Fluid API
* Block Capabilities
* Server driven particles and sounds/music
* Block Templates in the form of template classes and template models
* Basic structures with collision checking and rotation
* Multipart API
* Many utility features (e.g HasCraftingReturnStack, Slot Locking, EnhancedPlacement ContextItem etc.)

## How to Use  

#### Add NyaRepo under `repositories`
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
#### Add NyaLib as a dependency in `dependencies`
```groovy
modImplementation "net.danygames2014:NyaLib:${project.nyalib_version}"
```

&nbsp;
#### Specify the version in `gradle.properties`
```properties
nyalib_version=0.20.0
```

