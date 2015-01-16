# the global application entry
require './library'
require './books'

angular
  .module 'BookLibraryApp', ['library', 'books']
  .config (require './app.router')