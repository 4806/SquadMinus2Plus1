package SocialWiki.Cookies;

import SocialWiki.Users.User;
import SocialWiki.WikiPages.ConcreteWikiPage;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

/**
 * Created by Connor on 2017-03-08.
 */
public class CookieManager {

    /**
     * Get a new user cookie that corresponds to provided userName
     * @param userName - the value that will be stored in the user cookie
     * @return the new user cookie
     */
    public static Cookie getUserCookie(String userName) {
        // create the user cookie
        Cookie c = new Cookie("user", userName);
        c.setMaxAge(86400); // expires after a day

        return c;
    }

    /**
     * Get a cookie that effectively clears the client's user cookie
     * @return the clear user cookie
     */
    public static Cookie getClearUserCookie() {
        // create a cookie that will overwrite the user cookie
        Cookie c = new Cookie("user", "");
        c.setMaxAge(0); // expires immediately

        return c;
    }

    /**
     * Checks that the userName attached to the request's session matches the name in the user cookie also attached
     * @param request - an HTTP request that contains user data that needs to be validated
     * @return whether the session and cookie userNames are equal
     */
    public static boolean checkUserCookie(HttpServletRequest request) {
        // check that the session exists, if not, check is false
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }

        // get the user from the session
        String username = (String) session.getAttribute("user");

        // iterate over the request's cookies and check if the user one exists and matches the session user
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals("user")) {
                    if (c.getValue().equals(username)) {
                        return true;
                    }
                    return false;
                }
            }
        }

        // the cookie doesn't exist, so it doesn't need to be cleared
        return true;
    }

    /**
     * Get a cookie that signifies if the user likes the current page or not
     * @param user - the user that is currently logged in
     * @param id - the Id of the page that the user does or does not like
     * @return a cookie that signifies if the user likes the page
     */
    public static Cookie getIsLikedCookie(User user, Long id) {
        Cookie c;

        // if the page id is within the liked page list, return true cookie
        for (ConcreteWikiPage page : user.getLikedPages()) {
            if (page.getId().equals(id)) {
                c = new Cookie("isLiked", "true");
                c.setMaxAge(86400);
                return c;
            }
        }

        // the page is not liked, so return false cookie
        c = new Cookie("isLiked", "false");
        c.setMaxAge(86400);
        return c;
    }

    /**
     * Get a cookie that effectively clears the client's isLiked cookie
     * @return the clear isLiked cookie
     */
    public static Cookie getClearIsLikedCookie() {
        // create a cookie that will overwrite the isLiked cookie
        Cookie c = new Cookie("isLiked", "");
        c.setMaxAge(0); // expires immediately

        return c;
    }

    /**
     * Get a cookie that signifies if the user follows the current user or not
     * @param user - the user that is currently logged in
     * @param followUser - the username of the user that the user does or does not follow
     * @return a cookie that signifies if the user follows the user, they do not, or clears the cookie
     */
    public static Cookie getIsFollowedCookie(User user, String followUser) {
        Cookie c;

        // if the username is within the followed user list, return true cookie
        for (User u : user.getFollowedUsers()) {
            if (u.getUserName().equals(followUser)) {
                c = new Cookie("isFollowed", "true");
                c.setMaxAge(86400);
                return c;
            }
        }

        // the user is not followed, so return false cookie
        c = new Cookie("isFollowed", "false");
        c.setMaxAge(86400);
        return c;
    }

    /**
     * Get a cookie that effectively clears the client's isFollowed cookie
     * @return the clear isLiked cookie
     */
    public static Cookie getClearIsFollowedCookie() {
        // create a cookie that will overwrite the isFollowed cookie
        Cookie c = new Cookie("isFollowed", "");
        c.setMaxAge(0); // expires immediately

        return c;
    }
}
