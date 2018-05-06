package com.example.demo;


import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class testJSONController {
    @Autowired
    @RequestMapping(value = "/JSON", produces = "application/json")
    public @ResponseBody String testJSONController() throws JSONException {
        return new testJSON().getFile();
    }
}