LibraryView = require './library.view.html'

module.exports = ($stateProvider)->
  $stateProvider
    .state 'library',
      url: '/'
      template: LibraryView