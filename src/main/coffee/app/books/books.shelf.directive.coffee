{append, map, reduce, filter, where, range, nth, add, subtract, modulo} = require 'ramda'

module.exports = ->
  restict: 'A'
  transclude: true
  scope: 
    books: "="
  template: """
      <md-list layout="column" ng-repeat="books in matrix">
        <md-item ng-repeat="book in books" books-transclude>
        </md-item>
      </md-list>
  """
  link: (scope, element, attrs, ctrl, transclude)->
    #transclude scope, (clone, scope)->
    #element.append(clone)

    #mql = window.matchMedia("(min-width:1000px)");
  controller: ($scope)->
    columns = 6
    matrixfy = (matrix, book, index, books)->
      r = (index + 1) % columns or columns
      matrix[r - 1] = (append book, matrix[r - 1])
      matrix
    $scope.matrix = (reduce.idx matrixfy, (map (-> []), (range 1, columns + 1)), $scope.books)