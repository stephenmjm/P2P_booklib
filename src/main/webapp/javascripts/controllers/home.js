HomeController = function($scope, $mdSidenav, books) {

        $scope.books =  books;

        $scope.showBookDetail = function(book) {
            $mdSidenav('detail').toggle();
            $scope.selectedBook = book;
        };

        $scope.showRight = function(value) {
            alert(value)
        }

        $scope.borrow = function() {
            alert("Borrow")
        };
}

HomeController.resolve = {
    books : function($q, $http) {
        var deferred = $q.defer();

        $http.get('http://localhost:8080/rest/books')
                .success(function(response) {
                    deferred.resolve(response);
                });

        return deferred.promise;
    }
}

angular.module('BookLibraryApp').controller('HomeController', HomeController);
