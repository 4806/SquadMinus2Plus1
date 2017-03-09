package SocialWiki.Cookies;

import SocialWiki.Users.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by Connor on 2017-03-08.
 */
public class CookieManager {

    public static Cookie getUserCookie(String userName) {
        // create the user cookie
        Cookie c = new Cookie("user", userName);
        c.setMaxAge(86400); // expires after a day

        return c;
    }

    public static Cookie getClearUserCookie() {
        // create a cookie that will overwrite the user cookie
        Cookie c = new Cookie("user", "");
        c.setMaxAge(0); // expires immediately

        return c;
    }

    public static boolean checkUserCookie(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }

        User user = (User) session.getAttribute("user");

        Cookie[] cookies = request.getCookies();
        for (Cookie c : cookies) {
            if (c.getName().equals("user")) {
                if (!c.getValue().equals(user.getUserName())) {
                    return false;
                }
                return true;
            }
        }

        return true;
    }
}
