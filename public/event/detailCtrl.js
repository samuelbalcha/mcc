angular.module('whatsOut').controller('EventDetailCtrl', function($scope, $http, $stateParams, $location){
    
    'use strict';
    $scope.editMode = false;
    $scope.addMode = false;

    init();

    function init(){

        if($stateParams.id != 'add') {
            $http.get('/api/v1/events/' + $stateParams.id).success(function(data){
                $scope.eventDetail = data;
            }).error(function(err){
               alert(err);
            });
        } else {
            $scope.eventDetail = {};
            $scope.editMode = true;
            $scope.addMode = true;
        }
    }
    
    $scope.save = function(){

        if($scope.eventDetail._id) {
            $http.put('/api/v1/events/' + $scope.eventDetail._id , $scope.eventDetail).success(function(data){
              $scope.eventDetail = data;
              $scope.editMode = false;
            }).error(function(err){
              alert(err);
            });
        } else {
            $http.post('/api/v1/events/', $scope.eventDetail).success(function(data){
                $location.path('/events/'+data._id);
            }).error(function(err){
                    alert(err);
           });
        }
    };
    
    $scope.remove = function(){
        $http.delete('/api/v1/events/' + $scope.eventDetail._id).success(function(response){
            $location.path('/events');
        }).error(function(err){
            alert(err);
        });
    };

});