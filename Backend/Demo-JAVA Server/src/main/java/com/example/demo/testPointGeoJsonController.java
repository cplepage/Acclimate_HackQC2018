package com.example.demo;


import org.json.JSONException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class testPointGeoJsonController {


    @RequestMapping(value = "/api/testPoint", produces = "application/json")
    public @ResponseBody testPointGeoJson testPointGeoJsonController() throws JSONException {
        return new testPointGeoJson();
    }
}