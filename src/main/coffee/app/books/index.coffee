BooksRouter = require './books.router'
ImageLoaded = require './image.loaded.directive'

module.exports = angular.module 'books', ['ui.router', 'akoenig.deckgrid', 'ngMaterial']
  .config BooksRouter
  .directive 'imageLoaded', [ImageLoaded]