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
});