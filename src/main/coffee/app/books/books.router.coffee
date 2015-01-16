BooksController = require './books.controller'
BooksView       = require './books.view.html'

module.exports = ($stateProvider) ->
  $stateProvider
    .state 'library.books',
      url: 'books'
      template: BooksView
      controller: BooksController