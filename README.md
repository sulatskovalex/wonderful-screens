# wonderful-screens
Скрины на замену фрагментам

дока в разработке

# Installation 


Project `build.gradle`

```
buildscript {
  ext.screens_version: '0.0.6'
}

allprojects {
  repositories {
    maven { url "https://dl.bintray.com/sulatskovalex/maven" }
  }
}
```

module `build.gradle`


`implementation "com.github.sulatskovalex:wonderful-screens:$screens_version"`
