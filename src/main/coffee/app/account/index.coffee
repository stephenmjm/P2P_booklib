Accounts = require './accounts.service'

angular.module 'accounts', ['ngStorage']
.factory 'Accounts', Accounts