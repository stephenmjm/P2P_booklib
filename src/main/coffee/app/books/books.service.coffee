{filter, map, range, where} = require 'ramda'

createBook = (num)->
  title: "Angular in Action"
  owner: "James Tong"
  iconUrl: "http://lorempixel.com/600/800/people/" + num
  notes: "A fantastic tech book."

module.exports = ()->
  books = (map createBook, (range 1, 30))
  all: -> books
  where: (condition)->
    (filter (where condition), books)