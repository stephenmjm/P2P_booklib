{compose, append, map, reduce, filter, where, range, nth, add, subtract, modulo, forEach, toPairs, keys} = require 'ramda'

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
  controller: ($scope, screenSize)->
    screenSize.rules =
      lg: '(min-width: 960px)'
      md: '(min-width: 600px) and (max-width: 960px)'
      sm: '(min-width: 480px) and (max-width: 600px)'
      xs: '(max-width: 480px)'
    mapping = lg: 6, md: 4, sm: 3, xs: 2
    transform = (columns, books)->
      matrixfy = (matrix, book, index, books)->
        r = (index + 1) % columns or columns
        matrix[r - 1] = (append book, matrix[r - 1])
        matrix
      reduce.idx matrixfy, (map (-> []), (range 1, columns + 1)), books
    render = (columns)=>
      if @columns isnt columns
        @columns = columns
        $scope.matrix = transform @columns, $scope.books
    (compose (forEach render),
      (map (rule)-> mapping[rule]),
      (filter (rule)-> screenSize.is rule),
      keys) screenSize.rules
    (compose (forEach ((item)->
        screenSize.on item[0], (matched)-> render item[1] if matched)),
      toPairs) mapping