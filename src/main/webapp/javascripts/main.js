angular.module('BookLibraryApp', [
    'ngRoute',
    'akoenig.deckgrid',
    'ngMaterial'
]);

angular.module('BookLibraryApp').config([

    '$routeProvider',

    function configure ($routeProvider) {

        'use strict';

        $routeProvider.when('/', {
            controller: 'HomeController',
            templateUrl: 'templates/home.html',
            resolve: HomeController.resolve
        });

    }
]);