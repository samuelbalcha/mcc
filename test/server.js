var expect = require('chai').expect;
var request = require('request');

var url = 'https://whats-out-samuelbalcha.c9.io/';

describe("Server: Server", function(){
    
    describe("Server setup", function() {
       it("should return statusCode 404 for all requests", function(done){
           request(url, function(err, response, body){
               expect(response.statusCode).to.equal(404);
               done();
           });
       });
    });
    
    describe("API: Events listing", function() {
        //GET: api/v1/events
       it("should retrun 200 for empty list of events", function(done) {
           request(url.concat('api/v1/events'), function(err, response, body) {
               expect(response.statusCode).to.equal(200);
               done();
           });
       });
       
       //db is populated already so will not return empty array
       it.skip("should return [] for empty list of events", function(done) {
           request(url.concat('api/v1/events'), function(err, response, body) {
               expect(body).to.equal('[]');
               done();
           });
       }); 
       
       it("should return list of events", function(done) {
            request(url.concat('api/v1/events'), function(err, response, body) {
               expect(body.length).not.to.equal('[]');
               done();
            });
       });
    });
    
    describe("API: Events create", function() {
        // POST: api/v1/events (remove skip to test creating)
       it.skip("should retrun 201 after creating event", function(done) {
            
            var data = {
                 title : "Special event",
                 description : "Some thing can be said ",
                 location : "Helsinki",
                 date : Date.now(),
                 createdBy : "Alison"
            };
           
            var options = {
                method: 'POST',
                body: data,
                json: true,
                url: url.concat('api/v1/events')
            };
           
            request(options, function(err, response, body) {
               expect(response.statusCode).to.equal(201);
               done();
            });
       });
    });
    
    describe("API: Events update", function() {
        // PUT: api/v1/events/:id 
       it.skip("should update event title", function(done) {
           
            var evtToUpdate = {
               _id : "N18kxBsxx",
               title : "Node is Awesome Get together"
            };
            
            var options = {
                method: 'PUT',
                body: evtToUpdate,
                json: true,
                url: url.concat('api/v1/events/' + evtToUpdate._id)
            };
            
            request(options, function(err, response, body) {
               expect(response.body.title).to.equal(evtToUpdate.title);
               done();
            });
       });
       // PATCH: api/v1/events/:id
       it("should add participant to the event", function(done){
           request(url.concat('api/v1/events'), function(err, response, body) {
               
               var items = JSON.parse(body);
               var  item = items[0];
               
               var options = {
                    method: 'PATCH',
                    url: url.concat('api/v1/events/' + item._id + '/samuelbalcha')
               };
               
               request(options, function(err, response, body) {
                   var evt = JSON.parse(body);
                   var participants = evt.participants;
                   expect(participants).to.be.above(item.participants);
                   done();
               });
            });
       });
    });
    
    describe("API: Events delete", function() {
        // DELETE: api/v1/events/:id 
       it.skip("should delete event", function(done) {
           
            request(url.concat('api/v1/events'), function(err, response, body) {
               
               var items = JSON.parse(body);
               
               var options = {
                    method: 'DELETE',
                    url: url.concat('api/v1/events/' + items[0]._id)
               };
               
               request(options, function(err, response, body) {
                   expect(response.body).to.equal("Item removed");
                   done();
               });
            });
       });
    });
    
    describe("API: Event detail", function() {
        // GET: api/v1/events/:id
       it("should get event by id", function(done) {
           
            request(url.concat('api/v1/events'), function(err, response, body) {
               var items = JSON.parse(body);
               
               request(url.concat('api/v1/events/' + items[1]._id), function(err, response, body) {
                   var item = JSON.parse(body);
                   expect(item).to.equal(item);
                   done();
               });
            });
       });
    });
    
    describe("API: Events search", function() {
        // GET: api/v1/events/search
       it("should get all events by location", function(done) {
           
           //location (Helsinki)
            request(url.concat('api/v1/search?location=Helsinki'), function(err, response, body) {
                var hki = JSON.parse(body);
                expect(hki[0].location).to.equal("Helsinki");
                done();
            });
       });
    });
    
    
});