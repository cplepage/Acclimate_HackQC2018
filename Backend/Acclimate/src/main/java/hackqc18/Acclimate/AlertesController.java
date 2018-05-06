package hackqc18.Acclimate;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class AlertesController {

//    @Autowired
    @RequestMapping(value="/alertes",produces="application/json;charset=UTF-8")
    public String alertes(
            @RequestParam(value="nord", defaultValue="66.") double nord,
            @RequestParam(value="sud", defaultValue="40.") double sud,
            @RequestParam(value="est", defaultValue="-58.") double est,
            @RequestParam(value="ouest", defaultValue="-84.") double ouest) {
        return new Alertes(nord, sud, est, ouest).toString();
    }
}

