HomeRouter = require './library.router'

module.exports = angular.module 'library', ['ui.router', 'ngMaterial']
  .config HomeRouter