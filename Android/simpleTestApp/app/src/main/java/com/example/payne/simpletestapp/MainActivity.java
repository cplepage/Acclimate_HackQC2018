package com.example.payne.simpletestapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.InfoWindow;


public class MainActivity extends AppCompatActivity implements MapEventsReceiver {

    public static final double[] MONTREAL_COORD = {45.5161, -73.6568};

    MapView map = null;
    public MapDisplay myMap;
    public static MapEventsOverlay mapEventsOverlay;
    public static MainActivity mainActivity;
    public static Marker lastPlacedPin = null;
    public static Manager manager;
    public static Menu menu;
    public static Marker pin_on_focus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //handle permissions first, before map is created. not depicted here
        final Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        mainActivity = this;

        //inflate and create the map
        setContentView(R.layout.activity_main);

        //creating the Toolbar?
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        /*
        TODO: GPS
        https://developer.android.com/guide/topics/location/strategies
         */

        map = findViewById(R.id.map);
        myMap = new MapDisplay(map, this);
        map.setTileSource(TileSourceFactory.MAPNIK);

        // set zoom control and multi-touch gesture
        map.setBuiltInZoomControls(false);
        map.setMultiTouchControls(true);

        // default initial value
        IMapController mapController = map.getController();
        mapController.setZoom(12);
        GeoPoint startPoint = new GeoPoint(MONTREAL_COORD[0], MONTREAL_COORD[1]);
        mapController.setCenter(startPoint);

        // setup app backend
        manager = new Manager(this, myMap);



