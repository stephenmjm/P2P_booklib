BooksTransclude  = require './books.transclude.directive'
ImageLoaded   = require './image.loaded.directive'
BooksShelf    = require './books.shelf.directive'
BooksRouter   = require './books.router'
Books         = require './books.service'

module.exports = angular.module 'books', ['ui.router', 'matchMedia', 'ngMaterial']
  .service 'Books', Books
  .config BooksRouter
  .directive 'booksTransclude', BooksTransclude
  .directive 'imageLoaded', ImageLoaded
  .directive 'booksShelf', BooksShelf