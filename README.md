![](images/broadcast.png)

Android client app that fetches, stores and displays the weekly meals provided by the [Bandejao](https://bandejao.gjhenrique.com) [API](https://github.com/gjhenrique/BandejaoServer). 

This Android project follows the principle of creating multiple apps for the universities based on a single code.
With the use of dynamic [Product Flavors](http://tools.android.com/tech-docs/new-build-system/user-guide#TOC-Product-flavors) and custom [generators](app/src/overlay/generator/generate_overlay), 
we can build and publish an app for a new university in a breeze.

[![Google Play](images/play-store.png)](https://play.google.com/store/apps/details?id=br.uel.easymenu)

## Features
* [GCM](https://developers.google.com/cloud-messaging/) notifications
* Daily synchronization with [CWAC Wakeful](https://github.com/commonsguy/cwac-wakeful)
* Material design
* Easy inclusion of new universities

## Create an app for your own university
To accomplish the task of __One code with multiple apps__, an automated mechanism to include new universities resources and the correct URL is required.
To add your university, follow these simple steps:

### Include the university information
We will build a fake university called Programming University
Add your university information in the [universities.yml](app/src/overlay/universities.yml) file
```yaml
# The key is the university name.
# This name has to match the name of 
PU:
  # Used for the Play Store description
  long_name: Programming University
  # Color of the toolbar background
  primary_color: "000"
  # Color of status bar and other minor elements
  secondary_color: "dd00dd"
  # Default language 
  default_language: en-US
```

### Generate the resources
Generates the strings, colors, generators.
For text files, we use [mustache](https://mustache.github.io) for texts and [gimp script-fu](https://docs.gimp.org/en/gimp-concepts-script-fu.html) for adapting the colors of the image.
```bash
sudo apt-get install gimp
cd app/overlay/app/src/overlay/generator
bundle
./generate_overlay --uni=pu
```

### Build and publish
```bash
# -Puni=<university name> creates the Product Flavor for the university
# Builds the code and publishes the apk and the resources to Play Store
./gradlew -Puni=pu publishPuRelease
```
