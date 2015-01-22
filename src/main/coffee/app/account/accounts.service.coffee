{compose, split, head, pick, nth} = require 'ramda'
{decode} = (require 'js-base64').Base64

parseToken = (token)-> JSON.parse(
  decode(
    nth(1, split('.', token))))

module.exports = ($http, $sessionStorage, $q)->
  session = if $sessionStorage.token? then parseToken $sessionStorage.token else null

  authenticated: -> session isnt null

  session: -> session

  signup: (account)->
    deferred = $q.defer()
    $http.post '/rest/account/signup', account
    .success (res)->
      $sessionStorage.token = res.token
      session = parseToken res.token
      deferred.resolve (pick ['email'], res)
    .error (error)->
      deferred.reject error
    deferred.promise
  
  signin: (account)->
    deferred = $q.defer()
    $http.post '/rest/account/signin', account
    .success (res)->
      $sessionStorage.token = res.token
      session = parseToken res.token
      deferred.resolve (pick ['email'], res)
    .error (error)->
      deferred.reject error
    deferred.promise

  signout: ->
    session = null
    delete $sessionStorage.token
    deferred = $q.defer()
    deferred.resolve({})
    deferred.promise
    #$http.post '/rest/account/signout'
  
  profile: (account)->
    $http.get '/rest/account/profile'

