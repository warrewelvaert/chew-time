# NMA Project - Warre Welvaert

<table>
  <tr>
    <td>
      <img src="Images/Screenshot_Add_Pet_Chew%20Time.jpg" alt="Add Pet" />
    </td>
    <td>
      <img src="Images/Screenshot_Pet_Overview_Chew%20Time.jpg" alt="Pet Overview" />
    </td>
    <td>
      <img src="Images/Screenshot_Add_Food_Chew%20Time.jpg" alt="Add Pet" />
    </td>
        <td>
      <img src="Images/Screenshot_Drawer_Chew%20Time.jpg" alt="Drawer Menu" />
    </td>
  </tr>
</table>

# 1. Concept and Introduction

For the topic of my application, I have chosen a pet food tracker app called Chew Time. The general use of the app is to allow user to add their pets and pet food in order to keep track of how much food they have in storage, and to remind them when to feed their pets. 

Users can use the app to add pet profiles(cats or dogs) to the application. In the profile they can specify the pet’s name and species, add a picture, and select when the pet is fed. The
user can decide whether sets an alarm reminder when it's time to feed their pet.

The user can add different types of pet food to the app and decide how much they have in storage and at what rate the pet eats the food. The app will calculate when the food runs out and notify the user when the food storage is almost empty. The application can help the user to quickly order food from an external webstore and add more food to their storage. 

### ! Important notes

- Server: When adding a pet food, the application will call an API. The server has been remotely deployed on https://nma-project-server.onrender.com/api/. Because it's deployed with a free tier account the server will go to sleep after not being used for some time. This means that it may take up to 30 seconds to connect to the server when it's asleep to wake it up again. So, if the app takes a long time to load when adding a pet food this can be fixed by waiting some seconds and trying again. After which the server should load quickly.

- This app was created for Android devices using the API 33 or Android version 13 and was designed for a screen size of 1080 x 2340. Although older Android versions up to API level 26 or Android 8.0 should be supported, the screens may not be displayed as intended.

# 2. Deliverables

## 2.1. Wire Frames

### [Link to the Wire Frames](https://www.figma.com/file/Qha0NaiTH6Xtig31xi4DYJ/Low-Fi-NMA-Project?node-id=0-1)

## 2.2. Video

### [Link to the video showing the functionality of the app](https://www.youtube.com/watch?v=GMYou_GlGWg)

## 2.3. Devoloper Badges

### [Link to profile page](https://developers.google.com/profile/u/116354308138608151069)

# 3. Application Features

Below a list of all required and optional features can be found, along with how and where they are implemented.

## 3.1. Must Have

#### [X] Build a multiscreen app (at least 4 screens) using the Jetpack Navigation component

#### [X] Use Jetpack Compose with Kotlin to build your screen layout (use at least 4 different elements with mandatory a scrollable List, Button, Text and Image)

#### [X] Create a menu-based navigation and bottom navigation
- Drawer menu on side of the screen + bottom navigation.

#### [X] Use the Android app architecture (ViewModel – State in Compose)

#### [X] Use a local Room database to store user data persistently
- Database is used to store Food with the amount in storage and the daily consumption, and Pet with the times they are fed.

#### [X] Use Retrofit to communicate with an API service
- API is called to retrieve a list of available pet foods when adding a new pet food to the storage. 
- See note in introduction for server limitations.

#### [X] Schedule at least 1 background task using the Workmanager
- Two work managers the schedule notifications and alarms: AlarmWorker.kt & NotificationWorker.kt.

#### [X] Use at least 2 Implicit Intents
1. Intent to open the preferred  web browser of the user when clicking on "Order More" button in FoodDetailScreen.kt. This takes the user to a webstore where they can order the desired pet food.

2. Intent to set an alarm for the user in AlarmWorker.kt. When the user clicks on a pet on the timer screen they are asked if they want set a reminder for the feeding time of the pet. On confirmation the AlarmWorker will add the corresponding feeding time as an alarm in the users Clock app.

3. Intent to add a profile picture of a pet by opening the users gallery when clicking on the profile icon when creating a pet on the AddPetScreen.kt.

#### [X] Write significant tests to make a robust app
- All tests are written as Android instrumented tests.

#### [X] Implement a custom application theme using MaterialDesign with a custom app
- Custom icon and light and dark mode theme available. 


## 3.2. Intermediate - self learning topics

#### [X] Notifications
- When the user adds a new food to the database the application calculates when the food runs out and schedules a notification with a WorkManager for 2 days before the food runs out. When the user updates the amount of food left this is recalculated and rescheduled. 
  
- The notifications can be tested by adding a food which has less than 2 days of food remaining in storage. 

#### [ ] Use the CameraX library - NOT IMPLEMENTED
- Camera X library is not implemented, although a picture can be added on the AddPetScreen this is done with an intent.

#### [X] Canvas and animations
- Loading animation can be found in LoadingScreen.kt, can be seen while loading after calling the API when adding a new food object.
  
- Canvas used to create ErrorScreen.kt, can be seen when API call fails when trying to add a new food object.

## 3.3. Experienced - self learning topics

#### [ ] ML Kit, Vision or Natural Language API - NOT IMPLEMENTED