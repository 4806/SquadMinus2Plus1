package SocialWiki.FrontPages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class PageController {

    @Autowired
    private Message msg;

    @RequestMapping(method = RequestMethod.GET)
    public String hello(Model model) {
        model.addAttribute("msg", msg);
        return "hello";
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

}
