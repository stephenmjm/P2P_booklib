module.exports = ->
  restrict: 'A'
  link: (scope, element, attrs)->
    element.bind 'load', ->
      angular.element element
             .addClass attrs.loadedclass