        // Logo button
        findViewById(R.id.logo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Merci d'utiliser Acclimate :) " +
                        "Nous allons sauver la planête un petit geste à la fois!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        /*
        Setting up Events for "Confirm Alert Dialog"
         */
        findViewById(R.id.confirm_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.mainActivity.findViewById(R.id.confirm_dialog).setVisibility(View.GONE);
            }
        });

        findViewById(R.id.confirm_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: GÉRER LE "YES" DU DIALOG
                // Remove temporary Pin
                map.getOverlays().remove(lastPlacedPin);
                String type="";
                if (pin_on_focus.getTitle().contains("Feu")) type = "Feu";
                if (pin_on_focus.getTitle().contains("Eau")) type = "Eau";
                if (pin_on_focus.getTitle().contains("Meteo")) type = "Meteo";
                if (pin_on_focus.getTitle().contains("Terrain")) type = "Terrain";

                String param =  "?type=" + type.toLowerCase() +
                        "&lat=" + pin_on_focus.getPosition().getLatitude() +
                        "&lng=" + pin_on_focus.getPosition().getLongitude();
                try{
                    manager.mainServer.getRequest("/putAlert", param);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                MainActivity.mainActivity.findViewById(R.id.confirm_dialog).setVisibility(View.GONE);
                MapDisplay.currentlyPlacingPin = false;

                pin_on_focus.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker, MapView mapView) {
                        marker.showInfoWindow();
                        mapView.getController().animateTo(marker.getPosition());
                        return true;
                    }
                });

                // Hide PopUp

            }
        });


        /*
        Setting up Events for "New Alert Type" prompt dialog
         */
        findViewById(R.id.wind_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Remove temporary Pin
                map.getOverlays().remove(lastPlacedPin);

                // Hide PopUp
                MainActivity.mainActivity.findViewById(R.id.pop_up).setVisibility(View.GONE);
                MapDisplay.currentlyPlacingPin = false;

                // locally register alert
                Alerte alert = new Alerte(lastPlacedPin.getPosition().getLongitude(),
                        lastPlacedPin.getPosition().getLatitude(),
                        "Meteo");

                myMap.addUserAlertPin(alert, MapDisplay.meteoIcon);
                manager.postAlert(alert);
            }
        });

        findViewById(R.id.water_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Remove temporary Pin
                map.getOverlays().remove(lastPlacedPin);

                // Hide PopUp
                MainActivity.mainActivity.findViewById(R.id.pop_up).setVisibility(View.GONE);
                MapDisplay.currentlyPlacingPin = false;

                // locally register alert
                Alerte alert = new Alerte(lastPlacedPin.getPosition().getLongitude(),
                        lastPlacedPin.getPosition().getLatitude(),
                        "Eau");

                myMap.addUserAlertPin(alert, MapDisplay.eauIcon);
                manager.postAlert(alert);

            }
        });

        findViewById(R.id.fire_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Remove temporary Pin
                map.getOverlays().remove(lastPlacedPin);

                // Hide PopUp
                MainActivity.mainActivity.findViewById(R.id.pop_up).setVisibility(View.GONE);
                MapDisplay.currentlyPlacingPin = false;

                // locally register alert
                Alerte alert = new Alerte(lastPlacedPin.getPosition().getLongitude(),
                        lastPlacedPin.getPosition().getLatitude(),
                        "Feu");

                myMap.addUserAlertPin(alert, MapDisplay.feuIcon);
                manager.postAlert(alert);

            }
        });

        findViewById(R.id.earth_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Remove temporary Pin
                map.getOverlays().remove(lastPlacedPin);

                // Hide PopUp
                MainActivity.mainActivity.findViewById(R.id.pop_up).setVisibility(View.GONE);
                MapDisplay.currentlyPlacingPin = false;

                // locally register alert
                Alerte alert = new Alerte(lastPlacedPin.getPosition().getLongitude(),
                        lastPlacedPin.getPosition().getLatitude(),
                        "Terrain");

                myMap.addUserAlertPin(alert, MapDisplay.terrainIcon);
                manager.postAlert(alert);

            }
        });


        // Cancel button
        findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Remove canceled Pin
                map.getOverlays().remove(lastPlacedPin);
                map.invalidate();

                // Hide PopUp
                MainActivity.mainActivity.findViewById(R.id.pop_up).setVisibility(View.GONE);
                MapDisplay.currentlyPlacingPin = false;
            }
        });


        /* TODO : Add "SEARCH" and "HISTORIQUE"
        https://developer.android.com/training/appbar/action-views */


        mapEventsOverlay = new MapEventsOverlay(this, this);
        map.getOverlays().add(0, mapEventsOverlay);

    }

    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        InfoWindow.closeAllInfoWindowsOn(map);
        return false;
    }

    @Override
    public boolean longPressHelper(GeoPoint p) {
        myMap.addUserPin(p);
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MainActivity.menu = menu;

        //Setting up Search bar
        MenuItem ourSearchItem = menu.findItem(R.id.action_search);
        SearchView sv = (SearchView) ourSearchItem.getActionView();
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                // TODO : eventually remove this Toast and FIX
                Toast.makeText(MainActivity.this, query, Toast.LENGTH_SHORT).show();

                BoundingBox boundingBox = JSONWrapper.googleBoundingBox(query);

                if (boundingBox != null)
                    map.zoomToBoundingBox(boundingBox, false);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            /*
            case (R.id.action_settings):
                Toast.makeText(this, "Who fucking cares...", Toast.LENGTH_SHORT).show();

                break;

            case (R.id.posBtn):
                BoundingBox current = map.getBoundingBox();

                double top = current.getLatNorth();
                double bottom = current.getLatSouth();
                double east = current.getLonEast();
                double west = current.getLonWest();

                String descr = "north : " + top + " \n" +
                        "south : " + bottom + " \n" +
                        "east :" + east + " \n" +
                        "west : " + west;

                Toast.makeText(this, descr, Toast.LENGTH_SHORT).show();
                break;

            case (R.id.centerBtn):
                IGeoPoint center = map.getMapCenter();
                Toast.makeText(this,
                        "Center\n Lat : " + center.getLatitude() +
                                "\nLong : " + center.getLongitude(),
                        Toast.LENGTH_SHORT).show();
            */

            case (R.id.highlight):
                manager.queryNewPins();
                break;

            case (R.id.maj):
                // TODO: Server Request for all Monitored Zones
                break;

            case (R.id.add):
                myMap.highlightCurrent(findViewById(android.R.id.content));
                myMap.refresh();
                break;

            /*
            case (R.id.removeAll):
                myMap.removeAll(findViewById(android.R.id.content), mapEventsOverlay);
                break;

            case (R.id.addUserPin):
                myMap.addUserPin(myMap.getCenter(), "seisme");
                break;

            case (R.id.circleBtn):
                myMap.drawCircleAtCenter(1000, 5);
                break;


*/
            case (R.id.cB_histo):
                // TODO: Toggle "Display Historique" (add filters)
                break;

            case (R.id.cB_users):
                if (item.isChecked()) {
                    MapDisplay.showUserPins = false;
                    item.setChecked(false);
                    myMap.refresh();
                } else {
                    MapDisplay.showUserPins = true;
                    item.setChecked(true);
                    myMap.refresh();
                }
                break;

            case (R.id.cB_zones):
                if (item.isChecked()) {
                    MapDisplay.isHighlight = false;
                    item.setChecked(false);
                    myMap.refresh();
                } else {
                    MapDisplay.isHighlight = true;
                    item.setChecked(true);
                    myMap.refresh();
                }
                break;

            case (R.id.cB_fire):
                if (item.isChecked()) {
                    MapDisplay.feuFilter = false;
                    item.setChecked(false);
                    myMap.refresh();
                } else {
                    MapDisplay.feuFilter = true;
                    item.setChecked(true);
                    myMap.refresh();
                }
                break;

            case (R.id.cB_water):
                if (item.isChecked()) {
                    MapDisplay.eauFilter = false;
                    item.setChecked(false);
                    myMap.refresh();
                } else {
                    MapDisplay.eauFilter = true;
                    item.setChecked(true);
                    myMap.refresh();
                }
                break;

            case (R.id.cB_terrain):
                if (item.isChecked()) {
                    MapDisplay.terrainFilter = false;
                    item.setChecked(false);
                    myMap.refresh();
                } else {
                    MapDisplay.terrainFilter = true;
                    item.setChecked(true);
                    myMap.refresh();
                }
                break;

            case (R.id.cB_meteo):
                if (item.isChecked()) {
                    MapDisplay.meteoFilter = false;
                    item.setChecked(false);
                    myMap.refresh();
                } else {
                    MapDisplay.meteoFilter = true;
                    item.setChecked(true);
                    myMap.refresh();
                }
                break;

            case (R.id.profileBtn):
                Toast.makeText(this, "Profile btn clicked", Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onResume() {
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    public void onPause() {
        super.onPause();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }
}
