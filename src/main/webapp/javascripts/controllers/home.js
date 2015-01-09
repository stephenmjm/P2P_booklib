angular.module('BookLibraryApp').controller('HomeController', function($scope, $mdSidenav) {

        init();

        function init() {
            $scope.books = [
            {"title": "大数据日知录", "image": "http://img3.douban.com/lpic/s27464965.jpg", "author":"张俊林", "summary": ""},        
            {"title": "Redis设计与实现", "image": "http://img5.douban.com/lpic/s27297117.jpg", "author":"黄健宏", "summary": ""},
            {"title": "领域专用语言实战", "image": "http://img5.douban.com/lpic/s27080718.jpg", "author":"[美] Debasish Ghosh", "summary": ""},
            {"title": "失控", "image": "http://img3.douban.com/lpic/s4554820.jpg", "author":"[美] 凯文·凯利", "summary": ""},
            {"title": "黑客与画家", "image": "http://img3.douban.com/lpic/s4669554.jpg", "author":"[美] Paul Graham", "summary": ""},
            {"title": "启示录", "image": "http://img5.douban.com/lpic/s4663406.jpg", "author":"[美] Marty Cagan", "summary": ""},
            {"title": "创业时, 我们在知乎聊什么?", "image": "http://img5.douban.com/lpic/s27196450.jpg", "author":"知乎", "summary": ""}
            ];
        }

        $scope.toggleDetail = function() {
            $mdSidenav('detail').toggle();
        };

        $scope.close = function() {
            $mdSidenav('detail').close();
        };
    });
