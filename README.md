MCC Assignment Phase 3

whats-out showcases how to build a simple `express` server that exposes REST API to create and manage events.
The events are stored in mongodb hosted at mongolabs service.
The sample contains unit tests that test the API endpoints. Some of the tests are in skip mode after the code is implemented but should work for an empty database otherwise.

In this phase, a simple Android client using the REST API is implemented.
It features basic CRUD operation, and an event sync to mobile phone of the user.

## Running the server

1) To fetch the node modules and dependencies defined in the package.json: 
    $ npm install 

2) To launch the app from the Terminal:
    $ node server.js 
    or 
    $ nodemon

3) Tests can be run from the Terminal with:
   $ mocha


## How to sync with google calendar

1) Create google app and credentials.

2) Update google app credentials with your own in server.js (modify googleConfig object)

3) Go to / and sync to google calendar or from google calendar


## How to use with mobile phone (Android)

1) Supports Android version 4.4 and up

2) Compiled with Gradle, built with Android Studio

3) Permissions: Internet, Write/Read Calendar

4) In order to sync with calendar, create a calendar called "Whats OUT" so all events will be synced to the calendar. If not, events will be synced to first available calendar.
