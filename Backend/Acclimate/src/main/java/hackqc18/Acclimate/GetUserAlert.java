package hackqc18.Acclimate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRawValue;


public class GetUserAlert {

    public GetUserAlert(){
    }

    @JsonRawValue
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String alerts(double nord, double sud, double est, double ouest) {
        Alertes theAlerts = new Alertes(nord, sud, est, ouest,
                PutAlert.getUserAlerts());
        
        return theAlerts.toString();
    }

}
