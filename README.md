image of the server sending to the university

Android client app that fetches, stores and displays the weekly meals provided by the _Bandejao_ API. 

This Android project follows the principle of creating multiple apps for the universities based on a single code.
With the use of dynamic [Product Flavors](http://tools.android.com/tech-docs/new-build-system/user-guide#TOC-Product-flavors), and custom _generators_, we can build and publish a new app in a breeze.

get it on google play image

## Features
* _GCM_ notifications
* Daily synchronization with _CWAC Wakeful_
* Material design
* Easy inclusion of new universities

## Build
// Import with Android studio

* export SDK_HOME
* gradle.properties
* ./gradlew yada yada


## Create an app for your own university

With the principle __One code. Multiple apps__, an automated mechanism to include new universities is required.
To include a new app for the, follow these steps.


### Include the university information
Include your university information in the _universities.yml_ file
```yaml
# University name.
# Generally, it is the initials, but you can put whatever you want.
PU:
  # Used for the Play Store description
  long_name: Programming University
  # Color of the Toolbar background
  primary_color: "000"
  # Color of status bar and other minor widgets
  secondary_color: "dd00dd"
  # Default language 
  default_language: en-US
```

### Generate the resources
Generates the strings, colors, generators.
For text files, we use _mustache_ for texts and _gimp script-fu_ for painting the images in the colors of the university
// gimp must be installed
gem install mustache, etc.
```generate_overlay```

### Build and publish
Builds the correct universities
This command includes the correct commands
Builds the apk with the overlaid resources
./gradlew assembleReleasePu -Puni=pu

Publish the apk
./gradlew generatePlayPu 
