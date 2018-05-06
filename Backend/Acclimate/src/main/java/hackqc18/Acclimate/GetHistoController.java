
package hackqc18.Acclimate;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class GetHistoController {
    @RequestMapping(value = "/getHisto",
            produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String getAlertController(
            @RequestParam(value="nord", defaultValue="66.") double nord,
            @RequestParam(value="sud", defaultValue="40.") double sud,
            @RequestParam(value="est", defaultValue="-58.") double est,
            @RequestParam(value="ouest", defaultValue="-84.") double ouest) {

        return new GetHisto().alerts(nord, sud, est, ouest);
    }
}
