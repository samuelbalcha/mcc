'use strict';
                  // app name, dependencies 
angular.module('whatsOut',  ['ngResource', 'ngMessages', 'ngAnimate', 'toastr', 'ui.router',
        'satellizer', 'ui.bootstrap', 'ngSanitize', 'ui.bootstrap.datetimepicker'])
       .config(function($stateProvider, $urlRouterProvider, $authProvider){
      
        // state provider
        // state name, configs 
        $stateProvider.state('list', { url : '/', templateUrl : 'event/list.html' });
        $stateProvider.state('detail', { url : '/events/:id', templateUrl : 'event/detail.html' });
        $stateProvider.state('add', { url : '/events/add', templateUrl : 'event/detail.html' });
        $stateProvider.state('sync', { url : '/sync', template: null, controller: 'EventSyncCtrl' });
        $stateProvider.state('gsync', { url : '/gsync', template: null,
            controller: function($scope, $http, $stateParams, $location){

                console.log('here');
                $http.get('/api/v1/gsync').success(function(data){
                    $location.path('/');
                }).error(function(err){
                        alert(err);
                });

        } });

       $stateProvider.state('login', { url : '/login', templateUrl : '/auth/login.html' ,
            controller: 'LoginCtrl',
            resolve: {
            skipIfLoggedIn: skipIfLoggedIn
        }});
        $stateProvider.state('logout', { url : '/logout', templateUrl : null });

        // unmatched urls/states go here
        $urlRouterProvider.otherwise("/");

        $authProvider.google({
            clientId: '256088856107-od5mglacb5qr4jerua7s1bpln7n2fgp8.apps.googleusercontent.com'
        });

        function skipIfLoggedIn($q, $auth) {
            var deferred = $q.defer();
            if ($auth.isAuthenticated()) {
                deferred.reject();
            } else {
                deferred.resolve();
            }
            return deferred.promise;
        }

        function loginRequired($q, $location, $auth) {
            var deferred = $q.defer();
            if ($auth.isAuthenticated()) {
                deferred.resolve();
            } else {
                $location.path('/login');
            }
            return deferred.promise;
        }
});

angular.module('whatsOut').factory('syncService', function ($location) {
    return function () {
        //Auth.setUser(undefined);

        console.log($location);
        console.log(this);


        //$location.path('/');
    }
});

/*angular.module('whatsOut', ['ngResource', 'ngMessages', 'ngAnimate', 'toastr', 'ui.router'])
angular.module('whatsOut', ['ngResource'])
 .config(function($stateProvider, $urlRouterProvider, $authProvider) {

        /*$stateProvider
            .state('home', {
                url: '/',
                controller: 'HomeCtrl',
                templateUrl: 'partials/home.html'
            })
            .state('login', {
                url: '/login',
                templateUrl: 'partials/login.html',
                controller: 'LoginCtrl',
                resolve: {
                    skipIfLoggedIn: skipIfLoggedIn
                }
            })
            .state('signup', {
                url: '/signup',
                templateUrl: 'partials/signup.html',
                controller: 'SignupCtrl',
                resolve: {
                    skipIfLoggedIn: skipIfLoggedIn
                }
            })
            .state('logout', {
                url: '/logout',
                template: null,
                controller: 'LogoutCtrl'
            })
            .state('profile', {
                url: '/profile',
                templateUrl: 'partials/profile.html',
                controller: 'ProfileCtrl',
                resolve: {
                    loginRequired: loginRequired
                }
            });

        $urlRouterProvider.otherwise('/');

        $authProvider.google({
            clientId: '256088856107-od5mglacb5qr4jerua7s1bpln7n2fgp8.apps.googleusercontent.com'
        });

        function skipIfLoggedIn($q, $auth) {
            var deferred = $q.defer();
            if ($auth.isAuthenticated()) {
                deferred.reject();
            } else {
                deferred.resolve();
            }
            return deferred.promise;
        }

        function loginRequired($q, $location, $auth) {
            var deferred = $q.defer();
            if ($auth.isAuthenticated()) {
                deferred.resolve();
            } else {
                $location.path('/login');
            }
            return deferred.promise;
        }
    });*/

