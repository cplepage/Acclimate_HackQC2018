package hackqc18.Acclimate;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

public class Alerte {

    private static AtomicLong counter = new AtomicLong();

    private final String nom;
    private final String source;
    private final String territoire;
    private String certitude;
    private final String severite;
    private final String type;
    private String dateDeMiseAJour;
    private final String idAlerte;
    private final String urgence;
    private final String description;
    private final String geom;
    private int count;
    private final long id;
    private CoordinatesJSON coord;

    public Alerte(String nom, String source, String territoire,
            String certitude, String severite, String type,
            String dateDeMiseAJour, String idAlerte, String urgence,
            String description, String geom, CoordinatesJSON coord) {

        this.nom = nom;
        this.source = source;
        this.territoire = territoire;
        this.certitude = certitude;
        this.severite = severite;
        this.type = type;
        this.dateDeMiseAJour = dateDeMiseAJour;
        this.idAlerte = idAlerte;
        this.urgence = urgence;
        this.description = description;
        this.geom = geom;
        this.id = counter.incrementAndGet();
        this.count = 1;
        this.coord = coord;

    }

    public CoordinatesJSON getCoord() {
        return coord;
    }

    
    public void increment(double lat, double lng, String date) {
        // TODO - renormalisé la posiiton du point
        //      ((x*count)+lat)/(count+1)
        //      ((y*count)+lng)/(count+1)
        count++;
        dateDeMiseAJour = date;
        if (count == 10) {
            certitude = "Observé";
        } else if (count == 5) {
            certitude = "Probable";
        }
    }

    /**
     * This method assume that the alert date is in the following format:
     *      AAAA-MM-JJTHH:MM:SS
     * @param days number of days
     * @param hours number of hours
     * @param minutes number of minutes
     * @return true if the alert date is older than the one given
     */
    public boolean isOlderThan(int days, int hours, int minutes) {
        LocalDateTime alrTime = LocalDateTime.parse(dateDeMiseAJour);
        LocalDateTime now = LocalDateTime.now();
        
        int dDays = now.getDayOfYear() - alrTime.getDayOfYear() - days;
        int dHours = now.getHour() - alrTime.getHour() - hours;
        int dMin = now.getMinute() - alrTime.getMinute() - minutes;
        return (dDays > 0 || (dDays == 0 &&
                (dHours > 0 || (dHours == 0 && dMin > 0))));
    }
    
    public String getNom() {
        return nom;
    }

    public String getSource() {
        return source;
    }

    public String getTerritoire() {
        return territoire;
    }

    public String getCertitude() {
        return certitude;
    }

    public String getSeverite() {
        return severite;
    }

    public String getType() {
        return type;
    }

    public String getDateDeMiseAJour() {
        return dateDeMiseAJour;
    }

    public String getIdAlerte() {
        return idAlerte;
    }

    public String getUrgence() {
        return urgence;
    }

    public String getDescription() {
        return description;
    }

    public String getGeom() {
        return geom;
    }

    @Override
    public String toString() {
        return "{\"alerte\" : {"
                + "\"id\": \"" + idAlerte + "\","
                + "\"count\": \"" + count + "\","
                + "\"nom\": \"" + nom + "\","
                + "\"source\": \"" + source + "\","
                + "\"territoire\": \"" + territoire + "\","
                + "\"certitude\": \"" + certitude + "\","
                + "\"severite\": \"" + severite + "\","
                + "\"type\": \"" + type + "\","
                + "\"dateDeMiseAJour\": \"" + dateDeMiseAJour + "\","
                + "\"urgence\": \"" + urgence + "\","
                + "\"description\": \"" + description + "\","
                + "\"geometry\": " + geom
                + "}}";

    }
}
