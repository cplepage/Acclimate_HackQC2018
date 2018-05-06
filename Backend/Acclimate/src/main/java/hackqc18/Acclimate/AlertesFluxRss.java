package hackqc18.Acclimate;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 *
 */
public class AlertesFluxRss {

    private static AlertesFluxRss theInstance = null;
    
    private ArrayList<Double> coordX = new ArrayList<>();
    private ArrayList<Double> coordY = new ArrayList<>();
    private ArrayList<Alerte> alertes = new ArrayList<>();


    public static AlertesFluxRss theInstance() {
        if (theInstance == null) {
            theInstance = new AlertesFluxRss();
        }
        return theInstance;
    }
    
    public AlertesFluxRss() {
        parseFeed();

    }

    public void parseFeed() {
        String contRss = getRssFeed();
        ArrayList<String> alertePrg = getInfos("<item>", 5, "</item>", contRss);

        String nom, source, territoire, certitude, severite, type;
        String dateDeMiseAJour, urgence, description, geom;
        for (int i = 0; i < alertePrg.size(); i++) {
            nom = getInfosStr("<title>", 0, "</title>", alertePrg.get(i));
            String coords = getInfosStr("<b>Urgence</b>", 0, "amp;zoom", alertePrg.get(i));
            geom = getInfosStr("center=", 4, "&", coords);
            String auteur = getInfosStr("Auteur", 150, "br/>", alertePrg.get(i));
            source = getInfosStr(":", 5, "<", auteur);
            type = getInfosStr("<b>Type</b> :", 1, "<br/>", alertePrg.get(i));
            dateDeMiseAJour = getInfosStr("<b>Date de mise à jour</b> :", 1,
                    "<br/>", alertePrg.get(i));
            description = getInfosStr("<b>Description</b> :", 1, "<br/>", alertePrg.get(i));
            severite = getInfosStr("<b>Sévérite</b> :", 1, "<br/>", alertePrg.get(i));
            territoire = getInfosStr("<b>Secteur</b> :", 1, "<br/>", alertePrg.get(i));
            certitude = getInfosStr("<b>Certitude</b> :", 1, "<br/>", alertePrg.get(i));
            urgence = getInfosStr("<b>Urgence</b> :", 1, "<br/>", alertePrg.get(i));
            String coordos = geom + "<>";
            double x = Double.parseDouble(("-" + getInfosStr("-", 0, ",", coordos)));
            double y = Double.parseDouble((getInfosStr(",", (x + "").length(),
                    "<>", coordos)));
            this.coordX.add(x);
            this.coordY.add(y);

            PointJSON point = new PointJSON(x, y);
            alertes.add(new Alerte(nom, source, territoire,
                    certitude, severite, type, dateDeMiseAJour, "00000", urgence,
                    description, point.toString(), point.getCoord()));
        }

    }

    public static String getRssFeed() {
        try {
            String rss = "";
            URL rssSource = new URL("https://geoegl.msp.gouv.qc.ca/avp/rss/");
            URLConnection rssSrc = rssSource.openConnection();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            rssSrc.getInputStream(), StandardCharsets.UTF_8));
            String inputLine;
            
            while ((inputLine = in.readLine()) != null) {
                rss += inputLine;
            }
            
            in.close();
            
            String rssCleaned = rss.replaceAll("&lt;", "<").replaceAll("&gt;", ">").substring(564);
            
            return rssCleaned;
        } catch (MalformedURLException ex) {
            Logger.getLogger(AlertesFluxRss.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AlertesFluxRss.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static String getInfosStr(String balise, int offset, String fin, String rss) {
        String liste = "";
        String rssText = rss;
        int ix;
        for (String word : rssText.split(fin)) {
            if (word.contains(balise)) {
                ix = rssText.indexOf(word) + balise.length();
                liste += (rssText.substring(ix + offset, rssText.indexOf(fin, ix + 1)));
            }
        }
        return liste;
    }

    public static ArrayList<String> getInfos(String balise, int offset, String fin, String rss) {
        ArrayList<String> liste = new ArrayList<>();
        String rssText = rss;
        int ix;
        for (String word : rssText.split(fin)) {
            if (word.contains(balise)) {
                ix = rssText.indexOf(word) + balise.length();
                liste.add(rssText.substring(ix + offset, rssText.indexOf(fin, ix + 1)));
                //System.out.println(rssText.substring(ix + offset, rssText.indexOf(fin, ix + 1)));
            }
        }
        return liste;
    }

    public ArrayList<Alerte> alertsInBox(double nord, double sud, double est, double ouest) {

        if (ouest <= -84 || est >= -58 || sud <= 40 || nord >= 66) {
            return new ArrayList<>(alertes);
        }

        ArrayList<Alerte> result = new ArrayList<>();
        for (int i = 0; i < alertes.size(); i++) {
            if (coordX.get(i) > ouest && coordX.get(i) < est
                    && coordY.get(i) > sud && coordY.get(i) < nord) {
                result.add(alertes.get(i));
            }
        }
        return result;
    }
}
