# the global application entry
route = require './route'
theme = require './theme'

angular
  .module 'BookLibraryApp', ['ui.router', 'ngMaterial']
  .config route
  #.config theme