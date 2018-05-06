package hackqc18.Acclimate;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api")
public class GetUserAlertController {

//    @Autowired
    @RequestMapping(value = "/getUserAlerts",
            produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String getAlertController(
            @RequestParam(value="nord", defaultValue="66.") double nord,
            @RequestParam(value="sud", defaultValue="40.") double sud,
            @RequestParam(value="est", defaultValue="-58.") double est,
            @RequestParam(value="ouest", defaultValue="-84.") double ouest) {

        return new GetUserAlert().alerts(nord, sud, est, ouest);
    }
}