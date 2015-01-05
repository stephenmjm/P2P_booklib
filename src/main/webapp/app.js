var app = angular.module('BookLibraryApp', ['ngMaterial'])

// Book list controller, contollers will be defined in seperated files later
app.controller('BookShelfCtrl', function($scope) {
  // generate dummy books
  $scope.books = R.map(function(idx) {
    return {
      title: "Angular in Action",
      author: "James Tong",
      iconUrl: "http://lorempixel.com/106/158/people/" + idx,
      notes: "A fantastic tech book."
    };
  }, R.range(1, 10));
});