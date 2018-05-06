package com.example.payne.simpletestapp;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;

public class Alerte {

    private double latitude;
    private double longitude;
    public String nom;
    public String source;
    public String territoire;
    public String certitude;
    public String severite;
    public String type;
    public String dateDeMiseAJour;
    public String idAlerte;
    public String urgence;
    public String description;

    /**
     * Create an Alert object from the a JSON file (GEOJSON format)
     *
     * @param jsonFile
     */
    public Alerte(JSONObject jsonFile) {

        try {

            // TODO : redo this from template

            this.nom = jsonFile.getString("nom");
            this.source = jsonFile.getString("source");
            this.territoire = jsonFile.getString("territoire");
            this.certitude = jsonFile.getString("certitude");
            this.severite = jsonFile.getString("severite");
            this.type = jsonFile.getString("type");
            this.dateDeMiseAJour = jsonFile.getString("dateDeMiseAJour");
            // this.idAlerte = jsonFile.getString("idAlerte");
            this.urgence = jsonFile.getString("urgence");
            this.description = jsonFile.getString("description");

            JSONObject geom = jsonFile.getJSONObject("geometry");
            this.latitude = (double) geom.getJSONArray("coordinates").get(1);
            this.longitude = (double) geom.getJSONArray("coordinates").get(0);

            Log.w("ALERTES", this.latitude + " -=-=- " + this.longitude);

        } catch (JSONException j){
            j.printStackTrace();
        }

    }

    public Alerte(double longitude, double latitude, String type){

        this.longitude = longitude;
        this.latitude = latitude;
        this.type = type;

    }

    public Alerte(GeoPoint point, String type){

        this.longitude = point.getLongitude();
        this.latitude = point.getLatitude();
        this.type = type;

    }

    public void log(){
        Log.w("Alerte : ", this.toString());
    }

    @Override
    public String toString(){

        String result = "";

        result += "nom : " + this.nom + "\n";
        result += "position : lat = " + this.getLatitude() + " - longitude = " + this.getLongitude();
        result += "type : " + this.type;

        return result;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
