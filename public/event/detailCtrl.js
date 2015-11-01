angular.module('whatsOut').controller('EventDetailCtrl', function($scope, $http, $stateParams, $location){
    
    'use strict';
    $scope.editMode = false;
    
    init();
    
    function init(){
        
        $http.get('/api/v1/events/' + $stateParams.id).success(function(data){
            $scope.eventDetail = data;
        }).error(function(err){
           alert(err); 
        });
    }
    
    $scope.save = function(){
        $http.put('/api/v1/events/' + $scope.eventDetail._id , $scope.eventDetail).success(function(data){
          $scope.eventDetail = data;
          $scope.editMode = false;
        }).error(function(err){
          alert(err);
        });  
    };
    
    $scope.remove = function(){
        $http.delete('/api/v1/events/' + $scope.eventDetail._id).success(function(response){
            $location.path('/events');
        }).error(function(err){
            alert(err);
        });
    };
    
});