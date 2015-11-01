'use strict';

var Event = require('../models/events').Events;

/**
 * Gets all events and returns a sorted list by dateCreated
 */
exports.getAllEvents = function(req, res, next){
    
    Event.find().sort({ dateCreated : 'desc'}).exec(function(err, evts){
        if(err){
            res.status(500);
        }
        if(!evts){
            res.status(404);
        }
        else{
           
            res.status(200).send(evts);
        }
    });
};

/**
 * Returns event by id
 */
exports.getEvent = function(req, res, next){
    Event.findOne({ _id : req.params.id }, function(err, evt){
        if(err){
            res.status(500);
        }
        if(!evt){
            res.status(404);
        }
        else{
            res.status(200).send(evt);
        }
    });
};

/**
 * Search events by location & createdBy
 * ability to extend by date and other fields for v2
 */
exports.searchEvents = function(req, res, next){
   
    var condition;
    if(req.query.location){
       condition = { location : req.query.location };
    }
    if(req.query.title){
        condition = { title : req.query.title };
    }
    /**
    if(req.query.date){
        condition = { createdBy : req.query.createdBy };
    }
   */
   
    Event.find(condition, function(err, evts){
        if(err){
            res.status(500);
        }
        if(!evts){
            res.status(404);
        }
        else{
            res.status(200).send(evts);
        }
    });
};


/**
 * Creats new event
 */ 
exports.createEvent = function(req, res, next){
    var par = req.body;
    
    var evt = new Event({
        title : par.title,
        description :  par.description,
        location :  par.location,
        date : par.date,
        createdBy : par.createdBy
    });
    
    evt.save(function(err){
        if(err){
            res.status(500).send(":(");
            next(err);
        }
        res.status(201).send(evt);
    });
};

/**
 * Updates event
 */ 
exports.updateEvent = function(req, res, next){
    var par = req.body;
    
    Event.findById(req.params.id, function(err, evt){
        if(err){
            res.status(500);
        }
        if(!evt){
            res.status(404);
        }
        else{
            evt.title = par.title || evt.title;
            evt.description = par.description || evt.description;
            evt.location = par.location || evt.location;
            evt.date = par.date || evt.date;
            
            evt.save(function(err){
                if(err){
                    res.status(500);
                }
                res.status(200).send(evt);
            });
        }
    });
};

/**
 * Removes event
 */
exports.removeEvent = function(req, res, next){
    Event.findOneAndRemove({ _id : req.params.id }, function(err, evt){
        if(err){
            res.status(500);
        }
        if(!evt){
            res.status(404);
        }
        else{
            res.status(200).send("Item removed");
        }
    });
};

/**
 * Patches event with a participant
 */ 
exports.addParticipantToEvent = function(req, res, next){
    var participant = req.params.person;
   
    Event.findById(req.params.id, function(err, evt){
        if(err){
            res.status(500);
        }
        if(!evt){
            res.status(404);
        }
        else{
            
            evt.participants.push(participant);
            
            evt.save(function(err){
                if(err){
                    res.status(500);
                }
                res.status(200).send(evt);
            });
        }
    });
};
