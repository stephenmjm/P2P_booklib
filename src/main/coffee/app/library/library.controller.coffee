SignupController = require './signup.controller'
SignupDialog = require './signup.dialog.html'

module.exports = ($scope, $mdDialog, Accounts)->
  $scope.email = if Accounts.authenticated() then Accounts.session().email else ""
  $scope.authenticated = Accounts.authenticated()
  $scope.signup = (ev)->
    $mdDialog.show
      controller: SignupController
      template: SignupDialog
      targetEvent: ev
    .then (account)->
      $scope.email = Accounts.session().email
      $scope.authenticated = true
    , ->
      $scope.authenticated = false
  $scope.signout = (ev)->
    Accounts.signout().then -> $scope.authenticated = false
