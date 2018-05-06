var app = angular.module('appServices', [])

app.factory('Request', ['$http', function($http) {
    return {
			getAll : function(){
                return $http.get("https://hackqc.herokuapp.com/api/alertes");
            },
            googleAPI : function(q){
                return $http.get("https://maps.googleapis.com/maps/api/geocode/json?&address=" + q)
            },
            getUserPins : function(){
                return $http.get("https://hackqc.herokuapp.com/api/getUserAlerts");
            },
            getHisto : function(){
                return $http.get("https://hackqc.herokuapp.com/api/getHisto");
            }
        }
}]);
