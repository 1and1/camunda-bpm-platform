"use strict";

define(["angular"], function(angular) {

  var module = angular.module("tasklist.pages");

  var Controller = function($scope, $location, EngineApi, Authentication) {
    var currentUser = Authentication.current();

    if (!currentUser) {
      return;
    }

    var tasks;
        
    $scope.colleagueCount = {};

    $scope.loadSitebar = function () {
      tasks = $scope.tasks = {
        mytasks: EngineApi.getTaskCount().get({ "assignee" : currentUser }),
        unassigned: EngineApi.getTaskCount().get({ "candidateUser" : currentUser })
      };

      $scope.groupInfo = EngineApi.getGroups(currentUser);
      
      $scope.groupInfo.$then(function(data){
        	
          angular.forEach(data.data.groupUsers, function(user) {    
        	  
      	    EngineApi.getColleagueCount(user.id)
      	      .$then(function(data) {
      	    	  
                $scope.colleagueCount[user.id] = data.data.count;   
                
      	  }); 
      	    
      	});            
      });
           
      
    };

    $scope.isActive = function(filter, search) {
      var params = $location.search();
      return (params.filter || "mytasks") == filter && params.search == search;
    };
    
    $scope.getColleagueCount = function(userId) {
    };
      
    $scope.$on("tasklist.reload", function () {
      $scope.loadSitebar();
    });

    $scope.loadSitebar();
  };

  Controller.$inject = ["$scope", "$location", "EngineApi", "Authentication"];

  var RouteConfig = function($routeProvider) {
    $routeProvider.when("/overview", {
      templateUrl: "pages/overview.html",
      controller: Controller
    });
  };

  RouteConfig.$inject = [ "$routeProvider" ];

  module
    .config(RouteConfig)
    .controller("SitebarController", Controller);
});