package com.example.demo;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api")
public class getAlertController {

    @Autowired
    @RequestMapping(value = "/latest", produces = "application/json")
    public @ResponseBody
    String getAlertController() throws JSONException {

        return new getAlert().getFile();
    }
}