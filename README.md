# SchedNet
## Overview
Welcome to Schednet. This is a senior project in the form of an all-in-one todo list and calendar mobile app developed for Android OS API Level 21 (Lollipop) and above for just over 99% compatibility with the current Android userbase. It is to be noted however, that the current build has only been tested on Android Studio Virtual Machines. Specifically, a Pixel 2 with Android OS API Level 32 and a 5.4 FWGVA with Android OS API Level 22 for 98.9% compatibility. The current state of the project is that it is now complete. It took a lot of sleep sacrifice, but it is finally done. Unfortunately, it only has simple features and there is a bug or two but none that would cause an error to my knowledge. These are mainly when a device update causes the app the be recreated and it will still behave as normbal but with a minor visual issue in the bottom navigation bar as it won't correct what is currently selected after bringing it back to the first option. Other than that, there should be no issue. Also, this is NOT designed for other devices like tablet nor watch. This is strictly for phones. I did not have enough time to come up with pages for tablet. Same for flip phone however, I have tried to have my pages scalable down to flip if necessary. 

## Will This Project Continue After this Semester?
It was but I clearly do not have enough experience nor the resources to build a final app on the scale that I have planned. However, I will be posting other small projects soon with the next two being a simple diary app and a password multi tool (generates, grades, organizes, and prints passwords for physical solutions).

## Setting Up the Necessary Environment:
To test or experiment with this build of the project, a copy of Android Studio with a configured Virtual Machine are needed. Starting with the installation of the IDE that everything will run on. There will be two possible routes to run this project however, both use the virtual machine if you do not have an android device. The only route that will be described here is the latter option which is the APK version.

### Installing Android Studio
To start things off, you will need to install a copy of Android Studio or an existing android device. However, Android studio may be needed as a bridge to install on an existing device. I am not sure since I don't have much experience with one. To run in Android Studio, use the link [here](https://developer.android.com/studio), click on the big "Download Android Studio" button and accept the terms of service. There is no need to worry about which version to install since the website will sense the OS and pick for you. After downloading the installer, launch it and follow through with the default option. For detailed instruction of installation, click on this link [here](https://developer.android.com/codelabs/basic-android-kotlin-compose-install-android-studio#2). There are detailed written instructions with screenshots to help you through the process for Windows, Mac, and Linux OS.

### Downloading the Project 
With this installment, I made a new branch with the final version in both source and APK form factor. The branch contains an open version of the source code and inside of the open version is a zipped copy of the APK (Name of APK: SchedNet APK V1.01.zip). For this option, one method is to select the APK from within the main branch and click the download button that is right below the History button. The other method is to select the "Code" button and then the "Download Zip" button. The second option will download both the source code and the zipped APK.

### Setting up an android virtual machine
Now that we have Android Studio installed, it is time to create a virtual machine. If you have not worked with android studio before then you should be in the welcome page with no existing projects. In this page, you will have to click on the settings button at the top right of the window (button with three dots) then open the device manager <Launch Android Studio → Menu Button → Device Manager>. When in the Device Manager Window, select the "Create Device" button. This will show you many devices with the default selected device being a Pixel 2. This will be a good option so select "Next" in the bottom right to get to the System Image selection page. The default image should be Android OS R at API level 30. The code was written with compatibility in mind so the default will work just fine. As a first timer, you will have to download the OS which there should be a download button next to the release name in the table on the left and another in its description on the right. This may take a few minutes. After finishing, you will be brought to a verification page. There is no need to review. Just select "finish" and you will now have a virtual machine.

If you have been working with Studio before then you should know how to access the Device Manager Window from within a project. If not, here is a reminder. Within a project, follow this route <Tools → Device Manager>. The route described above after opening the device manager is <Create device → Phone Section → Pixel 2 → Next → Release name R <download it not downloaded before> → Next → Finish>.

