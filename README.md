# TabooLib SDK

## Settings
```groovy
taboolib {
    tabooLibVersion = '5.59'
    loaderVersion = '2.12'
    classifier = null
    // relocate package
    relocate('io.izzel.taboolib', 'ink.ptms.taboolib')
    // built-in
    builtin = true
}
```

## Release Source Code
````groovy
processResources {
    from(sourceSets.main.allSource) {
        exclude 'plugin.yml'
    }
}
````
