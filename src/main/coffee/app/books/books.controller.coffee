
module.exports = ($scope, Books) ->
  # generate dummy books
  $scope.books = Books.all()
  $scope.search = ->
    $scope.books = [] #Books.filter $scope.term