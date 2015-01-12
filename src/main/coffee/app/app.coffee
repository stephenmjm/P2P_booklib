Route = require './route'

app = angular.module 'BookLibraryApp', ['ngRoute', 'ngMaterial']

app.controller 'MainController', ($scope, $route, $routeParams, $location) ->
  $scope.$route = $route;
  $scope.$location = $location;
  $scope.$routeParams = $routeParams;

app.config Route