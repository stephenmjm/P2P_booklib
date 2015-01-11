angular.module('BookLibraryApp').controller('HomeController', function($scope, $mdSidenav) {

        init();

        function init() {
            $scope.books = [
            {"id":1, "title": "大数据日知录", "smallImage":"http://img3.douban.com/mpic/s27464965.jpg","image": "http://img3.douban.com/lpic/s27464965.jpg", "author":"张俊林", "summary": ""},        
            {"id":2, "title": "Redis设计与实现", "smallImage":"http://img3.douban.com/mpic/s27297117.jpg","image": "http://img5.douban.com/lpic/s27297117.jpg", "author":"黄健宏", "summary": ""},
            {"id":3, "title": "领域专用语言实战", "smallImage":"http://img3.douban.com/mpic/s27080718.jpg","image": "http://img5.douban.com/lpic/s27080718.jpg", "author":"[美] Debasish Ghosh", "summary": ""},
            {"id":4, "title": "失控", "smallImage":"http://img3.douban.com/mpic/s4554820.jpg","image": "http://img3.douban.com/lpic/s4554820.jpg", "author":"[美] 凯文·凯利", "summary": ""},
            {"id":5, "title": "黑客与画家", "smallImage":"http://img3.douban.com/mpic/s4669554.jpg","image": "http://img3.douban.com/lpic/s4669554.jpg", "author":"[美] Paul Graham", "summary": ""},
            {"id":6, "title": "启示录", "smallImage":"http://img3.douban.com/mpic/s4663406.jpg","image": "http://img5.douban.com/lpic/s4663406.jpg", "author":"[美] Marty Cagan", "summary": ""},
            {"id":7, "title": "创业时, 我们在知乎聊什么?", "smallImage":"http://img3.douban.com/mpic/s27196450.jpg","image": "http://img5.douban.com/lpic/s27196450.jpg", "author":"知乎", "summary": ""}
            ];

            $scope.selectedBook = {};
        }

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
    });
