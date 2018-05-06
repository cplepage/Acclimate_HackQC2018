var clickedPin;

var view = new ol.View({
    center: ol.proj.transform([-73.656830, 45.516136], 'EPSG:4326', 'EPSG:3857'),
    projection: 'EPSG:3857',
    zoom: 9
});

var map = new ol.Map({
    layers: [
        new ol.layer.Tile({
            source: new ol.source.OSM()
        })
    ],
    target: 'map',
    controls: ol.control.defaults({
        attributionOptions: {
            collapsible: false
        }
    }),
    view: view
});

var overlay = new ol.Overlay({
  element: document.getElementById('popup-container'),
  positioning: 'bottom-center',
  offset: [0, -45]
});
map.addOverlay(overlay);

map.on('click', function(e) {
  overlay.setPosition();
  var features = map.getFeaturesAtPixel(e.pixel);
  if (features) {
    var coords = features[0].getGeometry().getCoordinates();
    var hdms = ol.proj.transform(coords, 'EPSG:3857', 'EPSG:4326');
    clickedPin(hdms);
    overlay.setPosition(coords);
  }
});
