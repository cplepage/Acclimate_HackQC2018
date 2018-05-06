package com.example.demo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRawValue;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class getAlert {



    JSONArray file;

    public static JSONArray parseJSONFile(String filename) throws JSONException, IOException {
        String content = new String(Files.readAllBytes(Paths.get(filename)));
        return new JSONArray(content);
    }

    public getAlert(){
        try {
            String filename = "src/main/recentAlert.json";
            this. file = parseJSONFile(filename);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @JsonRawValue
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getFile() {
        return this.file.toString();
    }

}
