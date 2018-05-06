package com.example.utilisateur.tesosmdroid;

import android.graphics.Color;
import android.view.View;
import android.widget.Toast;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.infowindow.BasicInfoWindow;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper pour le map de OSMdroid
 *
 * Created by Utilisateur on 2018-05-03.
 */

public class MapDisplay {

    public MapView map;
    private double[] lastTouch = {0, 0};

    public MapDisplay(MapView map){
        this.map = map;
    }

    /**
     * North, south, east, west
     * @return
     */
    public double[] getBoundingBox(){
        BoundingBox current = map.getBoundingBox();
        double[] result = new double[4];

        result[0] = current.getLatNorth();
        result[1] = current.getLatSouth();
        result[2] = current.getLonEast();
        result[3] = current.getLonWest();

        return result;

    }

    public void highlightCurrent(View view){

        double[] corners = getBoundingBox();
        List<GeoPoint> geoPoints = new ArrayList<>();

        //add your points here
        geoPoints.add(new GeoPoint(corners[0],corners[2])); // North East
        geoPoints.add(new GeoPoint(corners[0],corners[3])); // North West
        geoPoints.add(new GeoPoint(corners[1],corners[3])); // South West
        geoPoints.add(new GeoPoint(corners[1],corners[2])); // South East

        Polygon polygon = new Polygon(this.map);    //see note below
        polygon.setFillColor(Color.argb(75, 255,0,0));
        geoPoints.add(geoPoints.get(0));    //forces the loop to close
        polygon.setPoints(geoPoints);

        // style
        polygon.setStrokeColor(Color.argb(75, 255,0,0));
        polygon.setStrokeWidth(0);

        // infos
        polygon.setTitle("TITLE : A sample polygon");
        polygon.setSnippet("A Snippet");
        polygon.setSubDescription("A Subdescription");

        this.map.getOverlayManager().add(polygon);
        this.map.invalidate();
    }

    public void removeAll(View view, MapEventsOverlay mapEventsOverlay){

        Toast.makeText(view.getContext(), this.map.getOverlayManager().overlays().size() - 1 + " items removed", Toast.LENGTH_SHORT).show();
        this.map.getOverlayManager().removeAll(this.map.getOverlays());

        map.getOverlays().add(0, mapEventsOverlay);

        this.map.invalidate();

    }

    public void addPin(GeoPoint pos){

        Marker pin = new Marker(map);
        pin.setPosition(pos);
        pin.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);

        pin.setTitle("TITLE : A pin");
        pin.setSubDescription("A subdescripton");
        pin.setSnippet("A snippet");

        map.getOverlays().add(pin);
        this.map.invalidate();

    }

    public void drawCircleAtCenter(int radius, int shade){

        for (int i = 1; i <= shade + 1; i++){

            ArrayList<GeoPoint> circlePoints = new ArrayList<>();

            for (float f = 0; f < 360; f += 1){
                circlePoints.add(new GeoPoint(
                        this.getCenter().getLatitude(), this.getCenter().getLongitude())
                        .destinationPoint(i * (radius/shade), f));
            }

            Polygon circle = new Polygon(this.map);    //see note below
            circlePoints.add(circlePoints.get(0));    //forces the loop to close
            circle.setPoints(circlePoints);

            // define style
            circle.setStrokeWidth(0);
            circle.setStrokeColor(Color.argb(25, 10,255,10));
            circle.setFillColor(Color.argb(75, 10,255,10));


            map.getOverlayManager().add(circle);


        }

        map.invalidate();


    }

    public GeoPoint getCenter(){
        return (GeoPoint) this.map.getMapCenter();
    }

    public void setLastTouch(double x, double y){
        this.lastTouch[0] = x; this.lastTouch[1] = y;
    }

    public double[] getLastTouch(){
        return this.lastTouch;
    }

}
