# fix the sope issue of transclude directive
module.exports = ->
  restrict: 'EAC'
  link: (scope, element, attributes, controller, transclude)->
    childScope = scope.$new()
    transclude childScope, (clone)->
      element.empty()
      element.append clone
      element.on '$destroy', ->
        childScope.$destroy()
