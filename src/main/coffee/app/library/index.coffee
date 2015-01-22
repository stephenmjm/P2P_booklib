HomeRouter = require './library.router'
require '../account'

module.exports = angular.module 'library', ['ui.router', 'ngMaterial', 'ngMessages', 'accounts']
  .config HomeRouter