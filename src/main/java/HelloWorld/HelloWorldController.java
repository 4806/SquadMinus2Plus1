package HelloWorld;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by connor on 2/21/17.
 */

@Controller
@RequestMapping("/")
public class HelloWorldController {

    @Autowired
    private Message msg;

    @RequestMapping(method = RequestMethod.GET)
    public String hello(Model model) {
        model.addAttribute("msg", msg);
        return "hello";
    }

}
