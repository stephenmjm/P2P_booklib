{map, range} = R
app = angular.module 'BookLibraryApp', ['ngMaterial']

# Book list controller, contollers will be defined in seperated files later
app.controller 'BookShelfCtrl', ($scope) ->
  # generate dummy books
  $scope.books = map (idx) ->
      title: "Angular in Action"
      author: "James Tong"
      iconUrl: "http://lorempixel.com/106/158/people/" + idx
      notes: "A fantastic tech book."
    , range 1, 10