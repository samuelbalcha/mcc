'use strict';

var express = require('express');
var mongoose = require('mongoose');
var http = require('http');
var bodyParser = require('body-parser');

var moment = require('moment');
var google = require('googleapis');

// lets usage of HTTP verbs where client does not support 'em
var methodOverride = require('method-override');
var eventsApi = require('./server/apis/eventsApi');


var googleConfig = {
    clientID: 'xxx',
    clientSecret: 'yyy',
    calendarId: 'nnn',
    redirectURL: 'http://localhost:3000/auth'
};

var app = express();
var calendar = google.calendar('v3');
var oAuthClient = new google.auth.OAuth2(googleConfig.clientID, googleConfig.clientSecret, googleConfig.redirectURL);
var authed = false;

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
app.post('/api/v1/sync', eventsApi.syncEvents);

app.patch('/api/v1/events/:id/:person', eventsApi.addParticipantToEvent);

// auth (google)
app.get('/gauth', function(req, res) {

    // if not authenticated, go to auth
    if (!authed) {
        // Generate an OAuth URL and redirect there
        var url = oAuthClient.generateAuthUrl({
            scope: 'https://www.googleapis.com/auth/calendar'
        });
        res.redirect(url);
    } else {

        // Format today's date
        var today = moment().format('YYYY-MM-DD') + 'T';

        // Call google to fetch events for today on our calendar
        calendar.events.list({
            calendarId: googleConfig.calendarId,
            maxResults: 200,
            //timeMin: today + '00:00:00.000Z',
            //timeMax: today + '23:59:59.000Z',
            auth: oAuthClient
        }, function(err, events) {
            if(err) {
                console.log('Error fetching events');
                console.log(err);
                res.send([])
            } else {

                // Send our JSON response back to the browser
                console.log('Successfully fetched events');
                res.send(events);
            }
        });
    }

});

// Return point for oAuth flow, should match googleConfig.redirectURL
app.get('/auth', function(req, res) {

    var code = req.param('code');

    if(code) {
        // Get an access token based on our OAuth code
        oAuthClient.getToken(code, function(err, tokens) {

            if (err) {
                console.log('Error authenticating')
                console.log(err);
            } else {
                console.log('Successfully authenticated');
                console.log(tokens);

                // Store our credentials and redirect back to our main page
                oAuthClient.setCredentials(tokens);
                authed = true;
                res.redirect('/');
            }
        });
    }
});

// Syncs events from what's out to google calendar
app.get('/api/v1/gsync', function(req, res) {

    // if not authenticated, redirect to OAuth URL
    if(!authed) {
        // Generate an OAuth URL and redirect there
        var url = oAuthClient.generateAuthUrl({
            scope: 'https://www.googleapis.com/auth/calendar'
        });
        res.status(200).send({'url': url});
    }

    var Event = require('./server/models/events').Events;
    Event.find().exec(function(err, evts){
        if(evts) {
            for(var i= 0; i < evts.length; i++) {
                var localevent = evts[i];
                var event = {
                    'summary' : evts[i].title,
                    'location': evts[i].location,
                    'description': evts[i].description,
                    'start': {
                        'dateTime': evts[i].date,
                        'timeZone': 'Europe/Helsinki'
                    },
                    'end': {
                        'dateTime': evts[i].dateEnd,
                        'timeZone': 'Europe/Helsinki'
                    }
                };
                if(evts[i].participants.length > 0) {
                    event.attendees = [];
                    for (var j = 0; j < evts[i].participants.length; j++) {
                        event.attendees.push(evts[i].participants[j])
                    }
                }
                if(localevent.gid) {
                    calendar.events.update({
                        auth: oAuthClient,
                        calendarId: googleConfig.calendarId,
                        eventId: localevent.gid,
                        resource: event
                    }, function(err, event) {
                        if (err) {
                            console.log('Error: unable to update calender ' + err);
                            console.log(err);
                            return;
                        }
                    });

                } else {
                    calendar.events.insert({
                        auth: oAuthClient,
                        calendarId: googleConfig.calendarId,
                        resource: event
                    }, function(err, event) {
                        if (err) {
                            console.log('Error: unable to insert into calender ' + err);
                            console.log(err);
                            return;
                        }
                        var conditions = { name: 'borne' }
                            , update = { $inc: { visits: 1 }}
                            , options = { multi: true };

                        console.log('Event created: %s', event.htmlLink);
                        var update = {
                            description: localevent.description+ '<br/><a href="'+ event.htmlLink + '">(link)</a>',
                            gid: event.id
                        };

                        Event.update(localevent, update, null,function(err){
                            if(err){
                               console.log('Unable to update event');
                            }
                        });
                    });
                }
            }
        }
    });
    res.status(200).send('OK');
});

var port = process.env.PORT || 3000;
var server = http.createServer(app);

/**
 * Server listens to port.
 * In case of error retries again
 **/

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