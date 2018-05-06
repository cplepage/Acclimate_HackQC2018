/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hackqc18.Acclimate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRawValue;

/**
 *
 * @author incognito
 */
public class GetHisto {
    private static StaticParser parser = new StaticParser("historique_alertes.csv");

    @JsonRawValue
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String alerts(double nord, double sud, double est, double ouest) {
        Alertes theAlerts = new Alertes(nord, sud, est, ouest, parser.getAlertes());

        return theAlerts.toString();
    }

}
