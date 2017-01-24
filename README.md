# 09CapstoneProject - My Fridge

This app helps users to organize lists of what groceries to buy, what groceries are already in their fridge, and what they can make using groceries in the fridge by providing proper recipes (powered by FOOD2FORK API). The app also lets users know how many days have passed since they bought groceries and shows notification if any groceries are about to expire. 

## Getting Started

This sample uses the Gradle build system. To build this project, use the "gradlew build" command or use "Import Project" in Android Studio. Or clone this repository and import into **Android Studio**
```
git clone git@github.com:jinyoon0124/09CapstoneProject.git
```

## Configuration

###Keystores:
Create keystore.properties under project directory with the following info:
```
keyAlias='...'
keyPassword='...'
storeFile='...'
storePassword='...'
```
And place keystoreunder app directory
###FOOD2FORK API KEY:
Create your FOOD2FORK API KEY [here](http://food2fork.com/about/api) and add the key in `gradle.properties`
```
FOOD2FORK_API_KEY = "..."
```

## Generating signed APK

From Android Studio: 1. **_Build_** menu 2. **_Generate Signed APK_**... 3. Fill in the keystore information _(you only need to do this once manually and then let Android Studio remember it)_

## Screenshot
