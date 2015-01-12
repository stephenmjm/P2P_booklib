BooksController = require './books/books-controller'
BooksView       = require './books/books-view.html'
BookController  = require './books/book-controller'
BookView        = require './books/book-view.html'

module.exports = ($routeProvider, $locationProvider) ->
  $routeProvider
    .when '#/books',
      templateUrl:  BookView
      controller:   BookController
    .when '#/books/:id',
      templateUrl:  BooksView
      controller:   BookController
  $locationProvider.html5Mode true