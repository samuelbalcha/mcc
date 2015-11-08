angular.module('whatsOut').controller('WeekListCtrl', function($scope, $http){

    'use strict';

    init();

    function init(){

        $http.get('/api/v1/search', {params: {date: true}}).success(function(response){
            $scope.eventsList = response;
        }).error(function(err){
                alert(err);
            });
    }
});