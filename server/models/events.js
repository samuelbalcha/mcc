'use strict';

var mongoose = require('mongoose'),
    Schema = mongoose.Schema;
var shortid = require('shortid'); //convinient short ids

//Events schema (ability to extend createdBy to be a reference to a user data)
var eventSchema = new Schema({
    _id: {
        type: String,
        unique: true,
        'default': shortid.generate
    },
    
    title : { type: String, trim : true },
    description :  { type: String, trim : true },
    location :  { type: String, trim : true },
    date : Date,
    dateEnd : Date,
    createdBy : String,
    dateCreated : Date,
    gid: String, // google calendar id
    participants : [ { type : String }]
});

// pre-save hook (set datecreated before saving the model)
eventSchema.pre('save', function(next){
    var ev = this;
    ev.dateCreated = Date.now();
    next();
});

var Events = mongoose.model('Events', eventSchema);
module.exports = { Events : Events };