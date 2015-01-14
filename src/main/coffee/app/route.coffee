HomeView        = require './home/home-view.html'
BooksController = require './books/books-controller'
BooksView       = require './books/books-view.html'

module.exports = ($stateProvider, $urlRouterProvider) ->
  $urlRouterProvider.otherwise '/books'
  $stateProvider
    .state 'home',
      url: '/'
      template: HomeView
    .state 'home.books',
      url: 'books',
      template:  BooksView
      controller:   BooksController