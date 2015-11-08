angular.module('starter.services', ['ngResource'])
    .factory('Event', function ($resource) {
        console.log('here');
        return $resource('http://localhost:3000/api/v1/events/:eventId');
    }
);

