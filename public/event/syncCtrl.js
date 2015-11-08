angular.module('whatsOut').controller('EventSyncCtrl', function($scope, $http, $stateParams, $location){

    'use strict';

    init();

    function init(){

        $http.get('/gauth').success(function(response){

            if(response && response.items) {
                for(var i= 0; i < response.items.length; i++) {

                    var event = {
                        title : response.items[i].summary,
                        description :  response.items[i].summary + '<br/><a target="_blank" href="'+response.items[i].htmlLink + '">(link)</a>',
                        location :  response.items[i].location,
                        date : response.items[i].start.dateTime,
                        dateEnd : response.items[i].end.dateTime,
                        createdBy : response.items[i].creator.displayName,
                        gid: response.items[i].id
                    };

                    $http.post('/api/v1/sync/', event).success(function(data){
                        $location.path('/');
                    }).error(function(err){
                            alert(err);
                    });
                }
            }
            //$scope.eventsList = response;
        }).error(function(err){

        });
    }
});