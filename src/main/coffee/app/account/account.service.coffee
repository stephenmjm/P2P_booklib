{compose, split, head} = require 'ramda'
{decode} = require 'Base64'

parseToken = compose  JSON.parse,         # parse json
                      decode,             # decode base 64 string
                      (items)-> (nth 1),  # get 2th item
                      (split '.'))        # split token

module.exports = ($http, $sessionStorage, $q)->
  user = if $sessionStorage.token? then parseToken token else {}

  register: (user)->
    $http.post '/rest/account/register', user
  
  signin: (user)->
    $http.post '/rest/account/authenticate', user
  
  profile: (user)->
    $http.get '/rest/account/profile'
  
  logout: ->
    delete user
    delete $sessionStorage.token
    $q.when {}
