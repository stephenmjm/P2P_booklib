# the global application entry
route = require './route'

angular
  .module 'BookLibraryApp', ['ui.router', 'ngMaterial']
  .config route