// Define the `phonecatApp` module
var App = angular.module('greeting', []);

// Define the `PhoneListController` controller on the `phonecatApp` module
App.controller('greetingController', ['$scope', '$http', function($scope, $http) {

    var nom = "CP";



    $http.get('/api/greeting?name='+ nom+"&annee=1994").then(function(data){
        console.log(data);
        $scope.personne = data.data;
    }, function(err){
        console.log(err);
    });

    $scope.update = function(user){
        $http.get('/api/greeting?name='+ user.name +"&annee=" + user.annee).then(function(data){
                $scope.personne = data.data;
            });
    }

}]);