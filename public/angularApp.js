'use strict';
                  // app name, dependencies 
angular.module('whatsOut',  ['ngResource', 'ngMessages', 'ngAnimate', 'toastr', 'ui.router',
        'satellizer', 'ui.bootstrap', 'ngSanitize', 'ui.bootstrap.datetimepicker'])
       .config(function($stateProvider, $urlRouterProvider, $authProvider){
      
        // state provider
        // state name, configs 
        $stateProvider.state('list', { url : '/', templateUrl : 'event/list.html', controller: 'EventListCtrl' });
        $stateProvider.state('detail', { url : '/events/:id', templateUrl : 'event/detail.html' });
        $stateProvider.state('add', { url : '/events/add', templateUrl : 'event/detail.html' });
        $stateProvider.state('sync', { url : '/sync', template: null, controller: 'EventSyncCtrl' });
        $stateProvider.state('week', { url : '/week', templateUrl : 'event/list.html', controller: 'WeekListCtrl' });

        $stateProvider.state('gsync', { url : '/gsync', template: null,
            controller: function($window, $scope, $http, $stateParams, $location){
                $http.get('/api/v1/gsync').success(function(data){
                    if(data.url) {
                        $window.location.href  = data.url;
                    } else {
                        $location.path('/');
                    }
                }).error(function(err){
                        alert('Unable to sync to calendar. Please refresh the page.')
                });

        } });

});

