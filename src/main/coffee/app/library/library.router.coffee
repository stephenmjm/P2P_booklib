LibraryView = require './library.view.html'
LibraryController = require './library.controller'

module.exports = ($stateProvider)->
  $stateProvider
    .state 'library',
      url: '/'
      controller: LibraryController
      template: LibraryView