Note: If you are installing the APK onto a real device then you will have to open the source code and use the menu inside of the project. [Here](https://youtu.be/qipksSgXZi0) is a simple video that should help you use a real device. As a warning, you will want to disable developer mode on your phone afterwards for security reasons.

### Installing the project on your new virtual machine
Now, it is time to launch your virtual machine. Within the Device Manager, select the play button under the column avidly named "Actions". This should launch the virtual machine. If not, there are some links and information for troubleshooting at the very bottom of this page. Wait for your device to finish loading and which version (source code or APK) you plan on running will determine how the app will be installed. If you are going the source code route then you will have to open the source code in the developer window and select the green run button in the top right control bar. This may have issues becuase Android Studio must connect to the virtual machine and new setups are likely to run into a Refused to Connect error issue. The easier option will be to unzip the APK file and drag the it onto the virtual machine. It will immediatly install the app for you and be ready to run.
  
## Exploring the Project:
### Logging in
By now, you have have the app running. The first screen is the login screen. Everything is local and new so there are no accounts. All testing has been done with created accounts and not premade developer accounts so the first action will be to register. In the registration, the first thing will be enter an account name, username, and password <Note: All usernames must be unique so future registrations will not let you use the same username again until the account is deleted>. Next, enter your SEQ questions and answers <Make sure to write these down because these will allow you to rescue your account later>. Make sure to end your sentences. The SEQ questions are setup to allow statements. After selecting confirm, login with your new account.  
  
### Navigating the Main Menu
After logging in, you will be starting in the list page. On the bottom of the screen is a navigation bar which will allow you to travers between Todo, Calendar, Favorites, and Settings. Before creating tasks in the Todo branch, a list will have to created. Right above the navigation bar is the action bar. Every branch except for Settings will have one. On the right side of the action bar will always be an action button for creating something whether a list or task under the Todo branch, or a calendar event under the Calendar branch or even a profile for a common task under the Favorite branch. This will always bring up a bottom dialog that will prompt you to enter information. you will be warned when a required section is not filled when attempting to submit. Select the add button, enter a list name, confirm, and select your new list. You will then be able to use the add button to submit a task to the list. All list and todo tasks require a name. Todo tasks can be further given a description. Lists and tasks can be pinned by selecting the pin button in the edit page, and main page. Tasks can be pinned at creation. Both can be deleted by swiping left on their ribbons. Lists can be edited via long click and bottom dialog. Tasks can be edited with a click and a dedicated page. Lists can be opened with a click. Tasks can be completed by checking their box.

  The next branch is the Calendar branch. Events can be added to the calendar through the add button which will have you enter a name and pick a date and time. In the action bar, there are two other options. The option on the left will open list view which will display all upcoming events as ribbons with name, date, and time. Events can be deleted in the page by swiping left on the ribbon. Events can be edited by clicking on its ribbon. List view can be exit by click on the button that replaces the action bar. List view can be opened for a specific day by clicking on a specific day in the calendar. The option on the right will bring the calendar back to the current month. The top of the calendar has a second action bar dubbed the control bar. It will display the current month as well as allow you to go backwards and forwards a month. The calendar is designed to maintain the current open month so you can go to another branch then return. The calendar will return to the current month when the app is closed and launched again or when the refocus button is clicked.

Note: People can delete old events manually however, there is another method that is automatic that will be explained when going over settings.
  
  The next branch is the Favorite branch which is where profiles of common events can be created. This is split into separate list. The first list visible will be the Todo profile list however, this can be changed to start with calendar profiles in settings. The add button will allow you to create profiles. Profiles can be edited my click and a different page <Note: I forgot to set it to up so it can the maintain current list after leaving the edit page so it will return you to your default page in settings. By the time I noticed, it was too late.>. On the left side of the action bar, there is a button that will switch to the opposite list. Favorite profiles can be pinned. Favorite profiles can be added to a list or the calendar by selecting the plus button on the left of the profiles ribbon. This will open a dialog for the task or event with the information filled in. You can edit the info or immediately confirm it if a Todo task. For calendar events, you will have to select a date and time before confirming. For Todo tasks, a list will have to be selected first. The list page can be exit by picking a list and completing the add cycle or clicking the cancel button that replaces the action bar.
  
  The next and final branch is the Settings branch. On this page, you will be able to change the name of their account, security questions and answers, and their passwords. Also on this page, you will be able to pick whether the favorite branch will start on calendar or list. The final thing that can be configured on this branch is the garbage collector. For Todo tasks, there are options for <Off, Instant, Daily, Weekly, Monthly, and Yearly>. For Todo tasks, this will clear completed tasks based on the last time they have been interacted with <Pinning and unpinning even after marked complete will update this time>. For Calendar events, there are options for <Off, Instant, Weekly, Monthly, and Yearly>. Calendar events are cleared based on X amount of time before today at 12:00 AM. Instant on both clears based on now down to the nano second. The garbage collector can be turned off by picking "Off". Default for Todo is "Weekly". Default for Calendar is "Yearly". If you are constantly traveling, it is recommended to set the garbage collector to a more generous setting due to differences between time zones. The last options are logout and delete account. Delete account has a warning dialog what will require confirmation before deletion. This is because deleting clears everything tied to the account.
  
Note: Calendar Garbage Collector has been fully tested. Todo Garbage Collector has not been fully tested due to time constraints. If there is a bug with it, please let me know!
  
  If a password is forgotten, you will have to go through forgot password. This process starts by entering your username. Then it will ask you your questions. Answering is case sensitive so be careful. After answering the questions, you will be prompted to enter a new password. Confirming will bring you back to login.
  
## Troubleshooting the Emulator
Sometimes, your Android Studio may behave erroneously. There is nothing wrong with your computer nor the app. The virtual machine is just buggy at times. Common problems may be the virtual machine randomly dyeing and the app is still running or Android Studio refusing to connect to the virtual machine. If you have the first of the two, then the recommended option is to stop the app and go to the Device Manager tab which should be on the top right edge of the window. Select the tab to open the page and try running the Virtual Machine again or even better, cold booting. If the problem is the ladder, then cold booting (or a fresh restart) may work which will require you to go drop down arrow under the actions column in the Device Manager and select cold boot <You may have to wait a few minutes for the AVD to release the lock on the virtual machine>. After the virtual machine finishes booting, try running the app again.
  
  
Note: Older computers may not be able to run the AVD.
  
  [More Trouble Shooting Info](https://developer.android.com/studio/run/emulator-troubleshooting)


