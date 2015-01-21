{filter, map, range, where} = require 'ramda'

createBook = (num)->
  title: "Angular in Action"
  owner: "James Tong"
  iconUrl: "http://placehold.it/600x800&text=Book + #{num}"
  notes: "A fantastic tech book."

module.exports = ()->
  books = (map createBook, (range 1, 30))
  all: -> books
  where: (condition)->
    (filter (where condition), books)