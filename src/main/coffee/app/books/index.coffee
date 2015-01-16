
ImageLoaded = require './image.loaded.directive'
BooksRouter = require './books.router'
Books       = require './books.service'

module.exports = angular.module 'books', ['ui.router', 'akoenig.deckgrid', 'ngMaterial']
  .service 'Books', Books
  .config BooksRouter
  .directive 'imageLoaded', [ImageLoaded]