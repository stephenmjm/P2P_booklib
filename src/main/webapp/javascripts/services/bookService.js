angular.module('BookLibraryApp').service('bookService', function($http) {

        this.listAllBook = function() {
            var books;
            $http.get('http://localhost:8080/rest/books')
                .success(function(response) {
                    books = response;
                });

            return books;
        }
});
