package hackqc18.Acclimate;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class PutAlert {

    private final static ArrayList<Alerte> USER_ALERTS = new ArrayList<>();
    
    private boolean status;
    private String statusMsg;
    
    public static ArrayList<Alerte> getUserAlerts() {
        return new ArrayList<>(USER_ALERTS);
    }

    public PutAlert(String type, String latGet, String lngGet) {
        this.status = false;
        double lat = Double.parseDouble(lngGet);
        double lng = Double.parseDouble(latGet);
        String date = LocalDateTime.now().toString();

        if (lat > -84 && lat < -58 && lng > 40 && lng < 66) {
            
            String userAlrId = (int)distanceInMeter(-84, lng, lat, lng) + "-"
                    + (int)distanceInMeter(lat, 66, lat, lng);

            for (Alerte alerte : USER_ALERTS) {
                if (alerte.getIdAlerte().equals(userAlrId)) {
                    alerte.increment(lat, lng, date);
                    this.status = true;
                    this.statusMsg = "Alerte comfirmée, merci de votre participation!";
                    return;
                }
                // TODO - for demo the delay is set à 0 days, 0 hours, 5 minutes
                if (alerte.isOlderThan(0, 0, 5)) {
                    USER_ALERTS.remove(alerte);
                }

            }
            
            PointJSON point = new PointJSON(lat, lng);
            Alerte alerte = new Alerte(type, "usager", "inconnu",
                    "à déterminer", "inconnue", type, date, userAlrId, "inconnue",
                    "alerte usager", point.toString(), point.getCoord());
            
            USER_ALERTS.add(alerte);
            
            this.status = true;
            this.statusMsg = "Nouvelle alerte ajoutée. Merci pour votre aide!";
        } else {
            this.statusMsg = "Coordonnées non supportées pour le moment.";
        }

    }

    // generally used geo measurement function
    public double distanceInMeter(double lat1, double lon1, double lat2, double lon2) {
        double R = 6378.137; // Radius of earth in KM
        double dLat = (lat2 - lat1) * Math.PI / 180;
        double dLon = (lon2 - lon1) * Math.PI / 180;
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180)
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c;
        return d * 1000; // meters
    }

    public String isStatus() {
        return this.statusMsg;
    }

}
