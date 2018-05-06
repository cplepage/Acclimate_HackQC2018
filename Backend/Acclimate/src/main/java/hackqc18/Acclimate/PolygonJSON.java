package hackqc18.Acclimate;

import java.util.ArrayList;

public class PolygonJSON {

    private final CoordinatesJSON coordinates = new CoordinatesJSON();

    public PolygonJSON(ArrayList<double[]> points) {
        for (double[] pair : points) {
            coordinates.add(pair[0], pair[1]);
        }
    }
    
        
    @Override
    public String toString() {
        return "{\n\"type\": \"Polygon\",\n" 
                + coordinates.toString() + "}";
    }

}
