'use strict';

// Simple http request client
var request = require('request');

// Manipulate & dispaly dates
var moment = require('moment');

// JSON web token encode & decode
var jwt = require('jwt-simple'); 

var User = require('../models/users');


var config = {
   GOOGLE_CLIENT_SECRET : '',
   Secret : 'mcc1&2'
};


// Code from Satellizer example
// https://github.com/sahat/satellizer/blob/master/examples/server/node/server.js
// Signups a user or login to the system.
exports.googleLogin = function(req, res) {

    var accessTokenUrl = 'https://accounts.google.com/o/oauth2/token';
    var peopleApiUrl = 'https://www.googleapis.com/plus/v1/people/me/openIdConnect';
    
    var params = {
        code: req.body.code,
        client_id: req.body.clientId,
        client_secret: config.GOOGLE_CLIENT_SECRET,
        redirect_uri: req.body.redirectUri,
        grant_type: 'authorization_code'
    };

 
    // Step 1. Exchange authorization code for access token.
    request.post(accessTokenUrl, { json: true, form: params }, function(err, response, token) {
       
        if(err){
            return handleError(err, 500, res, "something went wrong.");
        }
       
        var accessToken = token.access_token;
        var headers = { Authorization: 'Bearer ' + accessToken };

        // Step 2. Retrieve profile information about the current user.
        request.get({ url : peopleApiUrl, headers: headers, json: true }, function(err, response, profile) {
            if(err){
               return handleError(err, 500, res, "oauth err");
            }
            if (profile.error) {
                return handleError(err, 500, res,  profile.error.message );
            }
            // Step 3a. Link user accounts.
            if (req.headers.authorization) {
                
                User.findOne({ google: profile.sub }, function(err, existingUser) {
                    if(err){
                        return handleError(err, 500, res, "could not get data from db");
                    }
                    
                    if (existingUser) {
                        return handleError(err, 409, res, "There is already a Google account that belongs to you");
                    }
                    
                    var token = req.headers.authorization.split(' ')[1];
                    var payload = jwt.decode(token, config.Secret);
                    
                    User.findById(payload.sub, function(err, user) {
                        if(err){
                            return handleError(err, 500, res, "could not get data from db");
                        }
                        
                        if (!user) {
                           return handleError(err, 500, res, "could not find user");
                        }
                        
                        user.google = profile.sub;
                        user.avatar = user.avatar || profile.picture; 
                        user.displayName = user.displayName || profile.name;
                        user.email = profile.email;
                        
                        user.save(function() {
                            var token = createJWT(user);
                            res.send({ token: token });
                        });
                    });
                });
            } 
            else
            {
                // Step 3b. Create a new user account or return an existing one.
                User.findOne({ google: profile.sub }, function(err, existingUser) {
                    if(err){
                        return handleError(err, 500, res, "could not get data from db");
                    }
                        
                    if (existingUser) {
                        res.send({ token: createJWT(existingUser) });
                    }
                    
                    var user = new User();
                    user.google = profile.sub;
                    user.avatar = profile.picture; //.replace('sz=50', 'sz=200');
                    user.displayName = profile.name;
                    user.email = profile.email;
                    
                    user.save(function(err) {
                        if(err){
                            return handleError(err, 500, res, "could not get save user");
                        }
                        var token = createJWT(user);
                        res.send({ token: token });
                    });
                });
            }
        });
    });

};

function createJWT(user) {
    var payload = {
        sub: user._id,
        iat: moment().unix(),
        exp: moment().add(1, 'day').unix()
    };
    return jwt.encode(payload, config.Secret);
}

// Basic err handler
function handleError(err, code, res, message){
    console.log("oauth err", err);
    res.status(code).send({ message : message });
}