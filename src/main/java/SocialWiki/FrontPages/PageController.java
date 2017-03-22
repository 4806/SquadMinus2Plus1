package SocialWiki.FrontPages;

import SocialWiki.Cookies.CookieManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class PageController {

    @GetMapping("/")
    public String index(HttpServletRequest request, HttpServletResponse response) {
        if (!CookieManager.checkUserCookie(request)) {
            response.addCookie(CookieManager.getClearUserCookie());
        }
        return "index";
    }

    @GetMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response) {
        if (!CookieManager.checkUserCookie(request)) {
            response.addCookie(CookieManager.getClearUserCookie());
        }
        return "login";
    }

    @GetMapping("/signup")
    public String signup(HttpServletRequest request, HttpServletResponse response) {
        if (!CookieManager.checkUserCookie(request)) {
            response.addCookie(CookieManager.getClearUserCookie());
        }
        return "signup";
    }

    @GetMapping("/search")
    public String search(HttpServletRequest request, HttpServletResponse response){
        if (!CookieManager.checkUserCookie(request)) {
            response.addCookie(CookieManager.getClearUserCookie());
        }
        return "search";
    }

    @GetMapping("/viewpage")
    public String viewpage(HttpServletRequest request, HttpServletResponse response){
        if (!CookieManager.checkUserCookie(request)) {
            response.addCookie(CookieManager.getClearUserCookie());
        }
        return "viewpage";
    }

    @GetMapping("/editwiki")
    public String editwiki(HttpServletRequest request, HttpServletResponse response){
        if (!CookieManager.checkUserCookie(request)) {
            response.addCookie(CookieManager.getClearUserCookie());
        }
        return "editwiki";
    }

    @GetMapping("/history")
    public String history(HttpServletRequest request, HttpServletResponse response){
        if (!CookieManager.checkUserCookie(request)) {
            response.addCookie(CookieManager.getClearUserCookie());
        }
        return "history";
    }

    @GetMapping("/profile")
    public String profule(HttpServletRequest request, HttpServletResponse response){
        if (!CookieManager.checkUserCookie(request)) {
            response.addCookie(CookieManager.getClearUserCookie());
        }
        return "profile";
    }

}
