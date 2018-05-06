package com.example.utilisateur.tesosmdroid;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MapEventsReceiver{

    public static final double[] MONTREAL_COORD = {45.5161, -73.6568};

    public MapView map = null;
    public MapDisplay myMap = null;
    public MapEventsOverlay mapEventsOverlay;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);


        //handle permissions first, before map is created. not depicted here

        //load/initialize the osmdroid configuration, this can be done
        final Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        //setting this before the layout is inflated is a good idea
        //it 'should' ensure that the map has a writable location for the map cache, even without permissions
        //if no tiles are displayed, you can try overriding the cache path using Configuration.getInstance().setCachePath
        //see also StorageUtils
        //note, the load method also sets the HTTP User Agent to your application's package name, abusing osm's tile servers will get you banned based on this string

        //inflate and create the map
        setContentView(R.layout.activity_main);

        map = (MapView) findViewById(R.id.map);
        myMap = new MapDisplay(map);
        map.setTileSource(TileSourceFactory.MAPNIK);

        // set zoom control and multi-touch gesture
        map.setBuiltInZoomControls(false);
        map.setMultiTouchControls(true);

        // default initial value
        IMapController mapController = map.getController();
        mapController.setZoom(12);
        GeoPoint startPoint = new GeoPoint(MONTREAL_COORD[0], MONTREAL_COORD[1]);
        mapController.setCenter(startPoint);

        Button posBtn = (Button) findViewById(R.id.posBtn);
        Button centerBtn = (Button) findViewById(R.id.centerBtn);
        Button highlight = (Button) findViewById(R.id.highlight);
        Button removeAll = (Button) findViewById(R.id.removeAll);
        Button addPin = (Button) findViewById(R.id.addPin);
        Button circleBtn = (Button) findViewById(R.id.circleBtn);



        posBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BoundingBox current = map.getBoundingBox();

                double top = current.getLatNorth();
                double bottom = current.getLatSouth();
                double east = current.getLonEast();
                double west = current.getLonWest();

                String descr =  "north : " + top + " \n" +
                                "south : " + bottom + " \n" +
                                "east :" + east + " \n"  +
                                "west : " + west;

                Toast.makeText(view.getContext(), descr ,
                        Toast.LENGTH_SHORT).show();
            }
        });

        centerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IGeoPoint center = map.getMapCenter();
                Toast.makeText(view.getContext(),
                        "Center\n Lat : " + center.getLatitude() +
                            "\nLong : " + center.getLongitude(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        highlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myMap.highlightCurrent(view);
            }
        });

        removeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myMap.removeAll(view, mapEventsOverlay);
            }
        });

        circleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myMap.drawCircleAtCenter(1000, 5);
            }
        });

        addPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myMap.addPin(myMap.getCenter());
            }
        });

        mapEventsOverlay = new MapEventsOverlay(this, this);
        map.getOverlays().add(0, mapEventsOverlay);



    }

    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        Toast.makeText(this, "tapped :)", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public boolean longPressHelper(GeoPoint p) {
        myMap.addPin(p);
        return false;
    }


    public void onResume(){
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    public void onPause(){
        super.onPause();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}