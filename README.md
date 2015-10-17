MCC Assignment Phase 1

whats-out showcases how to build a simple `express` server that exposes REST API to create and manage events.
The events are stored in mongodb hosted at mongolabs service.
The sample contains unit tests that test the API endpoints. Some of the tests are in skip mode after the code is implemented but should work for an empty database otherwise.

## Running the server

1) To fetch the node modules and dependencies defined in the package.json: 
    $ npm install 

2) To launch the app from the Terminal:
    $ node server.js 
    or 
    $ nodemon

3) Tests can be run from the Terminal with:
   $ mocha