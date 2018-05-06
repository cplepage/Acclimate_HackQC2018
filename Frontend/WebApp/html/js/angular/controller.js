var app = angular.module('appController', [])

app.controller('mainController', ['$scope', 'Request', function($scope, Request) {

    function parseData(data){
        var arr = [];
        for(i in data){
            data[i].alerte.risque = data[i].alerte.type;
            data[i].alerte.type = "Feature";
            delete data[i].alerte.id;
            var exist = false;
            for(j in arr){
                if(data[i].alerte.risque != arr[j].type){
                    continue;
                }
                else{
                    exist = true;
                    arr[j].data.push(data[i].alerte);
                    break;
                }
            }
            if(!exist){
                arr.push({
                    type: data[i].alerte.risque,
                    data: []
                });
                arr[arr.length - 1].data.push(data[i].alerte);
            }
        }
        return arr;
    }

    var histoAlertes = [];
    Request.getHisto().then(function(data){
        histoAlertes = parseData(data.data.alertes);
        //loadAllPins(histoAlertes);
        $scope.histos = loadLeftPaneMenu(histoAlertes, false);
    }, function(err){
        console.log(err);
    })

    var userAlertes = [];
    Request.getUserPins().then(function(data){
        userAlertes = parseData(data.data.alertes);
        loadAllPins(userAlertes);
        $scope.userPins = loadLeftPaneMenu(userAlertes, true);
    }, function(err){
        console.log(err);
    })

    var alertes = [];
    Request.getAll().then(function(data){
        alertes = parseData(data.data.alertes);
        loadAllPins(alertes);
        $scope.risques = loadLeftPaneMenu(alertes, true);
    })

    function loadLeftPaneMenu(alertes, show){
        var arr = [
            {
                show: show,
                title: "Tout",
                icon: "icon_acclimate.png",
                date:{
                    debut:"",
                    fin:""
                }
            }
        ]
        for(i in alertes){
            var icon = "../images/icon_pluie.png";
            switch (alertes[i].type){
                case "Inondation" :
                    icon = "../images/icon_goutte.png";
                    break;
                case "Suivi des cours d'eau" :
                    icon = "../images/icon_goutte.png";
                    break;
                case "Vent" :
                    icon = "../images/icon_vent.png";
                    break;
                case "Pluie" :
                    icon = "../images/icon_pluie.png";
                    break;
                case "Neige" :
                    icon = "../images/icon_pluie.png";
                    break;
                case "Meteo" :
                    icon = "../images/icon_vent.png";
                    break;
                case "Feu" :
                    icon = "../images/icon_feu.png";
                    break;
                case "Terrain" :
                    icon = "../images/icon_seisme.png";
                    break;
                case "Eau" :
                    icon = "../images/icon_goutte.png";
                    break;
            }
            arr.push({
                show: show,
                title: alertes[i].type,
                icon: icon,
                date:{
                    debut: "",
                    fin: ""
                }
            })
        }
        return arr;
    }

    $scope.showHideRisque = function(i, scope){
        if(scope == 'risques'){
            $scope.risques[i].show = !$scope.risques[i].show;
            if($scope.risques[i].show){
                if(i == 0){
                    for(j in $scope.risques){
                        if(j == 0) continue;
                        if(!$scope.risques[j].show){
                            loadPins(alertes[j-1]);
                            $scope.risques[j].show = true;
                        }
                    }
                    return;
                }
                loadPins(alertes[i-1]);
                for(var i = 1; i < $scope.risques.length; i++){
                    if($scope.risques[i].show == false){
                        return;
                    }
                }
                $scope.risques[0].show = true;
            }
            else{
                $scope.risques[0].show = false;
                if(i == 0){
                    for(j in $scope.risques){
                        if(j == 0) continue;
                        if($scope.risques[j].show){
                            removeLayer($scope.risques[j].title)
                            $scope.risques[j].show = false;
                        }
                    }
                    return;
                }
                removeLayer($scope.risques[i].title)
            }
        }else if (scope == 'userPins'){
            $scope.userPins[i].show = !$scope.userPins[i].show;
            if($scope.userPins[i].show){
                if(i == 0){
                    for(j in $scope.userPins){
                        if(j == 0) continue;
                        if(!$scope.userPins[j].show){
                            loadPins(userAlertes[j-1]);
                            $scope.userPins[j].show = true;
                        }
                    }
                    return;
                }
                loadPins(userAlertes[i-1]);
                for(var i = 1; i < $scope.userPins.length; i++){
                    if($scope.userPins[i].show == false){
                        return;
                    }
                }
                $scope.userPins[0].show = true;
            }
            else{
                $scope.userPins[0].show = false;
                if(i == 0){
                    for(j in $scope.userPins){
                        if(j == 0) continue;
                        if($scope.userPins[j].show){
                            removeLayer($scope.userPins[j].title)
                            $scope.userPins[j].show = false;
                        }
                    }
                    return;
                }
                removeLayer($scope.userPins[i].title)
            }
        }else if (scope == 'histos'){
            $scope.histos[i].show = !$scope.histos[i].show;
            if($scope.histos[i].show){
                if(i == 0){
                    for(j in $scope.histos){
                        if(j == 0) continue;
                        if(!$scope.histos[j].show){
                            loadPins(histoAlertes[j-1]);
                            $scope.histos[j].show = true;
                        }
                    }
                    return;
                }
                loadPins(histoAlertes[i-1]);
                for(var i = 1; i < $scope.histos.length; i++){
                    if($scope.histos[i].show == false){
                        return;
                    }
                }
                $scope.histos[0].show = true;
            }
            else{
                $scope.histos[0].show = false;
                if(i == 0){
                    for(j in $scope.histos){
                        if(j == 0) continue;
                        if($scope.histos[j].show){
                            removeLayer($scope.histos[j].title)
                            $scope.histos[j].show = false;
                        }
                    }
                    return;
                }
                removeLayer($scope.histos[i].title)
            }
        }

    }

    function loadAllPins(alertes){
        for(i in alertes){
            var jsonObject = {
                type: "FeatureCollection",
                features: alertes[i].data
            }
            pinsToMap(alertes[i].type, jsonObject);
        }
    }

    function loadPins(alertes){

        var jsonObject = {
            type: "FeatureCollection",
            features: alertes.data
        }
        pinsToMap(alertes.type, jsonObject);
    }


    function pinsToMap(type, jsonFile){
        var icon = "../images/pin_feu.png";

        switch (type){
            case "Inondation" :
                icon = "../images/pin_goutte.png";
                break;
            case "Suivi des cours d'eau" :
                icon = "../images/pin_goutte.png";
                break;
            case "Vent" :
                icon = "../images/pin_vent.png";
                break;
            case "Pluie" :
                icon = "../images/pin_pluie.png";
                break;
            case "Neige" :
                icon = "../images/pin_pluie.png";
                break;
            case "Meteo" :
                icon = "../images/pin_vent.png";
                break;
            case "Feu" :
                icon = "../images/pin_feu.png";
                break;
            case "Terrain" :
                icon = "../images/pin_seisme.png";
                break;
            case "Eau" :
                icon = "../images/pin_goutte.png";
                break;
        }

        var image = new ol.style.Icon(/** @type {olx.style.IconOptions} */ ({
            anchor: [0.5, 30],
            anchorXUnits: 'fraction',
            anchorYUnits: 'pixels',
            opacity: 0.8,
            src: icon
          }))

        var styles = {
            'Point': new ol.style.Style({
                image: image
            })
        };

        var styleFunction = function(feature) {
            return styles[feature.getGeometry().getType()];
        };

        var geojsonObject = jsonFile;

        var vectorSource = new ol.source.Vector({
            features: (new ol.format.GeoJSON()).readFeatures(geojsonObject,
                {featureProjection: map.getView().getProjection()}),

        });

        var vectorLayer = new ol.layer.Vector({
            title: type,
            source: vectorSource,
            style: styleFunction
        });
        map.addLayer(vectorLayer);
    }

    function removeLayer(type){
        var layersToRemove = [];
        map.getLayers().forEach(function (layer) {
            if (layer.get('title') != undefined && layer.get('title') === type) {
                layersToRemove.push(layer);
            }
        });

        var len = layersToRemove.length;
        for(var i = 0; i < len; i++) {
            map.removeLayer(layersToRemove[i]);
        }
    }

    clickedPin = function(coords){
        for(i in alertes){
            var found = false;
            for(j in alertes[i].data){
                if(Math.abs(alertes[i].data[j].geometry.coordinates[0] - coords[0]) < 0.00005 && Math.abs(alertes[i].data[j].geometry.coordinates[1] - coords[1]) < 0.0005){
                    found = true;
                    overlay.getElement().innerHTML = "<h5>" +
                        alertes[i].data[j].risque +
                        "</h5><div>" +
                        alertes[i].data[j].description +
                        "</div><small>" +
                        alertes[i].data[j].source +
                        "</small><br><small>" +
                        alertes[i].data[j].dateDeMiseAJour +
                        "</small>";
                    return;
                }
            }
        }
        if(!found){
            for(i in userAlertes){
                var found = false;
                for(j in userAlertes[i].data){
                    if(Math.abs(userAlertes[i].data[j].geometry.coordinates[0] - coords[0]) < 0.00005 && Math.abs(userAlertes[i].data[j].geometry.coordinates[1] - coords[1]) < 0.0005){
                        found = true;
                        overlay.getElement().innerHTML = "<h5>" +
                            userAlertes[i].data[j].risque +
                            "</h5><small>Alerte d'usager</small><div>" +
                            userAlertes[i].data[j].certitude +
                            "</div><small>" +
                            userAlertes[i].data[j].source +
                            "</small><br><small>" +
                            userAlertes[i].data[j].dateDeMiseAJour +
                            "</small>";
                        return;
                    }
                }
            }

        }
        if(!found){
            for(i in histoAlertes){
                var found = false;
                for(j in histoAlertes[i].data){
                    found = true;
                    if(Math.abs(histoAlertes[i].data[j].geometry.coordinates[0] - coords[0]) < 0.00005 && Math.abs(histoAlertes[i].data[j].geometry.coordinates[1] - coords[1]) < 0.0005){
                        overlay.getElement().innerHTML = "<h5>" +
                            histoAlertes[i].data[j].risque +
                            "</h5><small>Alerte Historique</small><div>" +
                            histoAlertes[i].data[j].description +
                            "</div><small>" +
                            histoAlertes[i].data[j].source +
                            "</small><br><small>" +
                            histoAlertes[i].data[j].dateDeMiseAJour +
                            "</small>";
                        return;
                    }
                }
            }
        }

    }


    $scope.search = function(){
        Request.googleAPI($scope.searchForm).then(function(data){
            var point = [data.data.results[0].geometry.location.lng, data.data.results[0].geometry.location.lat];
            var size = /** @type {ol.Size} */ (map.getSize());
            view.centerOn(ol.proj.transform(point, 'EPSG:4326', 'EPSG:3857'), size, [size[0]/2, size[1]/2]);
        })
    }

}]);
