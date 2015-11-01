'use strict';

var mongoose = require('mongoose'),
    Schema = mongoose.Schema;
var shortid = require('shortid'); 

//Users schema 
var userSchema = new Schema({
    _id: {
        type: String,
        unique: true,
        'default': shortid.generate
    },
    
    displayName : { type: String, trim : true },
    
    email: {
        type: String,
        unique : true,
        trim: true,
        match: [/.+\@.+\..+/, 'Please fill a valid email address']
    },
    
    avatar: { type: String, trim: true},
    google : String,
});

var Users = mongoose.model('Users', userSchema);
module.exports = { Users : Users };