{pick} = require 'ramda'

module.exports = ($scope, $mdDialog, Accounts)->
  $scope.cancel = ->
    $mdDialog.hide()

  $scope.signup = ->
    account = $scope.account
    if account.confirmPassword is account.password
      Accounts.signup pick(['email', 'password'], account)
      .then (account)->
        $mdDialog.hide(account)
      .catch (error)->
        #$mdDialog.hide(error)

  $scope.signin = ->
    account = $scope.account
    Accounts.signin pick(['email', 'password'], account)
    .then (account)->
      $mdDialog.hide(account)
    .catch (error)->
      #$mdDialog.hide(error)