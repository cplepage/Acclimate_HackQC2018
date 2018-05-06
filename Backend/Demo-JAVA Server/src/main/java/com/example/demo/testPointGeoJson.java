package com.example.demo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class testPointGeoJson {

    private JSONObject point = new JSONObject();

    public testPointGeoJson() throws JSONException {

        this.point.put("type", "Feature");

        JSONObject properties = new JSONObject();
        properties.put("date_observation", "2017/04/27 00:00:00");
        properties.put("code_municipalite", "93055");
        properties.put("nom", "Labrecque");
        properties.put("urgence", "Inconnue");
        properties.put("certitude", "Observé");
        properties.put("type", "Inondation");
        properties.put("type", "Mineure");
        properties.put("type", "Actuel");

        this.point.put("properties", properties);

        JSONObject geometry = new JSONObject();
        geometry.put("type", "Point");
        JSONArray coordinates = new JSONArray();
        coordinates.put(-71.49859980203153);
        coordinates.put(48.67906237862251f);
        geometry.put("coordinates", coordinates);

        this.point.put("geometry", geometry);



    }


    @JsonRawValue
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "PointTest")
    public String getPoint() {
        return this.point.toString();
    }


}
