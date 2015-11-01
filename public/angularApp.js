'use strict';
                  // app name, dependencies 
angular.module('whatsOut', ['ngResource','ui.router', 'satellizer'])
       .config(function($stateProvider, $urlRouterProvider, $authProvider){
      
        // state provider
        // state name, configs 
        $stateProvider.state('list', { url : '/', templateUrl : 'event/list.html' });
        $stateProvider.state('detail', { url : '/events/:id', templateUrl : 'event/detail.html' });
        
        // unmatched urls/states go here
        $urlRouterProvider.otherwise("/");
    
    
        // auth provider
        // googel client id
       // $authProvider.google({ clientId: 'google client id' });  
});