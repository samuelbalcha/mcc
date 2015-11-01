angular.module('whatsOut').controller('EventListCtrl', function($scope, $http){
   
   'use strict';
   
    init();
    
    function init(){
       
        $http.get('/api/v1/events').success(function(response){
           $scope.eventsList = response;
        }).error(function(err){
            alert(err);
        });
    }
});