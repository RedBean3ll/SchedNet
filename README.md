# SchedNet

## Overview
Welcome to Schednet. This is a senior project in the form of an all-in-one todo list and calendar mobile app developed for Android OS API Level 21 (Lollipop) and above for just over 99% compatibility with the current Android userbase. It is to be noted however, that the current build has only been tested on Android Studio Virtual Machines. Specifically, a Pixel 2 with Android OS API Level 32 and a 5.4 FWGVA with Android OS API Level 22 for 98.9% compatibility. The current state of the project is mostly complete with little work with the database handler, a few minor functions, and cleaning up remaining. Below is a list of current actions that are available in this build:

## Currently Available Actions:
* Register new accounts
* Login with new accounts (Option to remember login)
* Navigate between pages
* Create and Delete Lists
* Create and Delete Tasks

## Status of Currently Unavailable Actions:
Calendar Functionality is complete however, not yet implemented in the master branch due to scheduling issues. Favorite Task Profiles are implemented however, it has yet to be rigged to the database yet. Both of these are slated to be implemented Wednesday 16th of November, 2022. Calendar guaranteed to be on the 16th however, favorites may not be implemented till the day after on the 17th. 

## What is Coming After These Updates?:
After these updates, some cleaning up of the UI will be done as well as the addition of more features. The settings page will be completed as a given addition along with a forgot password and change password feature. If time permits, reminders and repeating tasks will be added to calendar events and list tasks.

## Will This Project Continue After this Semester?:
Yes. To simply put it, there are loftier goals for this project in the future that will, when finished, hopefully be a swift yet powerful solution to planning both on the go and at rest. There truly is belief in the project and we find it important to advance. The goal is to keep this app simple yet add more crucial features over time. A long-time goal will be to add support for smart watches, home screen widgets, voice assistant control options, and more.

## Setting Up the Necessary Environment:
To test or experiment with the current main build of the project, a copy of Android Studio with a configured Virtual Machine are needed. Starting with the installation of the IDE that everything will run on. 

### Installing Android Studio
First things first is the IDE which can be installed [here](https://developer.android.com/studio). This link will bring you the the Android Studio Download page. After downloading the installer, launch the installer and it will walk you through it. For detailed instruction of installation, click on this link [here](https://developer.android.com/codelabs/basic-android-kotlin-compose-install-android-studio#2). There are detailed written instructions with screenshots to help you through the process for Windows, Mac, and Linux OS.

### Downloading the Project 
The version to download is a copy of the main branch [here](https://github.com/RedBean3ll/SchedNet/archive/refs/heads/main.zip). After the download is complete, unzip the directory and open Android Studio. If this is your first time installing Android Studio the you will be seeing [this](https://developer.android.com/static/codelabs/basic-android-kotlin-compose-install-android-studio/img/736f743c4450766c.png). To open the project, select the open option and navigate to your unzipped copy of the project. After doing so, open the directory and select the directory named "SchedNet-main" that is next to another directory named .idea. Confirm your selection and wait a minute for Android Studio to parse the files. [Here](https://developer.android.com/studio/run/managing-avds) is a detailed guide on how to create your first AVD. As a note, a recommended setup is to create a Pixel 2 with Android Release version R or API Lenel 30

### Running the Project
Now that we have Android Studio installed with an AVD established, Android Studio should automatically pair the app with the android virtual machine and all you would have to do is to select the green arrow button at the top right of the screen. First it will attempt to compile the project and then it will attempt to install it into the virtual machine. Once it succeeds, it will launch the app into the login screen.

## Exploring the Project:
### Logging in
By now, the app should be running in your new virtual machine on the login page with no users currently existing. There is a "Add sample user" button on this screen for now however, its function has been long removed so to move further, you must click on the button saying "REGISTER" and it will bring you to the registration page. Enter a username and password and select confirm. You will now be brought to the main area were everything can be accessed.

### Navigating the Main Menu
Now that you are logged in, you will be in the main menu with the list page on full display. This page holds all your todo lists and you may view the tasks by selecting a list. Before getting to far ahead, a fresh account will have no todo lists and tasks so you will have to create one by clicking on the plus symbol button on the right side of the action bar just above the navigation bar. Selecting it will bring up a Bottom Sheet Dialog Fragment. Enter a name and click save. Now you have a new list (Note: tapping away from the dialog fragment or pulling it down will dismiss the dialog fragment with no new list added). Clicking on the list will bring you the the lists page. Since this is a new list, there are no tasks present. To add a new task, select the plus symbol button (it is in the same place as the other page). Enter a task name and confirm by selecting the save button. Now you have a new task. To navigate back to the list page, click on the back button on the bottom of the virtual machine (Note: Not all have a bottom control bar. Those that do not can still perform this action with the emulator control buttons at the top left of the virtual machine). An example of an Android control bar is [this](https://developer.android.com/static/training/material/images/fab.png). It is the bottom left button. To navigate to other pages, select buttons in the bottom navigation bar and explore. The button next to the todo button in the Navigation bar is the Calendar (To be implemented tomorrow Nov 16th but still accessible). The button next to the calendar is a page dedicated to favorite tasks (Also to be implemented tomorrow Nov 16th), users will be able to create task profiles to then add to a list or the calendar at will. The last button is dedicated to settings. Due to time constraints, settings will not have many options by the end of this project period.

## Troubleshooting the Emulator
Sometimes, your Android Studio may behave erroneously. There is nothing wrong with your computer. The virtual machine is just buggy at times. Common problems may be the virtual machine randomly dyeing and the app is still running or Android Studio refusing to connect to the virtual machine. If you have the first of the two, then the recommended option is to stop the app and go to the Device Manager tab which should be on the top right edge of the window. Select the tab to open the page and try running the Virtual Machine again. If the problem is the ladder then cold booting (or a fresh restart) may work which will require you to go [here](https://developer.android.com/static/studio/images/run/avd-coldboot-callout_2x.png) in the DeviceManager and select cold boot. After the virtual machine finishes booting, try running the app again and it should succeed. If you are unable to relaunch the virtual machine to to the virtual machine not releasing the lock on itself after crashing, usually waiting a minute or two then relaunching the virtual machine will work however, cold booting is recommended after that situation.

Note: This readme is set to change in the near future with improvements.
