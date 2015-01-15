{map, range} = require 'ramda'

module.exports = ($scope) ->
  # generate dummy books
  $scope.books = map (idx) ->
      title: "Angular in Action"
      author: "James Tong"
      iconUrl: "http://lorempixel.com/600/800/people/" + idx
      notes: "A fantastic tech book."
    , range 1, 10