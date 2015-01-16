
module.exports = ($scope, Books) ->
  # generate dummy books
  $scope.books = Books.all()