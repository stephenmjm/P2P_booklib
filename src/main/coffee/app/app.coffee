# the global application entry
route       = require './route'
theme       = require './theme'
loadEffect  = require './helper/load-effect'

angular
  .module 'BookLibraryApp', ['ui.router', 'akoenig.deckgrid', 'ngMaterial']
  .config route
  .directive 'imageloaded', [loadEffect]
  #.config theme