package hackqc18.Acclimate;

import java.util.ArrayList;

public class CoordinatesJSON {

    private final ArrayList<double[]> data = new ArrayList<>();

    public CoordinatesJSON() {
    }
    
    public CoordinatesJSON(double x, double y) {
        data.add(new double[]{x, y});
    }

    public void add(double x, double y) {
        data.add(new double[]{x, y});
    }

    public ArrayList<double[]> getData() {
        return data;
    }


    @Override
    public String toString() {
        String result = "\"coordinates\": ";

        for (int i = 0; i < data.size() - 1; i++) {
            result += "[" + data.get(i)[0] + "," + data.get(i)[1] + "],";
        }
        result += "[" + data.get(data.size() - 1)[0] 
                + "," + data.get(data.size() - 1)[1] + "]";

        return result;
    }
}
