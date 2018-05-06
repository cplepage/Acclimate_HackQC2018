package com.example.payne.simpletestapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;


public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.home);

        findViewById(R.id.proceed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.textView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void onResume() {
        super.onResume();

        ServerConnection tmpServer = new ServerConnection(Manager.SERVER_ADDR, Manager.PORT);

        String quebec, userPins;

        // valeurs totales :)
        int feu = 0;
        int eau = 0;
        int meteo = 0;
        int terrain = 0;

        // valeurs d'usagers
        int feuU = 0;
        int eauU = 0;
        int meteoU = 0;
        int terrainU = 0;

        try {
            quebec = tmpServer.ping(MapDisplay.QUEBEC_BOUNDING_BOX);
            userPins = tmpServer.getRequest("/getUserAlerts", "");

            JSONArray alertesQuebec = (new JSONObject(quebec).getJSONArray("alertes"));
            JSONArray alertesuser = (new JSONObject(userPins).getJSONArray("alertes"));

            for (int i = 0; i < alertesQuebec.length(); i++){
                switch (((JSONObject) alertesQuebec.get(i)).getJSONObject("alerte").getString("type")) {
                    case "Feu" : feu++; break;
                    case "Eau" : eau++; break;
                    case "Inondation" : eau++; break;
                    case "Suivi des cours d'eau" : eau++; break;
                    case "pluie" : meteo++; break;
                    case "vent" : meteo++;
                    case "Meteo" : meteo++; break;
                    case "Terrain" : terrain++; break;
                    default: meteo++; break;
                }
            }

            for (int i = 0; i < alertesuser.length(); i++){
                switch (((JSONObject) alertesuser.get(i)).getJSONObject("alerte").getString("type")) {
                    case "Feu" : feuU++; break;
                    case "Eau" : eauU++; break;
                    case "Meteo" : meteoU++; break;
                    case "Terrain" : terrainU++; break;
                    default: meteo++; break;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        ((TextView) findViewById(R.id.dlb_eauMain)).setText(String.valueOf(eau)+ " alertes actives");
        ((TextView) findViewById(R.id.dlb_feuMain)).setText(String.valueOf(feu)+ " alertes actives");
        ((TextView) findViewById(R.id.dlb_meteoMain)).setText(String.valueOf(meteo)+ " alertes actives");
        ((TextView) findViewById(R.id.dlb_terrainMain)).setText(String.valueOf(terrain)+ " alertes actives");

        ((TextView) findViewById(R.id.dlb_eauSec)).setText("+" + String.valueOf(eauU)+ " saisies d'USAGERS");
        ((TextView) findViewById(R.id.dlb_feuSec)).setText("+" + String.valueOf(feuU)+ " saisies d'USAGERS");
        ((TextView) findViewById(R.id.dlb_meteoSec)).setText("+" + String.valueOf(meteoU)+ " saisies d'USAGERS");
        ((TextView) findViewById(R.id.dlb_terrainSec)).setText("+" + String.valueOf(terrainU)+ " saisies d'USAGERS");
    }
}
