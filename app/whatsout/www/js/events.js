angular.module('whatsout.events', ['ngResource', 'whatsout.config'])
    // Routes
    .config(function ($stateProvider) {
        $stateProvider
            .state('app.events', {
                url: "/events",
                views: {
                    'menuContent' :{
                        templateUrl: "templates/events.html",
                        controller: "EventsListCtrl"
                    }
                }
            })

            .state('app.event', {
                url: "/events/:eventId",
                views: {
                    'menuContent' :{
                        templateUrl: "templates/event.html",
                        controller: "EventCtrl"
                    }
                }
            })

    })
    .factory('Event', function ($resource, SERVER_PATH) {
        return $resource(SERVER_PATH + '/events/:eventId');
    })

    .controller('EventsListCtrl', function ($scope, Event, SERVER_PATH) {
        $scope.serverPath = SERVER_PATH;
        $scope.events = Event.query();
    })

    .controller('EventCtrl', function ($scope, $stateParams, Event, SERVER_PATH) {
        $scope.serverPath = SERVER_PATH;
        $scope.event = Event.get({eventId: $stateParams.eventId});

        $scope.push = function(event) {
            //Notification.push({message: "Check out this session: " + $scope.session.title});
        }

        /*$scope.share = function(event) {
            openFB.api({
                method: 'POST',
                path: '/me/feed',
                params: {
                    message: "I'll be attending: '" + $scope.session.title + "' by " +
                        $scope.session.speaker
                },
                success: function () {
                    alert('The session was shared on Facebook');
                },
                error: function () {
                    alert('An error occurred while sharing this session on Facebook');
                }
            });
        };*/

    });