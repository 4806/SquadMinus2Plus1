package SocialWiki.FrontPages;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        return "signup";
    }

    @GetMapping("/editwiki")
    public String editwiki(Model model){
        return "editwiki";
    }

    @GetMapping("/search")
    public String search(Model model){
        return "search";
    }

    @GetMapping("/viewpage")
    public String viewpage(Model model){
        return "viewpage";
    }

}
