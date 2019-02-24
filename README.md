# What's this about??
A few years back a project called Multi-Lingual Fam was launched with the goal of increasing awareness and promoting the 
use of African Languages amongst students at the University of Cape Town. This was achieved by putting up a new poster 
every day which contained a word with phrases demonstrating its use, together with an English translation, as well as an 
interactive component. Click [here](https://www.instagram.com/tutorxsocialprojects/) if you would like to see the posters we made. 

During the course of the campaign we realised that people were more likely to remember the phrase if, they were in a context
where they could use the word/phrase shortly after learning it. For example: at a bus stop a word in isiXhosa like "Uyaphi?", 
meaning "where are you going?", would have a high chance of being used and remembered. Based on this insight, I started thinking... what if
there was an app that could recommend words and phrases given a user's current location... The idea remained dormant until now! 

This project will always be open source and will be a good place to find sample code for building features into your own app. 
If you feel passionate about preserving African languages and culture or just want to help out please do get in touch. We have a 
totally open door policy. Anyone who helps will be acknowledge as a team member in the app. 

### Sister Repo
The accompanying Cloud Functions repo can be found [here](https://github.com/olddustysocksunderthecouch/CloudFunctionForLocationForegroundServiceSample)

# "Steal-able Code"
- Tracking a user's **location** while the App is in the background with a **Foreground** service see below for details
- Cloud Functions for:
    - Updating location - once called, the Cloud function:
        1. Updates database 
        2. Triggers a word suggestion (notification) if in range of a suggestion location
    - Updating/Adding a unique device token to the database (used for sending notifications)
- Email Authentication with Firebase 
    - Just copy over the `auth` folder and `activity_authentication.xml` layout into your project
    - Make the adjustments recommended by Android Studio
- Making data persist (caching reads/writes)
    - Copy and paste `FirebaseUtil` into your project then when ever you need a ref to the db use 
    ``` kotlin
    val mRef = FirebaseUtil.database.reference
    ```
- Splash Screen - with auth check
- Receiving notifications while the app is open, in the foreground (not to confused with a foreground service)
    - See `CloudMessagingService` and it's registration in the `AndroidManifest.xml`


# Architecture
Coming Soon

### Foreground Services
What is it?

Put simply is that an app must show a notification which can’t be swiped away if 
they what to run tasks like GPS in the background and get reliable updates. There are certain tasks that an app can do 
without showing a notification. 
Services are things that can run in the background 

_Open for suggestions on what else to include in this section or any other section for that matter_

# Setup
### General
 1. Create a new Firebase Project using the [Firebase Console](https://console.firebase.google.com).
 1. Enable **Email Provider** in the [Auth section](https://console.firebase.google.com/project/_/authentication/providers)

 ### Android App
 See the accompanying [Cloud Functions Repo](https://github.com/olddustysocksunderthecouch/LocationForegroundServiceSample)
 for other setup instructions
 1. Clone or download this repo and open the directory in a terminal/command line of your choice.
 1. Go to Project Settings in Firebase Console
     - Add an App (Android)
     - Enter the Package Name
     - Enter SHA1 Key for authentication 
         - Your SHA1 key can be generated in Android Studio by clicking on Gradle (far right) 
         then --> your project name --> Tasks --> Android --> signingReport 
 1. Download and copy google-service.json
 1. Paste (and replace) it into the app folder (Your app file structure set to Project to do this)

# Want to help out?
### Principal/Values
- This project will always be open source
- We will never store a long term history of user's location data
- We will never sell user location data or any other data for that matter
- If you contribute to the project you will be acknowledged for your contribution

### Questions / Features You could help out with
- How to we keep location tracking accurate while having a limited impact on battery?
- Strategy/logic around when notifications are sent to users?
- How do we implement language preferences?
- Security and Storage Rules
- **Translations** and word/phrase suggestions
- Design - we need a better logo as well as some UX work
- Bring in **your own ideas** and implement them! 

### I'm cool with that, what's next?
Fill in [this super quick Google Form](https://goo.gl/forms/TIE4wfNlaMAeNOwp2) and I'll get in touch with you ASAP.
