# 09CapstoneProject - My Fridge
This app helps users to organize lists of what groceries to buy, what groceries are already in their fridge, and what they can make using groceries in the fridge by providing proper recipes (powered by FOOD2FORK API). The app also lets users know how many days have passed since they bought groceries and shows notification if any groceries are about to expire. 

![Demo](https://cloud.githubusercontent.com/assets/17938363/22281230/8c1a0522-e28a-11e6-8917-708f83b70de5.gif)

## Features
* List of groceries to buy & in user's fridge
* Recipes using ingredients in user's fridge
* Widget showing the list of items to buy
* Notofication if any items in the fridge is about to expire

## Getting Started
This sample uses the Gradle build system. To build this project, use the "gradlew build" command or use "Import Project" in Android Studio. Or clone this repository and import into **Android Studio**
```
git clone git@github.com:jinyoon0124/09CapstoneProject.git
```

## Configuration
###Keystores:
Create `keystore.properties` under project directory with the following info:
```
keyAlias='...'
keyPassword='...'
storeFile='...'
storePassword='...'
```
And place keystore under app directory
###FOOD2FORK API KEY:
Create your FOOD2FORK API KEY [here](http://food2fork.com/about/api) and add the key in `gradle.properties`
```
FOOD2FORK_API_KEY = "..."
```

## Generating signed APK
From Android Studio: 1. **_Build_** menu 2. **_Generate Signed APK_**... 3. Fill in the keystore information _(you only need to do this once manually and then let Android Studio remember it)_


## Libraries
* Google Play Services - Admob/ Analytics
* [Picasso](http://square.github.io/picasso/)
* [Retrofit](https://square.github.io/retrofit/)
* [Material Dialog](https://github.com/afollestad/material-dialogs)

## License
```
Copyright 2017 Jin Yoon

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
