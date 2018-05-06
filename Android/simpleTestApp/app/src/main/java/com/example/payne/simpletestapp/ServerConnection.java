package com.example.payne.simpletestapp;

import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import org.osmdroid.util.BoundingBox;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ServerConnection {

    public String serverAddress;
    public int port;

    public ServerConnection(String addr, int port){
        this.serverAddress = addr;
        this.port = port;

    }

    public ServerConnection(String addr){

        this.port = 80;
        this.serverAddress = addr;

    }

    /**
     * Envoie une requête au serveur de façon périodique.
     * <p></p>
     * Reçoit toutes les alertes sur le territoire
     *
     * @return
     */
    public String ping(BoundingBox boundingBox) throws Exception {

        String param =
            "?nord=" + boundingBox.getLatNorth() +
            "&sud=" + boundingBox.getLatSouth() +
            "&est=" + boundingBox.getLonEast() +
            "&ouest=" + boundingBox.getLonWest();

        return this.getRequest("/alertes", param);

    }


    public String getRequest(final String path, final String param) throws Exception {

        final Response result = new Response();

        Thread connection = new Thread(new Runnable() {

            @Override
            public void run() {

                try{
                    URL obj = new URL(serverAddress + path + param);
                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                    // optional default is GET
                    con.setRequestMethod("GET");
                    // int responseCode = con.getResponseCode();

                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }

                    in.close();

                    result.response = response.toString();

                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        connection.start();
        connection.join();


        return result.response;

    }

    public Boolean postAlert(Alerte alerte) {

        boolean success = false;
        alerte.log();

        String param =  "?type=" + alerte.type +
                        "&lat=" + alerte.getLatitude() +
                        "&lng=" + alerte.getLongitude();

        try {
            this.getRequest("/putAlert", param);
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return success;

    }


}
