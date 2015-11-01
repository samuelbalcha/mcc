'use strict';

var express = require('express');
var mongoose = require('mongoose');
var http = require('http');
var bodyParser = require('body-parser');

// lets usage of HTTP verbs where client does not support 'em
var methodOverride = require('method-override');
var eventsApi = require('./server/apis/eventsApi');
var authApi = require('./server/apis/authorization');

var app = express();

// tell express where client folder is
app.use(express.static(__dirname + '/public'));
app.use(bodyParser.json());
app.use(bodyParser.json({ type: 'application/vnd.api+json' }));
app.use(methodOverride());

// connect to mongo (Mongolab)
var mongo_uri = 'mongodb://mcc:T-110.5121@ds037447.mongolab.com:37447/fendka';
mongoose.connect(mongo_uri);


// api endpoints (version 1)
app.get('/api/v1/events', eventsApi.getAllEvents);
app.get('/api/v1/events/:id', eventsApi.getEvent);
app.post('/api/v1/events', eventsApi.createEvent);
app.put('/api/v1/events/:id', eventsApi.updateEvent);
app.delete('/api/v1/events/:id', eventsApi.removeEvent);
app.get('/api/v1/search', eventsApi.searchEvents);
app.patch('/api/v1/events/:id/:person', eventsApi.addParticipantToEvent);

// auth
app.post('/auth/google', authApi.googleLogin);

var port = process.env.PORT || 3000;
var server = http.createServer(app);

 
server.listen(port).on('error', function(err){
    if(err.code === 'EADDRINUSE'){
        port++;
        console.log('Address in use, retrying on port ' + port);
        
        setTimeout(function () {
            server.listen(port);
         }, 250);
    }
});

console.log("App listening on port ", process.env.PORT);