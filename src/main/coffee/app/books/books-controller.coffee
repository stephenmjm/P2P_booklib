{map, range} = require 'ramda'

module.exports = ($scope) ->
  # generate dummy books
  $scope.books = map (idx) ->
      title: "Angular in Action"
      author: "James Tong"
      iconUrl: "http://lorempixel.com/106/158/people/" + idx
      notes: "A fantastic tech book."
    , range 1, 10